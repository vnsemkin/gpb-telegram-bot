package org.vnsemkin.semkintelegrambot.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
public class TelegramBotRunner {
    private final TelegramBot bot;

    @EventListener(ApplicationReadyEvent.class)
    public void run() throws TelegramApiException {
        new TelegramBotsApi(DefaultBotSession.class)
            .registerBot(bot);
    }
}

