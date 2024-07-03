package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerInfoResponse;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.domain.models.Customer;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import org.vnsemkin.semkintelegrambot.domain.utils.AppValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.ACCOUNT_NAME;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.ACCOUNT_NOT_OPEN;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.ACCOUNT_PREFIX;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.ARROW_EMOJI;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.BOLD_START_TAG;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.BOLD_STOP_TAG;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.CHAT_ID;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.CUSTOMER_ALREADY_REGISTER;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.CUSTOMER_NOT_FOUND;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.CUSTOMER_PREFIX;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.DELIMITER_LINE;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.EMAIL;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.EMAIL_PREFIX;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.EMPTY_LINE;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.FIRSTNAME;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.INPUT_EMAIL;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.NEW_LINE;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.REGISTER_INFO;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.SMT_WRONG;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.USERNAME;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.USERNAME_PREFIX;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.USER_ID;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.UUID;


@ExtendWith(MockitoExtension.class)
class CustomerRegistrationServiceTest {
    @Mock
    private TgSenderInterface sender;
    @Mock
    private AppWebClient appWebClient;
    @Mock
    private AppValidator validator;
    @Mock
    private Map<Long, String> messageHandlerServiceMap;
    @InjectMocks
    private CustomerRegistrationService customerRegistrationService;

    @Test
    void startRegistration_CustomerExist_AccountNotOpen() {
        Message message = createMessage();
        CustomerInfoResponse customerInfoResponse =
            new CustomerInfoResponse(FIRSTNAME, USERNAME, EMAIL, UUID, null);
        Result<CustomerInfoResponse, String> result = Result.success(customerInfoResponse);

        when(appWebClient.getCustomerInfo(anyLong())).thenReturn(result);

        customerRegistrationService.startRegistration(message);

        verify(sender).sendText(CHAT_ID, customerInfoMessage(ACCOUNT_NOT_OPEN, customerInfoResponse));
    }

    @Test
    void startRegistration_CustomerExist_AccountOpen() {
        Message message = createMessage();
        CustomerInfoResponse customerInfoResponse =
            new CustomerInfoResponse(FIRSTNAME, USERNAME, EMAIL, UUID, ACCOUNT_NAME);
        Result<CustomerInfoResponse, String> result = Result.success(customerInfoResponse);

        when(appWebClient.getCustomerInfo(anyLong())).thenReturn(result);

        customerRegistrationService.startRegistration(message);

        verify(sender).sendText(CHAT_ID, customerInfoMessage(ACCOUNT_NAME, customerInfoResponse));
    }

    @Test
    void startRegistration_resultError() {
        Message message = createMessage();
        Result<CustomerInfoResponse, String> result = Result.error(SMT_WRONG);

        when(appWebClient.getCustomerInfo(anyLong())).thenReturn(result);

        customerRegistrationService.startRegistration(message);

        verify(sender).sendText(CHAT_ID, SMT_WRONG);
    }

    @Test
    void startRegistration_customerDoesNotExist() {
        Message message = createMessage();
        User user = message.getFrom();
        Result<CustomerInfoResponse, String> result = Result.error(CUSTOMER_NOT_FOUND);

        when(appWebClient.getCustomerInfo(anyLong())).thenReturn(result);

        customerRegistrationService.startRegistration(message);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(sender).sendSendMessage(messageCaptor.capture());
        verify(messageHandlerServiceMap).put(eq(CHAT_ID), eq(CommandToServiceMap.REGISTER.value));

        SendMessage capturedMessage = messageCaptor.getValue();
        assertEquals(createWelcomeMessage(user.getFirstName()).getText(), capturedMessage.getText());
    }

    @Test
    void handle_customerWithoutEmail() throws NoSuchMethodException,
        InvocationTargetException, IllegalAccessException {
        Message message = createMessage();
        Customer customer = new Customer(USER_ID, FIRSTNAME, USERNAME);
        Result<Boolean, String> result = Result.error(EMPTY_LINE);

        when(validator.validateEmail(anyString())).thenReturn(result);

        Method method = CustomerRegistrationService.class
            .getDeclaredMethod("handleUserRegistrationState",
                Message.class, Customer.class);
        method.setAccessible(true);
        method.invoke(customerRegistrationService, message, customer);

        verify(sender).sendText(CHAT_ID, EMPTY_LINE + NEW_LINE + INPUT_EMAIL);
    }


    private SendMessage createWelcomeMessage(String firstName) {
        String welcomeMessage = String.format(BOLD_START_TAG +
            REGISTER_INFO +
            BOLD_STOP_TAG +
            NEW_LINE +
            INPUT_EMAIL +
            ARROW_EMOJI, firstName);
        return new SendMessage(Long.toString(org.vnsemkin.semkintelegrambot.config.TestConstants.CHAT_ID), welcomeMessage);
    }

    private String customerInfoMessage(String account, CustomerInfoResponse response) {
        return CUSTOMER_PREFIX + response.firstName() + NEW_LINE +
            CUSTOMER_ALREADY_REGISTER + NEW_LINE +
            DELIMITER_LINE + NEW_LINE +
            EMAIL_PREFIX + response.email() + NEW_LINE +
            USERNAME_PREFIX + response.userName() + NEW_LINE +
            ACCOUNT_PREFIX + account + NEW_LINE;
    }

    private Message createMessage() {
        Message message = mock(Message.class);
        User user = mock(User.class);

        Mockito.lenient().when(message.getChatId()).thenReturn(CHAT_ID);
        Mockito.lenient().when(message.getFrom()).thenReturn(user);
        Mockito.lenient().when(user.getId()).thenReturn(USER_ID);
        Mockito.lenient().when(message.getText()).thenReturn(EMPTY_LINE);
        return message;
    }
}