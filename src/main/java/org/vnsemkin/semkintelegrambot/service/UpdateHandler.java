package org.vnsemkin.semkintelegrambot.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateHandler {
    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;

    public void handle(Update update) {
        if (update != null) {
            if (update.hasMessage()) {
                messageHandler.handler(update.getMessage());
            } else if (update.hasCallbackQuery()) {
                callbackQueryHandler.handle(update.getCallbackQuery());
            }
        }else {
            log.error("Update is null");
        }
    }
}
