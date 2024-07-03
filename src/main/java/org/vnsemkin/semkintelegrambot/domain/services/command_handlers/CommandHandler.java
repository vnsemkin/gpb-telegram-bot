package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import org.springframework.lang.NonNull;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandHandler {
    void handle(@NonNull Message message);
    String getHandlerName();
}