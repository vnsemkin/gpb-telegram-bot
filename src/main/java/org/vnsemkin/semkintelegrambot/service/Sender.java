package org.vnsemkin.semkintelegrambot.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.vnsemkin.semkintelegrambot.config.BotConfig;
import org.vnsemkin.semkintelegrambot.constant.BotConstant;


@Slf4j
@Service
public final class Sender extends DefaultAbsSender {

    public Sender(BotConfig botConfig) {
        super(new DefaultBotOptions(), botConfig.getToken());
    }

    public SendMessage getSendMessage(long chatId, String text) {
        SendMessage sendMessage =
            new SendMessage(Long.toString(chatId), text);
        sendMessage.setParseMode(BotConstant.HTML_MARKUP);
        return sendMessage;
    }

    public void send(@NonNull Object obj) {
        if (obj instanceof SendMessage sendMessage) {
            try {
                this.execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            log.info(BotConstant.NOT_IMPLEMENTED);
        }
    }
}