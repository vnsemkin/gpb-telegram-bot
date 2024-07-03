package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountRegistrationRequest;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountRegistrationResponse;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.domain.models.Result;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.ACCOUNT_CREATED;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.ACCOUNT_NAME;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.BIG_DECIMAL_ACCOUNT_BALANCE;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.CHAT_ID;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.EMPTY_LINE;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.NEW_LINE;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.SMT_WRONG;

public class AccountRegistrationServiceTest {
    @Mock
    private TgSenderInterface sender;
    @Mock
    private AppWebClient appWebClient;
    @InjectMocks
    private AccountRegistrationService accountRegistrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldRegisterAccount_onResultSuccess() {
        AccountRegistrationResponse response =
            new AccountRegistrationResponse(ACCOUNT_NAME, BIG_DECIMAL_ACCOUNT_BALANCE, EMPTY_LINE);
        Long chatId = createMessage().getChatId();
        Result<AccountRegistrationResponse, String> result = Result.success(response);

        when(appWebClient.registerAccount(new AccountRegistrationRequest(chatId))).thenReturn(result);

        accountRegistrationService.handle(createMessage());

        verify(sender).sendText(CHAT_ID, ACCOUNT_CREATED + NEW_LINE +
            ACCOUNT_NAME + NEW_LINE + BIG_DECIMAL_ACCOUNT_BALANCE + NEW_LINE);
    }

    @Test
    public void shouldReturnMessage_onResultError() {
        long chatId = createMessage().getChatId();
        Result<AccountRegistrationResponse, String> result = Result.error(SMT_WRONG);

        when(appWebClient.registerAccount(new AccountRegistrationRequest(chatId))).thenReturn(result);

        accountRegistrationService.handle(createMessage());

        verify(sender).sendText(CHAT_ID, SMT_WRONG);

    }

    private Message createMessage() {
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        chat.setId(CHAT_ID);
        message.setChat(chat);

        when(message.getChatId()).thenReturn(CHAT_ID);
        return message;
    }
}
