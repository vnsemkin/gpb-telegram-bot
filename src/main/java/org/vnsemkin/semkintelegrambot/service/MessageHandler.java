package org.vnsemkin.semkintelegrambot.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.constant.BotConstant;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageHandler {
    private final Sender sender;

    public void handler(Message message) {
        Long chatId = message.getChatId();
        if (message.hasText()) {
            String text = message.getText();
            if (text.startsWith(BotConstant.COMMAND_DELIMITER)) {
                commandHandler(chatId, text);
            } else {
                replyHandler(chatId, text);
            }
        }
    }

    private void commandHandler(long chatId, String command) {
        switch (command) {
            case BotConstant.START_COMMAND -> sender.send(sender.getSendMessage(chatId, BotConstant.START_COMMAND));
            case BotConstant.PING_COMMAND -> sender.send(sender.getSendMessage(chatId, BotConstant.PONG_ANSWER));
            default -> sender.send(sender.getSendMessage(chatId, BotConstant.NOT_IMPLEMENTED));
        }
    }

    private void replyHandler(long chatId, String text) {
        sender.send(sender.getSendMessage(chatId, BotConstant.NOT_IMPLEMENTED));
    }
}
