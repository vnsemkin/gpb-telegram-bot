package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageHandler {
    void handle(Message message);
    String getHandlerName();
}