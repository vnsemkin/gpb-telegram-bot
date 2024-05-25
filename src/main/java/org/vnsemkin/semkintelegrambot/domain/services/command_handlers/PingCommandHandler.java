package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.domain.constants.BotCommand;
import org.vnsemkin.semkintelegrambot.domain.services.common.Sender;

@Slf4j
@Service
@RequiredArgsConstructor
public final class PingCommandHandler implements CommandHandler {
    private final Sender sender;
    private static final String PONG_ANSWER = "pong";

    @Override
    public void handle(long chatId) {
        sender.send(sender.getSendMessage(chatId, PONG_ANSWER));
    }

    @Override
    public String getCommand() {
        return BotCommand.PING.command;
    }
}