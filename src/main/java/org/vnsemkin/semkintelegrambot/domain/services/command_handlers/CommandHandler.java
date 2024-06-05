package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandHandler {
    void handle(Message message);
    String getHandlerName();
}