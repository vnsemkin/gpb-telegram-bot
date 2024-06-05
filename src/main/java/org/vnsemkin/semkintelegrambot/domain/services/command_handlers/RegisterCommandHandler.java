package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.registration.RegistrationService;


@Service
@RequiredArgsConstructor
public final class RegisterCommandHandler implements CommandHandler {
    private final RegistrationService registrationService;

    @Override
    public void handle(Message message) {
        registrationService.startPickUpInformation(message);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.REGISTER.value;
    }
}