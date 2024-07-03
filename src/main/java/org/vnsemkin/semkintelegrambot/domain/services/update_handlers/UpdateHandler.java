package org.vnsemkin.semkintelegrambot.domain.services.update_handlers;

import org.springframework.lang.NonNull;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
    void handleUpdate(@NonNull Update update);
}