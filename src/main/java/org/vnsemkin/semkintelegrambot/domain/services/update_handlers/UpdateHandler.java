package org.vnsemkin.semkintelegrambot.domain.services.update_handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
    void handle(Update update);
}
