package org.vnsemkin.semkintelegrambot.domain.services.update_handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
@Service
@RequiredArgsConstructor
public final class UpdateCallbackQueryHandler implements UpdateHandler {
    private static final String UNDERSCORE = "_";

    @Override
    public void handle(Update update) {
        if (update.getCallbackQuery() == null) {
            return;
        }
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String service = callbackQuery.getData().split(UNDERSCORE)[0];
    }
}