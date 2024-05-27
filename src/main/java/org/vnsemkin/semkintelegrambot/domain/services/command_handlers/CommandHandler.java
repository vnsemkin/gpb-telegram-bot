package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

public interface CommandHandler {
    void handle(long chatId);
    String getCommand();
    String getServiceName();
}
