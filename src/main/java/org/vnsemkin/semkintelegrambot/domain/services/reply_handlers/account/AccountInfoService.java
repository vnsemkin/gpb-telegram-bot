package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.account;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountDto;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.MessageHandler;

@Service
@RequiredArgsConstructor
public final class AccountInfoService implements MessageHandler {
    private static final String CUSTOMER_ACCOUNT = "Счет пользователя: ";
    private static final String ACCOUNT_BALANCE = "Баланс счета: ";
    private static final String NO_INFO = "Нет информации по счету";
    private static final String NEW_LINE = "\n";
    private final TgSenderInterface sender;
    private final AppWebClient appWebClient;

    @Override
    public void handle(@NonNull Message message) {
        long chatId = message.getChatId();
        Long id = message.getFrom().getId();
        Result<AccountDto, String> result = appWebClient.getCustomerAccount(id);
        String response = result.isSuccess() ?
            customerAccountInfoSuccess(result) : customerAccountInfoError(result);
        sender.sendText(chatId, response);
    }

    private String customerAccountInfoSuccess(@NonNull Result<AccountDto, String> result) {
        return result.getData()
            .map(this::getAccountInfoFormatedString).orElse(NO_INFO);
    }

    private String getAccountInfoFormatedString(@NonNull AccountDto account) {
        return CUSTOMER_ACCOUNT + account.accountName() + NEW_LINE +
            ACCOUNT_BALANCE + account.balance();
    }

    private String customerAccountInfoError(@NonNull Result<AccountDto, String> result) {
        return result.getError().orElse(NO_INFO);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.CURRENT_BALANCE.value;
    }
}