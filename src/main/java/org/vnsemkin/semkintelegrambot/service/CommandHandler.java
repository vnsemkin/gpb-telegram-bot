package org.vnsemkin.semkintelegrambot.service;

import lombok.NonNull;

public interface CommandHandler {
    void handle(long chatId, @NonNull String command);
    String getCommand();
}
