package org.vnsemkin.semkintelegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Slf4j
@Service
public class CallbackQueryHandler {
    public void handle(CallbackQuery callbackQuery) {
        log.info("Got callback query: {}", callbackQuery);
    }
}
