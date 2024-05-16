package org.vnsemkin.semkintelegrambot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.constant.BotCommand;
import org.vnsemkin.semkintelegrambot.constant.BotConstant;

@Slf4j
@Service
@RequiredArgsConstructor
public class PingCommandHandler implements CommandHandler {
    private final Sender sender;

    @Override
    public void handle(long chatId, String command) {
        sender.send(sender.getSendMessage(chatId, BotConstant.PONG_ANSWER));
    }

    @Override
    public String getCommand() {
        return BotCommand.PING.name().toLowerCase();
    }
}
