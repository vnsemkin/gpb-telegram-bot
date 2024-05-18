package org.vnsemkin.semkintelegrambot.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.vnsemkin.semkintelegrambot.service.UpdateHandlerService;

@Component
public final class TelegramBot extends TelegramLongPollingBot {
    @Value("${telegram.gpb-bot.name}")
    private String botName;

    private final UpdateHandlerService updateHandlerService;

    public TelegramBot(@Value("${telegram.gpb-bot.token}") String token,
                       UpdateHandlerService updateHandlerService) {
        super(token);
        this.updateHandlerService = updateHandlerService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateHandlerService.handle(update);
    }
    @Override
    public String getBotUsername() {
        return botName;
    }
}
