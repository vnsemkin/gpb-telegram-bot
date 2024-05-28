package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.domain.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.domain.services.senders.TgApiSender;

@Slf4j
@Service
@RequiredArgsConstructor
public final class PingCommandHandler implements CommandHandler {
    private final TgApiSender tgApiSender;
    private static final String PONG_ANSWER = "pong";

    @Override
    public void handle(long chatId) {
        tgApiSender.sendText(chatId, PONG_ANSWER);
    }

    @Override
    public String getCommand() {
        return CommandToServiceMap.PING.command;
    }

    @Override
    public String getServiceName() {
        return "";
    }
}
