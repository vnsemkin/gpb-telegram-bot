package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.presentation.tg_client.TgSenderInterfaceImp;

@Slf4j
@Service
public final class PingCommandHandler implements CommandHandler {
    private final TgSenderInterface tgSenderInterface;
    private static final String PONG_ANSWER = "pong";

    public PingCommandHandler(TgSenderInterfaceImp tgSenderImp) {
        this.tgSenderInterface = tgSenderImp;
    }

    @Override
    public void handle(long chatId) {
        tgSenderInterface.sendText(chatId, PONG_ANSWER);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.PING.value;
    }
}