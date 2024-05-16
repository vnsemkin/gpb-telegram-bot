package org.vnsemkin.semkintelegrambot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
    void handle(Update update);
}
