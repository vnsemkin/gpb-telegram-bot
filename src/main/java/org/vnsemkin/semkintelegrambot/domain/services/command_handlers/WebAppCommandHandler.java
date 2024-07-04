package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.web_app.WebAppService;

@Service
@RequiredArgsConstructor
public class WebAppCommandHandler implements CommandHandler {
    private final WebAppService webAppService;
    @Override
    public void handle(@NonNull Message message) {
        webAppService.handle(message);
    }

    @Override
    public String getHandlerName() {
       return CommandToServiceMap.WEBAPP.value;
    }
}
