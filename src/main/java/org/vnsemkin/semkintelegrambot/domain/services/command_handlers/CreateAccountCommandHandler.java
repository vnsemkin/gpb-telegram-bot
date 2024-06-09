package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.account.AccountRegistrationService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreateAccountCommandHandler implements CommandHandler {
    private final AccountRegistrationService accountRegistrationService;
    private final Map<Long, String> messageHandlerServiceMap;

    @Override
    public void handle(Message message) {
        messageHandlerServiceMap.remove(message.getChatId());
        accountRegistrationService.handle(message);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.CREATE_ACCOUNT.value;
    }
}
