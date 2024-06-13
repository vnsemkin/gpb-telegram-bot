package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.transfer.TransferMoneyService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferCommandHandler implements CommandHandler {
    private final Map<Long, String> messageHandlerServiceMap;
    private final TransferMoneyService transferMoneyService;

    @Override
    public void handle(Message message) {
        messageHandlerServiceMap.remove(message.getChatId());
        transferMoneyService.startTransferMoneyProcess(message);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.TRANSFER.value;
    }
}