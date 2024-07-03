package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.transfer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerInfoResponse;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import org.vnsemkin.semkintelegrambot.domain.utils.AppValidator;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.ACCOUNT_CREATED;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.CHAT_ID;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.DEFAULT_ACCOUNT_NAME;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.EMAIL;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.FIRSTNAME;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.INVALID_AMOUNT;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.INVALID_DATA;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.INVALID_TRANSFER_AMOUNT;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.INVALID_TRANSFER_REQUEST;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.INVALID_TRANSFER_USERNAME;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.INVALID_USERNAME;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.TRANSFER_REQUEST;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.USERNAME;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.USER_OR_ACCOUNT_NOT_EXIST;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.UUID;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.VALID_USERNAME;

@ExtendWith(MockitoExtension.class)
public class TransferMoneyServiceTest {
    @Mock
    AppValidator validator;
    @Mock
    Map<Long, String> messageHandlerServiceMap;
    @Mock
    TgSenderInterface sender;
    @Mock
    AppWebClient appWebClient;
    @InjectMocks
    TransferMoneyService transferMoneyService;

    @Test
    void shouldReturnUserNotFound() {
        Message message = createMessage();
        Result<CustomerInfoResponse, String> result = Result.error(USER_OR_ACCOUNT_NOT_EXIST);
        when(appWebClient.getCustomerInfo(message.getChatId())).thenReturn(result);

        transferMoneyService.startTransferMoneyProcess(message);

        verify(sender).sendText(message.getChatId(), USER_OR_ACCOUNT_NOT_EXIST);
    }

    @Test
    void shouldAskInputTransferMoneyRequest() {
        Message message = createMessage();
        CustomerInfoResponse response =
            new CustomerInfoResponse(FIRSTNAME, USERNAME, EMAIL, UUID, DEFAULT_ACCOUNT_NAME);
        Result<CustomerInfoResponse, String> result = Result.success(response);

        when(appWebClient.getCustomerInfo(message.getChatId())).thenReturn(result);

        transferMoneyService.startTransferMoneyProcess(message);

        verify(sender).sendText(message.getChatId(), TRANSFER_REQUEST);
        verify(messageHandlerServiceMap).put(eq(CHAT_ID), eq(CommandToServiceMap.TRANSFER.value));
    }

    @Test
    void shouldReturnInvalidTransferMoneyRequest_onOneArgument() {
        Message message = createMessage();

        when(message.getText()).thenReturn(DEFAULT_ACCOUNT_NAME);

        transferMoneyService.handle(message);

        verify(sender).sendText(message.getChatId(), INVALID_TRANSFER_REQUEST);
    }

    @Test
    void shouldReturnInvalidTransferMoneyRequest_onThreeArguments() {
        Message message = createMessage();

        when(message.getText()).thenReturn(ACCOUNT_CREATED);

        transferMoneyService.handle(message);

        verify(sender).sendText(message.getChatId(), INVALID_TRANSFER_REQUEST);
    }

    @Test
    void shouldReturnErrorOnInvalidTransferUsername() {
        Message message = createMessage();
        Result<Boolean, String> result = Result.error(INVALID_DATA);

        when(validator.validateName(INVALID_USERNAME)).thenReturn(result);
        when(message.getText()).thenReturn(INVALID_TRANSFER_USERNAME);

        transferMoneyService.handle(message);

        verify(sender).sendText(message.getChatId(), INVALID_DATA);
    }

    @Test
    void shouldReturnErrorOnInvalidAmount() {
        Message message = createMessage();
        Result<Boolean, String> resultError = Result.error(INVALID_DATA);
        Result<Boolean, String> resultSuccess = Result.success(Boolean.TRUE);


        when(validator.validateName(VALID_USERNAME)).thenReturn(resultSuccess);
        when(validator.validateAmount(INVALID_AMOUNT)).thenReturn(resultError);
        when(message.getText()).thenReturn(INVALID_TRANSFER_AMOUNT);

        transferMoneyService.handle(message);

        verify(sender).sendText(message.getChatId(), INVALID_DATA);
    }


    private Message createMessage() {
        Message message = mock(Message.class);
        User user = mock(User.class);
        user.setId(CHAT_ID);
        Chat chat = mock(Chat.class);
        chat.setId(CHAT_ID);
        message.setChat(chat);

        when(message.getChatId()).thenReturn(CHAT_ID);
        return message;
    }
}
