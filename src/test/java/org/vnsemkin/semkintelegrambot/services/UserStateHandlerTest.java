package org.vnsemkin.semkintelegrambot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerDto;
import org.vnsemkin.semkintelegrambot.application.externals.TgInterface;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.domain.models.Customer;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.registration.UserStateHandler;
import org.vnsemkin.semkintelegrambot.domain.utils.AppValidator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserStateHandlerTest {
    private static final String VALID_NAME = "ValidName";
    private static final String INVALID_NAME = "InvalidName";
    private static final String VALID_EMAIL = "valid.email@example.com";
    private static final String INVALID_EMAIL = "invalid-email";
    private static final String VALID_PASSWORD = "ValidPassword";
    private static final String INVALID_PASSWORD = "invalid-password";
    private static final String ERROR_INVALID_NAME = "Invalid name";
    private static final String ERROR_INVALID_EMAIL = "Invalid email";
    private static final String ERROR_INVALID_PASSWORD = "Invalid password";
    private static final String REG_SUCCESS_MESSAGE = "Регистрация успешно завершена!";
    private static final String EMPTY_STRING = "";
    private static final long CHAT_ID = 12345L;

    @Mock
    private AppValidator validator;
    @Mock
    private TgInterface tgInterface;
    @Mock
    private AppWebClient appWebClient;
    @Mock
    private Message message;
    @InjectMocks
    private UserStateHandler userStateHandler;
    private final Map<Long, String> messageIdToServiceMap = new ConcurrentHashMap<>();
    private final Map<Long, Customer> customersOnRegistrationMap = new ConcurrentHashMap<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userStateHandler = new UserStateHandler(validator, messageIdToServiceMap, appWebClient, tgInterface);
    }

    @Test
    public void testHandleUserRegistrationState_waitingForName_validName() {
        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getText()).thenReturn(VALID_NAME);
        when(validator.validateName(anyString())).thenReturn(Result.success(true));

        userStateHandler.handleUserRegistrationState(message, customersOnRegistrationMap);

        assertEquals(VALID_NAME, customersOnRegistrationMap.get(CHAT_ID).getName());
        verify(tgInterface).sendText(eq(CHAT_ID), contains("Введите email"));
    }

    @Test
    public void testHandleUserRegistrationState_waitingForName_invalidName() {
        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getText()).thenReturn(INVALID_NAME);
        when(validator.validateName(anyString())).thenReturn(Result.error(ERROR_INVALID_NAME));

        userStateHandler.handleUserRegistrationState(message, customersOnRegistrationMap);

        verify(tgInterface).sendText(eq(CHAT_ID), contains(ERROR_INVALID_NAME + "\nВведите имя"));
    }

    @Test
    public void testHandleUserRegistrationState_waitingForEmail_validEmail() {
        Customer customer = new Customer();
        customer.setName(VALID_NAME);
        customersOnRegistrationMap.put(CHAT_ID, customer);

        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getText()).thenReturn(VALID_EMAIL);
        when(validator.validateEmail(anyString())).thenReturn(Result.success(true));

        userStateHandler.handleUserRegistrationState(message, customersOnRegistrationMap);

        assertEquals(VALID_EMAIL, customersOnRegistrationMap.get(CHAT_ID).getEmail());
        verify(tgInterface).sendText(eq(CHAT_ID), contains("Введите пароль"));
    }

    @Test
    public void testHandleUserRegistrationState_waitingForEmail_invalidEmail() {
        Customer customer = new Customer();
        customer.setName(VALID_NAME);
        customersOnRegistrationMap.put(CHAT_ID, customer);

        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getText()).thenReturn(INVALID_EMAIL);
        when(validator.validateEmail(anyString())).thenReturn(Result.error(ERROR_INVALID_EMAIL));

        userStateHandler.handleUserRegistrationState(message, customersOnRegistrationMap);

        verify(tgInterface).sendText(eq(CHAT_ID), contains(ERROR_INVALID_EMAIL + "\nВведите email"));
    }

    @Test
    public void testHandleUserRegistrationState_waitingForPassword_validPassword() {
        Customer customer = new Customer();
        customer.setName(VALID_NAME);
        customer.setEmail(VALID_EMAIL);
        customersOnRegistrationMap.put(CHAT_ID, customer);

        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getText()).thenReturn(VALID_PASSWORD);
        when(validator.validatePassword(anyString())).thenReturn(Result.success(true));

        Result<CustomerDto, String> regResult =
            Result.success(new CustomerDto(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING));
        when(appWebClient.registerCustomer(any(CustomerDto.class))).thenReturn(regResult);

        userStateHandler.handleUserRegistrationState(message, customersOnRegistrationMap);

        verify(tgInterface).sendText(eq(CHAT_ID), contains(REG_SUCCESS_MESSAGE));
    }

    @Test
    public void testHandleUserRegistrationState_waitingForPassword_invalidPassword() {
        Customer customer = new Customer();
        customer.setName(VALID_NAME);
        customer.setEmail(VALID_EMAIL);
        customersOnRegistrationMap.put(CHAT_ID, customer);

        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getText()).thenReturn(INVALID_PASSWORD);
        when(validator.validatePassword(anyString())).thenReturn(Result.error(ERROR_INVALID_PASSWORD));

        userStateHandler.handleUserRegistrationState(message, customersOnRegistrationMap);

        verify(tgInterface).sendText(eq(CHAT_ID), contains(ERROR_INVALID_PASSWORD + "\nВведите пароль"));
    }
}