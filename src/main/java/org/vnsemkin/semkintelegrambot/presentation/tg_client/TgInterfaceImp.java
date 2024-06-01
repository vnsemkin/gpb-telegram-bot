package org.vnsemkin.semkintelegrambot.presentation.tg_client;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.vnsemkin.semkintelegrambot.application.externals.TgInterface;
import org.vnsemkin.semkintelegrambot.domain.models.Result;


@Slf4j
@Service
public final class TgInterfaceImp extends DefaultAbsSender implements TgInterface {
    private static final String HTML_MARKUP = "HTML";
    private static final String NOT_IMPLEMENTED = "Функция в разработке";

    public TgInterfaceImp(@Value("${telegram.gpb-bot.token}") String token) {
        super(new DefaultBotOptions(), token);
    }

    public Result<Message> sendText
        (long chatId, @NonNull String text) {
        SendMessage sendMessage =
            new SendMessage(Long.toString(chatId), text);
        return send(sendMessage);
    }

    public Result<Message> sendSendMessage(@NonNull SendMessage sm) {
        return send(sm);
    }

    private Result<Message> send(@NonNull Object obj) {
        if (obj instanceof SendMessage sendMessage) {
            sendMessage.setParseMode(HTML_MARKUP);
            try {
                return Result.success(execute(sendMessage));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
                return Result.failure(e);
            }
        } else {
            log.info(NOT_IMPLEMENTED);
            return Result.success(messageNotImplemented());
        }
    }

    private Message messageNotImplemented() {
        Message message = new Message();
        message.setText(NOT_IMPLEMENTED);
        return message;
    }
}