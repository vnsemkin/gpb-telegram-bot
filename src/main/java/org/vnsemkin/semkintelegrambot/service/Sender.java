package org.vnsemkin.semkintelegrambot.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.vnsemkin.semkintelegrambot.config.BotConfig;


@Slf4j
@Service
public final class Sender extends DefaultAbsSender {
    private static final String HTML_MARKUP = "HTML";
    private static final String NOT_IMPLEMENTED = "Функция в разработке";

    public Sender(BotConfig botConfig) {
        super(new DefaultBotOptions(), botConfig.getToken());
    }

    public SendMessage getSendMessage(long chatId, @NonNull String text) {
        SendMessage sendMessage =
            new SendMessage(Long.toString(chatId), text);
        sendMessage.setParseMode(HTML_MARKUP);
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
            log.info(NOT_IMPLEMENTED);
        }
    }
}