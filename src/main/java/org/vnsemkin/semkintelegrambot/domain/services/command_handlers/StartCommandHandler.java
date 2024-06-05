package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.presentation.tg_client.TgSenderInterfaceImp;

@Slf4j
@Service
public final class StartCommandHandler implements CommandHandler {
    private final TgSenderInterface tgSenderInterface;

    public StartCommandHandler(TgSenderInterfaceImp tgSenderImp) {
        this.tgSenderInterface = tgSenderImp;
    }

    @Override
    public void handle(Message message) {
        tgSenderInterface.sendText(message.getChatId(),
            CommandToServiceMap.START.value);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.START.value;
    }
}