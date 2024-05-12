package org.vnsemkin.semkintelegrambot.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.vnsemkin.semkintelegrambot.config.BotConfig;
import org.vnsemkin.semkintelegrambot.service.UpdateHandler;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private UpdateHandler updateHandler;
    private final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
    }


    @Override
    public void onUpdateReceived(Update update) {
        updateHandler.handle(update);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
}
