package org.vnsemkin.semkintelegrambot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.constant.BotCommand;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandler {
    private final Sender sender;

    @Override
    public void handle(long chatId, String command) {
        sender.send(sender.getSendMessage(chatId, BotCommand.START.name().toLowerCase()));
    }

    @Override
    public String getCommand() {
        return BotCommand.START.name().toLowerCase();
    }
}
