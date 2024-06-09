package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;

import java.util.Map;

import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.NOT_IMPLEMENTED;

@Service
@RequiredArgsConstructor
public class CurrentBalanceCommandHandler implements CommandHandler{
    private final TgSenderInterface tgSenderInterface;
    private final Map<Long, String> messageHandlerServiceMap;

    @Override
    public void handle(Message message) {
        messageHandlerServiceMap.remove(message.getChatId());
        tgSenderInterface.sendText(message.getChatId(), NOT_IMPLEMENTED);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.CURRENT_BALANCE.value;
    }
}
