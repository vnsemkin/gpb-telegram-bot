package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.registration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerDto;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.application.mappers.AppMapper;
import org.vnsemkin.semkintelegrambot.application.constants.UserRegistrationState;
import org.vnsemkin.semkintelegrambot.domain.models.Customer;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import org.vnsemkin.semkintelegrambot.domain.utils.AppValidator;

import java.util.Map;

@Slf4j
@Service
public final class UserStateHandler {
    private static final String INPUT_EMAIL = "Введите email";
    private static final String INPUT_PASSWORD = "Введите пароль";
    private static final String NEW_LINE = "\n";
    private static final String INPUT_NAME = "Введите имя";
    private static final String HELLO = "Привет ";
    private static final String EMAIL = "Email: ";
    private static final String PASSWORD = "Пароль: ";
    private static final String REG_SUCCESS = "Регистрация успешно завершена!";
    private static final String REG_FAIL = "Что то пошло не так. Попробуйте зарегистрироваться заново :(";
    private final AppValidator validator;
    private final Map<Long, String> messageIdToServiceMap;
    private final AppWebClient appWebClient;
    private final TgSenderInterface tgSenderInterface;
    private Map<Long, Customer> customersOnRegistrationMap;
    private final AppMapper mapper = AppMapper.INSTANCE;

    public UserStateHandler(AppValidator validator,
                            Map<Long, String> messageIdToServiceMap,
                            AppWebClient appWebClient,
                            TgSenderInterface tgSenderInterface) {
        this.validator = validator;
        this.messageIdToServiceMap = messageIdToServiceMap;
        this.appWebClient = appWebClient;
        this.tgSenderInterface = tgSenderInterface;
    }

    public void handleUserRegistrationState(@NonNull Message message, @NonNull Map<Long,
        Customer> customersOnRegistrationMap) {
        this.customersOnRegistrationMap = customersOnRegistrationMap;
        long chatId = message.getChatId();
        String text = message.getText();
        Customer customer = customersOnRegistrationMap.computeIfAbsent(chatId, id -> new Customer());
        UserRegistrationState userState = getUserState(customer);

        switch (userState) {
            case WAITING_FOR_NAME -> handleNameInput(chatId, text, customer);
            case WAITING_FOR_EMAIL -> handleEmailInput(chatId, text, customer);
            case WAITING_FOR_PASSWORD -> handlePasswordInput(chatId, text, customer);
        }
    }

    private void handleNameInput(long chatId, String text, Customer customer) {
        Result<Boolean, String> result = validator.validateName(text);
        if (result.isSuccess()) {
            customer.setName(text);
            tgSenderInterface.sendText(chatId, buildCustomerInfoMessage(customer) + NEW_LINE + INPUT_EMAIL);
        } else {
            tgSenderInterface.sendText(chatId, getErrorMessage(result, INPUT_NAME));
        }
    }

    private void handleEmailInput(long chatId, String text, Customer customer) {
        Result<Boolean, String> result = validator.validateEmail(text);
        if (result.isSuccess()) {
            customer.setEmail(text);
            tgSenderInterface.sendText(chatId, buildCustomerInfoMessage(customer) + NEW_LINE + INPUT_PASSWORD);
        } else {
            tgSenderInterface.sendText(chatId, getErrorMessage(result, INPUT_EMAIL));
        }
    }

    private void handlePasswordInput(long chatId, String text, Customer customer) {
        Result<Boolean, String> result = validator.validatePassword(text);
        if (result.isSuccess()) {
            customer.setPassword(text);
            registerCustomer(chatId, customer);
        } else {
            tgSenderInterface.sendText(chatId, getErrorMessage(result, INPUT_PASSWORD));
        }
    }

    private void registerCustomer(long chatId, Customer customer) {
        Result<CustomerDto, String> regResult = appWebClient.registerCustomer(mapper.toDto(customer));
        if (regResult.isSuccess()) {
            tgSenderInterface.sendText(chatId, REG_SUCCESS);
        } else {
            tgSenderInterface.sendText(chatId, REG_FAIL);
        }
        messageIdToServiceMap.remove(chatId);
        customersOnRegistrationMap.remove(chatId);
    }

    private String getErrorMessage(Result<Boolean, String> result, String defaultMsg) {
        return result.getError().map(error -> error + NEW_LINE + defaultMsg).orElse(defaultMsg);
    }

    private String buildCustomerInfoMessage(Customer customer) {
        StringBuilder sb = new StringBuilder();
        if (customer.getName() != null) sb.append(HELLO).append(customer.getName()).append(NEW_LINE);
        if (customer.getEmail() != null) sb.append(EMAIL).append(customer.getEmail()).append(NEW_LINE);
        if (customer.getPassword() != null) sb.append(PASSWORD).append(customer.getPassword());
        return sb.toString();
    }

    private UserRegistrationState getUserState(Customer customer) {
        if (customer.getName() == null) return UserRegistrationState.WAITING_FOR_NAME;
        if (customer.getEmail() == null) return UserRegistrationState.WAITING_FOR_EMAIL;
        if (customer.getPassword() == null) return UserRegistrationState.WAITING_FOR_PASSWORD;
        return UserRegistrationState.REGISTRATION_COMPLETE;
    }
}