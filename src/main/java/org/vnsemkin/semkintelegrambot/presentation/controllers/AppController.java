package org.vnsemkin.semkintelegrambot.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.vnsemkin.semkintelegrambot.domain.components.TelegramBot;


@RestController
@RequiredArgsConstructor
public class AppController {
    private final TelegramBot telegramBot;

    @PostMapping("/webhook")
    public void onUpdateReceived(@RequestBody Update update) {
        telegramBot.onUpdateReceived(update);
        System.out.println("onUpdateReceived");
    }
}

