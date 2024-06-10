package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.account.AccountInfoService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrentBalanceCommandHandler implements CommandHandler{
    private final Map<Long, String> messageHandlerServiceMap;
    private final AccountInfoService accountInfoService;

    @Override
    public void handle(Message message) {
        messageHandlerServiceMap.remove(message.getChatId());
        accountInfoService.handle(message);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.CURRENT_BALANCE.value;
    }
}