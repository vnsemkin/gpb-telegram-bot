package org.vnsemkin.semkintelegrambot.component;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.vnsemkin.semkintelegrambot.config.BotConfig;
import org.vnsemkin.semkintelegrambot.service.UpdateHandlerService;

@Component
public final class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final UpdateHandlerService updateHandlerService;

    public TelegramBot(BotConfig botConfig,
                       UpdateHandlerService updateHandlerService) {
        super(botConfig.getToken());
        this.updateHandlerService = updateHandlerService;
        this.botConfig = botConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateHandlerService.handle(update);
    }

    @Override
    public String getBotUsername() {
        return this.botConfig.getBotName();
    }
}
