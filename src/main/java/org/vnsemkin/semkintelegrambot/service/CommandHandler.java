package org.vnsemkin.semkintelegrambot.service;

public interface CommandHandler {
    void handle(long chatId, String command);
    String getCommand();
}
