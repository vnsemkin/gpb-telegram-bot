package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.domain.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.domain.constants.UserRegistrationState;
import org.vnsemkin.semkintelegrambot.domain.models.Customer;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import org.vnsemkin.semkintelegrambot.domain.services.common.Sender;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RegistrationService implements MessageHandler {
    private static final String INPUT_NAME = "Введите имя";
    private static final String INPUT_EMAIL = "Введите email";
    private static final String INPUT_PASSWORD = "Введите пароль";
    private static final String HELLO = "Привет ";
    private static final String EMAIL = "Email: ";
    private static final String PASSWORD = "Пароль: ";
    private static final String NOT_VALID_NAME = "Неправильное имя";
    private static final String NEW_LINE = "\n";
    private static final String NOT_VALID_EMAIL = "Неправильный формат email";
    private static final String NOT_VALID_PASSWORD = "Неправильный формат пароля";
    private static final String REG_SUCCESS = "Регистрация успешно завершена!";
    private static final String REG_FAIL = "Что то пошло не так, повторите регистрацию.";
    private static final String ARROW_EMOJI = "⬇";
    private static final String BOLD_START_TAG = "<b>";
    private static final String BOLD_STOP_TAG = "</b>";
    private static final String REGISTER_INFO = """
        Добро пожаловать в Мини-Банк.
        Для регистрации пожалуйста !
        """;
    private final Map<Long, Customer> registeringUsers;
    private final Map<Long, String> messageIdToServiceMap;
    private final Sender sender;

    public RegistrationService(Map<Long, String> messageIdToServiceMap, Sender sender) {
        this.messageIdToServiceMap = messageIdToServiceMap;
        this.registeringUsers = new ConcurrentHashMap<>();
        this.sender = sender;
    }

    public void startPickUpInformation(long chatId) {
        registeringUsers.remove(chatId);
        Result<Message> messageResult = sender
            .sendSendMessage(startPickUpInfo(chatId));
        messageResult.ifSuccess(msg ->
            messageIdToServiceMap.put(msg.getChatId(), getServiceName()));
    }

    @Override
    public void handle(@NonNull Message message) {
        long chatId = message.getChatId();
        Customer customer = registeringUsers.computeIfAbsent(chatId, id -> new Customer());
        UserRegistrationState userState = getUserState(customer);
        String text = message.getText();
        switch (userState) {
            case WAITING_FOR_NAME -> {
                Result<Boolean> booleanResult = validateName(text);
                if (booleanResult.isSuccess()) {
                    customer.setName(text);
                    sender.sendText(chatId, getNewCustomer(chatId)
                        + NEW_LINE
                        + INPUT_EMAIL);
                } else {
                    sender.sendText(chatId, booleanResult.getError()
                        .getMessage()
                        + NEW_LINE
                        + INPUT_NAME);
                }
            }
            case WAITING_FOR_EMAIL -> {
                Result<Boolean> booleanResult = validateEmail(text);
                if (booleanResult.isSuccess()) {
                    customer.setEmail(text);
                    sender.sendText(chatId, getNewCustomer(chatId)
                        + NEW_LINE
                        + INPUT_PASSWORD);
                } else {
                    sender.sendText(chatId, booleanResult.getError()
                        .getMessage()
                        + NEW_LINE
                        + INPUT_PASSWORD);
                }
            }
            case WAITING_FOR_PASSWORD -> {
                Result<Boolean> booleanResult = validatePassword(text);
                if (booleanResult.isSuccess()) {
                    customer.setPassword(text);
                    if (completeRegistration(chatId)) {
                        sender.sendText(chatId, REG_SUCCESS);
                    } else {
                        sender.sendText(chatId, REG_FAIL);
                    }
                } else {
                    sender.sendText(chatId, booleanResult.getError()
                        .getMessage());
                    sender.sendText(chatId, INPUT_PASSWORD);
                }
            }
        }
    }

    private boolean completeRegistration(long chatId) {
        Customer customer = registeringUsers.remove(chatId);
        if (customer != null) {
            return true;
        }
        log.warn("Не удален chatId");
        return false;
    }

    private UserRegistrationState getUserState(Customer user) {
        if (user.getName() == null) {
            return UserRegistrationState.WAITING_FOR_NAME;
        } else if (user.getEmail() == null) {
            return UserRegistrationState.WAITING_FOR_EMAIL;
        } else if (user.getPassword() == null) {
            return UserRegistrationState.WAITING_FOR_PASSWORD;
        }
        return UserRegistrationState.REGISTRATION_COMPLETE;
    }

    private String getNewCustomer(long chatId) {
        Customer customer = registeringUsers.get(chatId);
        StringBuilder sb = new StringBuilder();
        if (customer.getName() != null) sb.append(HELLO).append(customer.getName()).append(NEW_LINE);
        if (customer.getEmail() != null) sb.append(EMAIL).append(customer.getEmail()).append(NEW_LINE);
        if (customer.getPassword() != null) sb.append(PASSWORD).append(customer.getPassword());
        return sb.toString();
    }

    private SendMessage startPickUpInfo(long chatId) {
        StringBuilder sb = new StringBuilder();
        sb.append(BOLD_START_TAG);
        sb.append(REGISTER_INFO);
        sb.append(BOLD_STOP_TAG);
        sb.append("\n");
        sb.append(INPUT_NAME);
        sb.append(ARROW_EMOJI);
        return new SendMessage(Long.toString(chatId), sb.toString());
    }

    private Result<Boolean> validateName(@NonNull String name) {
        if (name.matches("^[a-zA-Zа-яА-Я]+$") && name.length() <= 10) {
            return Result.success(true);
        }
        return Result.failure(new RuntimeException(NOT_VALID_NAME));
    }

    private Result<Boolean> validateEmail(@NonNull String email) {
        if (email.matches("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+") && email.length() <= 50) {
            return Result.success(true);
        }
        return Result.failure(new RuntimeException(NOT_VALID_EMAIL));
    }

    private Result<Boolean> validatePassword(@NonNull String password) {
        if (password.length() >= 5 && password.length() <= 10) {
            return Result.success(true);
        }
        return Result.failure(new RuntimeException(NOT_VALID_PASSWORD));
    }

    @Override
    public String getServiceName() {
        return CommandToServiceMap.REGISTER.service;
    }
}
