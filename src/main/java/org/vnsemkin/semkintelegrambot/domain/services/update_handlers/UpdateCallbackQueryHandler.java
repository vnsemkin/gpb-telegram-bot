package org.vnsemkin.semkintelegrambot.domain.services.update_handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
public final class UpdateCallbackQueryHandler implements UpdateHandler {
    @Override
    public void handle(Update update) {
        if (update.getCallbackQuery() == null) {
            return;
        }
        CallbackQuery callbackQuery = update.getCallbackQuery();
        log.info("Got callback query: {}", callbackQuery);
    }
}

