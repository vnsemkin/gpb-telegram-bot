package org.vnsemkin.semkintelegrambot.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.constant.BotCommand;

@Slf4j
@Service
@RequiredArgsConstructor
public final class StartCommandHandler implements CommandHandler {
    private final Sender sender;

    @Override
    public void handle(long chatId, @NonNull String command) {
        sender.send(sender.getSendMessage(chatId, BotCommand.START.command));
    }

    @Override
    public String getCommand() {
        return BotCommand.START.command;
    }
}
