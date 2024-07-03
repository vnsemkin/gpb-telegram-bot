package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.customer.CustomerRegistrationService;

import java.util.Map;


@Service
@RequiredArgsConstructor
public final class RegisterCommandHandler implements CommandHandler {
    private final CustomerRegistrationService customerRegistrationService;
    private final Map<Long, String> messageHandlerServiceMap;

    @Override
    public void handle(@NonNull Message message) {
        messageHandlerServiceMap.remove(message.getChatId());
        customerRegistrationService.startRegistration(message);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.REGISTER.value;
    }
}