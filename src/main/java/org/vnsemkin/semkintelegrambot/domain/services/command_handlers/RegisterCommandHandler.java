package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.domain.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.registration.RegistrationService;


@Service
@RequiredArgsConstructor
public final class RegisterCommandHandler implements CommandHandler {
    private final RegistrationService registrationService;

    @Override
    public void handle(long chatId) {
        registrationService.startPickUpInformation(chatId);
    }

    @Override
    public String getCommand() {
        return CommandToServiceMap.REGISTER.command;
    }

    @Override
    public String getServiceName() {
        return CommandToServiceMap.REGISTER.service;
    }
}