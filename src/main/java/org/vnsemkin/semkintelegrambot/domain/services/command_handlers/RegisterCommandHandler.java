package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.domain.constants.BotCommand;
import org.vnsemkin.semkintelegrambot.domain.services.common.Sender;

@Service
@RequiredArgsConstructor
public class RegisterCommandHandler implements CommandHandler {
    private final Sender sender;

    @Override
    public void handle(long chatId) {

    }

    @Override
    public String getCommand() {
        return BotCommand.REGISTER.command;
    }
}
