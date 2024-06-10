package org.vnsemkin.semkintelegrambot.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerRegistrationDto;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.domain.models.Customer;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
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
    private static final long TG_ID = 120198730L;
    private static final String USERNAME = "username";
    private static final String TG_USERNAME = "test";
    private static final String VALID_NAME = "ValidName";
    private static final String VALID_EMAIL = "valid.email@example.com";
    private static final String INVALID_EMAIL = "invalid-email";
    private static final String VALID_PASSWORD = "ValidPassword";
    private static final String INVALID_PASSWORD = "invalid-password";
    private static final String ERROR_INVALID_EMAIL = "Invalid email";
    private static final String ERROR_INVALID_PASSWORD = "Invalid password";
    private static final String REG_SUCCESS_MESSAGE = "Регистрация успешно завершена!";
    private static final String EMPTY_STRING = "";
    private static final long CHAT_ID = 12345L;

    @Mock
    private AppValidator validator;
    @Mock
    private TgSenderInterface tgSenderInterface;
    @Mock
    private AppWebClient appWebClient;
    @Mock
    private Message message;
    @InjectMocks
    private UserStateHandler userStateHandler;
    private final Map<Long, String> messageIdToServiceMap = new ConcurrentHashMap<>();
    private final Map<Long, Customer> customersOnRegistrationMap = new ConcurrentHashMap<>();
    private static Customer customer;

    @BeforeAll
    public static void init() {
       customer = new Customer(TG_ID, VALID_NAME, USERNAME);
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userStateHandler = new UserStateHandler(validator, messageIdToServiceMap, appWebClient, tgSenderInterface);
    }

    @Test
    public void testHandleUserRegistrationState_waitingForEmail_validEmail() {
        customersOnRegistrationMap.put(CHAT_ID, customer);

        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getText()).thenReturn(VALID_EMAIL);
        when(validator.validateEmail(anyString())).thenReturn(Result.success(true));

        userStateHandler.handleUserRegistrationState(message, customer);

        assertEquals(VALID_EMAIL, customersOnRegistrationMap.get(CHAT_ID).getEmail());
        verify(tgSenderInterface).sendText(eq(CHAT_ID), contains("Введите пароль"));
    }

    @Test
    public void testHandleUserRegistrationState_waitingForEmail_invalidEmail() {
        Customer customer = new Customer(TG_ID, VALID_NAME, USERNAME);
        customersOnRegistrationMap.put(CHAT_ID, customer);

        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getText()).thenReturn(INVALID_EMAIL);
        when(validator.validateEmail(anyString())).thenReturn(Result.error(ERROR_INVALID_EMAIL));

        userStateHandler.handleUserRegistrationState(message, customer);

        verify(tgSenderInterface).sendText(eq(CHAT_ID), contains(ERROR_INVALID_EMAIL + "\nВведите email"));
    }

    @Test
    public void testHandleUserRegistrationState_waitingForPassword_validPassword() {
        customer.setEmail(VALID_EMAIL);
        customersOnRegistrationMap.put(CHAT_ID, customer);

        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getText()).thenReturn(VALID_PASSWORD);
        when(validator.validatePassword(anyString())).thenReturn(Result.success(true));

        Result<CustomerRegistrationDto, String> regResult =
            Result.success(new CustomerRegistrationDto(TG_ID, TG_USERNAME, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING));
        when(appWebClient.registerCustomer(any(CustomerRegistrationDto.class))).thenReturn(regResult);

        userStateHandler.handleUserRegistrationState(message, customer);

        verify(tgSenderInterface).sendText(eq(CHAT_ID), contains(REG_SUCCESS_MESSAGE));
    }

    @Test
    public void testHandleUserRegistrationState_waitingForPassword_invalidPassword() {
        customer.setEmail(VALID_EMAIL);
        customersOnRegistrationMap.put(CHAT_ID, customer);

        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getText()).thenReturn(INVALID_PASSWORD);
        when(validator.validatePassword(anyString())).thenReturn(Result.error(ERROR_INVALID_PASSWORD));

        userStateHandler.handleUserRegistrationState(message, customer);

        verify(tgSenderInterface).sendText(eq(CHAT_ID), contains(ERROR_INVALID_PASSWORD + "\nВведите пароль"));
    }
}