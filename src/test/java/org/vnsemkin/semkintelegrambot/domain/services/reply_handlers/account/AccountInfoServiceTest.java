package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountDto;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.domain.models.Result;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vnsemkin.semkintelegrambot.config.TestConstants.*;

public class AccountInfoServiceTest {

    @Mock
    private TgSenderInterface sender;
    @Mock
    private AppWebClient appWebClient;
    @InjectMocks
    private AccountInfoService accountInfoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAccountInfo_onResultSuccess() {
        Message message = createMessage();
        AccountDto accountDto = new AccountDto(UUID, ACCOUNT_NAME, BIG_DECIMAL_ACCOUNT_BALANCE);
        Result<AccountDto, String> result = Result.success(accountDto);

        when(appWebClient.getCustomerAccount(anyLong())).thenReturn(result);

        accountInfoService.handle(message);

        String expectedResponse = CUSTOMER_ACCOUNT_MESSAGE +
            ACCOUNT_NAME + NEW_LINE + ACCOUNT_BALANCE_MESSAGE + BIG_DECIMAL_ACCOUNT_BALANCE;
        verify(sender).sendText(CHAT_ID, expectedResponse);
    }

    @Test
    void shouldReturnAccountNotFound_onResultError() {
        Message message = createMessage();
        Result<AccountDto, String> result = Result.error(ACCOUNT_NO_INFO);

        when(appWebClient.getCustomerAccount(anyLong())).thenReturn(result);

        accountInfoService.handle(message);

        verify(sender).sendText(CHAT_ID, ACCOUNT_NO_INFO);
    }

    private Message createMessage() {
        Message message = mock(Message.class);
        User user = mock(User.class);

        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(CHAT_ID);

        return message;
    }
}
