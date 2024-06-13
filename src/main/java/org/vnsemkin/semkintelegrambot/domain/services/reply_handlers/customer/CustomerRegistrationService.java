package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.customer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.application.constants.UserRegistrationState;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerInfoResponse;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerRegistrationDto;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.application.mappers.AppMapper;
import org.vnsemkin.semkintelegrambot.domain.models.Customer;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.MessageHandler;
import org.vnsemkin.semkintelegrambot.domain.utils.AppValidator;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public final class CustomerRegistrationService implements MessageHandler {
    private final static String CUSTOMER_NOT_FOUND = "Пользователь не найден";
    private static final String INPUT_EMAIL = "Введите email";
    private static final String CUSTOMER_PREFIX = "Пользователь: ";
    private static final String CUSTOMER_ALREADY_REGISTER = " уже зарегистрирован !";
    private static final String EMAIL_PREFIX = "Email: ";
    private static final String USERNAME_PREFIX = "Username: ";
    private static final String ACCOUNT_PREFIX = "Счет: ";
    private static final String INPUT_PASSWORD = "Введите пароль";
    private static final String ACCOUNT_NOT_OPEN = "Счет не открыт ! ";
    private static final String SPACE = " ";
    private static final String NEW_LINE = "\n";
    private static final String DELIMITER_LINE = "-------------------";
    private static final String HELLO = "Привет ";
    private static final String EMAIL = "Email: ";
    private static final String PASSWORD = "Пароль: ";
    private static final String REG_SUCCESS = "Регистрация успешно завершена!";
    private static final String REG_FAIL = "Что то пошло не так. Попробуйте зарегистрироваться заново :(";
    private static final String INPUT_NAME = "Введите email";
    private static final String ARROW_EMOJI = "⬇";
    private static final String BOLD_START_TAG = "<b>";
    private static final String BOLD_STOP_TAG = "</b>";
    private static final String REGISTER_INFO = """
        Привет %s
        Добро пожаловать в Мини-Банк.
        Для регистрации пожалуйста !
        """;
    private final Map<Long, String> messageHandlerServiceMap;
    private final TgSenderInterface sender;
    private final AppValidator validator;
    private final AppWebClient appWebClient;
    private final AppMapper mapper = AppMapper.INSTANCE;
    private final ThreadLocal<Customer> customerLocal = new ThreadLocal<>();
    ;

    public void startRegistration(@NonNull Message message) {
        long chatId = message.getChatId();
        User user = message.getFrom();
        Result<CustomerInfoResponse, String> customerInfo = appWebClient.getCustomerInfo(user.getId());
        if (customerInfo.getData().isPresent()) {
            CustomerInfoResponse response = customerInfo.getData().get();
            sender.sendText(chatId, getCustomerInfoMessage(response));
            return;
        }
        if(customerInfo.getError().isPresent()) {
            if (!customerInfo.getError().get().equals(CUSTOMER_NOT_FOUND)) {
                sender.sendText(chatId, customerInfo.getError().get());
                return;
            }
        }

        customerLocal.set(new Customer(user.getId(), user.getFirstName(), user.getUserName()));
        sender.sendSendMessage(createWelcomeMessage(chatId, user.getFirstName()));
        messageHandlerServiceMap.put(chatId, getHandlerName());
    }

    @Override
    public void handle(@NonNull Message message) {
        handleUserRegistrationState(message, customerLocal.get());
    }

    private SendMessage createWelcomeMessage(long chatId, String firstName) {
        String welcomeMessage = String.format(BOLD_START_TAG +
            REGISTER_INFO +
            BOLD_STOP_TAG +
            NEW_LINE +
            INPUT_NAME +
            ARROW_EMOJI, firstName);
        return new SendMessage(Long.toString(chatId), welcomeMessage);
    }

    private void handleUserRegistrationState(Message message, Customer customer) {
        long chatId = message.getChatId();
        String text = message.getText();
        UserRegistrationState userState = getUserState(customer);
        switch (userState) {
            case WAITING_FOR_EMAIL -> handleEmailInput(chatId, text, customer);
            case WAITING_FOR_PASSWORD -> handlePasswordInput(chatId, text, customer);
        }
    }

    private void handleEmailInput(long chatId, String text, Customer customer) {
        Result<Boolean, String> result = validator.validateEmail(text);
        if (result.isSuccess()) {
            customer.setEmail(text);
            sender.sendText(chatId, buildCustomerInfoMessage(customer)
                + NEW_LINE
                + INPUT_PASSWORD);
        } else {
            sender.sendText(chatId, getErrorMessage(result, INPUT_EMAIL));
        }
    }

    private void handlePasswordInput(long chatId, String text, Customer customer) {
        Result<Boolean, String> result = validator.validatePassword(text);
        if (result.isSuccess()) {
            customer.setPassword(text);
            registerCustomer(chatId, customer);
        } else {
            sender.sendText(chatId, getErrorMessage(result, INPUT_PASSWORD));
        }
    }

    private void registerCustomer(long chatId, Customer customer) {
        Result<CustomerRegistrationDto, String> registrationResult =
            appWebClient.registerCustomer(mapper.toDto(customer));
        String message = registrationResult.isSuccess() ?
            REG_SUCCESS : registrationResult.getError().orElse(REG_FAIL);
        sender.sendText(chatId, message);
        cleanupRegistrationMaps(chatId);
    }

    private String getErrorMessage(Result<Boolean, String> result, String defaultMsg) {
        return result.getError().map(error -> error + NEW_LINE + defaultMsg).orElse(defaultMsg);
    }

    private String buildCustomerInfoMessage(Customer customer) {
        StringBuilder sb = new StringBuilder();
        sb.append(HELLO).append(SPACE).append(customer.getFirstName()).append(NEW_LINE);
        if (customer.getEmail() != null) sb.append(EMAIL).append(customer.getEmail()).append(NEW_LINE);
        if (customer.getPassword() != null) sb.append(PASSWORD).append(customer.getPassword());
        return sb.toString();
    }

    private UserRegistrationState getUserState(Customer customer) {
        if (customer.getEmail() == null) return UserRegistrationState.WAITING_FOR_EMAIL;
        if (customer.getPassword() == null) return UserRegistrationState.WAITING_FOR_PASSWORD;
        return UserRegistrationState.REGISTRATION_COMPLETE;
    }

    private void cleanupRegistrationMaps(long chatId) {
        messageHandlerServiceMap.remove(chatId);
        customerLocal.remove();
    }

    private String getCustomerInfoMessage(CustomerInfoResponse response) {
        String account = response.accountName() == null ? ACCOUNT_NOT_OPEN : response.accountName();
        return CUSTOMER_PREFIX + response.firstName() + NEW_LINE +
            CUSTOMER_ALREADY_REGISTER + NEW_LINE +
            DELIMITER_LINE + NEW_LINE +
            EMAIL_PREFIX + response.email() + NEW_LINE +
            USERNAME_PREFIX + response.username() + NEW_LINE +
            ACCOUNT_PREFIX + account + NEW_LINE;
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.REGISTER.value;
    }
}