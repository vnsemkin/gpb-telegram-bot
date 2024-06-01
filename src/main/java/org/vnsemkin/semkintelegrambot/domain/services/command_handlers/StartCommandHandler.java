package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.application.externals.TgInterface;
import org.vnsemkin.semkintelegrambot.domain.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.presentation.tg_client.TgInterfaceImp;

@Slf4j
@Service
public final class StartCommandHandler implements CommandHandler {
    private final TgInterface tgInterface;

    public StartCommandHandler(TgInterfaceImp tgSenderImp) {
        this.tgInterface = tgSenderImp;
    }

    @Override
    public void handle(long chatId) {
        tgInterface.sendText(chatId,
            CommandToServiceMap.START.command);
    }

    @Override
    public String getCommand() {
        return CommandToServiceMap.START.command;
    }

    @Override
    public String getServiceName() {
        return "";
    }
}
