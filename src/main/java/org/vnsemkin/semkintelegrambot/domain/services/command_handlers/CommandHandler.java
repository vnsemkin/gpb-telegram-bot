package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.NonNull;

public interface CommandHandler {
    void handle(long chatId);
    String getCommand();
}
