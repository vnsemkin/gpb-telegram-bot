package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountRegistrationRequest;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountRegistrationResponse;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.MessageHandler;

@Service
@RequiredArgsConstructor
public class AccountRegistrationService implements MessageHandler {
    private final static String ACCOUNT_CREATED = "Счет успешно открыт ";
    private final static String SMT_WRONG = "Что-то пошло не так. Попробуйте позднее.";
    private final static String NEW_LINE = "\n";
    private final AppWebClient appWebClient;
    private final TgSenderInterface sender;

    @Override
    public void handle(Message message) {
        Long chatId = message.getChat().getId();
        Result<AccountRegistrationResponse, String> accountRegistrationResult =
            appWebClient.registerAccount(new AccountRegistrationRequest(message.getChat().getId()));
        String result = accountRegistrationResult.isSuccess() ?
            accountCreated(accountRegistrationResult) : accountCreationError(accountRegistrationResult);
        sender.sendText(chatId, result);
    }

    private String accountCreated(Result<AccountRegistrationResponse, String> result) {
        return result.getData().map(account -> ACCOUNT_CREATED +
                NEW_LINE +
                account.accountName() +
                NEW_LINE +
                account.balance() +
                NEW_LINE +
                account.info())
            .orElse(SMT_WRONG);
    }

    private String accountCreationError(Result<AccountRegistrationResponse, String> result) {
        return result.getError().orElse(SMT_WRONG);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.CREATE_ACCOUNT.value;
    }
}
