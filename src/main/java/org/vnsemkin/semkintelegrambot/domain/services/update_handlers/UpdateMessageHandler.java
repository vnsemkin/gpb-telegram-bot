package org.vnsemkin.semkintelegrambot.domain.services.update_handlers;


import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.vnsemkin.semkintelegrambot.domain.services.common.Sender;
import org.vnsemkin.semkintelegrambot.domain.services.command_handlers.CommandHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public final class UpdateMessageHandler implements UpdateHandler {
    private static final String COMMAND_DELIMITER = "/";
    private static final String NOT_IMPLEMENTED = "Функция в разработке";
    private final Sender sender;
    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();

    public UpdateMessageHandler(Sender sender,final List<CommandHandler> commandHandlerList) {
        this.sender = sender;
        commandHandlerList.forEach(handler -> commandHandlers.put(handler.getCommand(), handler));
    }

    @Override
    public void handle(@NonNull Update update) {
        if (update.getMessage() == null) {
            return;
        }
        final Message message = update.getMessage();
        long chatId = message.getChatId();
        if (message.hasText()) {
            checkIfCommandElseReply(chatId, message.getText());
            return;
        }
        handleReply(chatId, NOT_IMPLEMENTED);
    }

    private void checkIfCommandElseReply(long chatId, @NotNull String text) {
        if (text.startsWith(COMMAND_DELIMITER)) {
            handleCommand(chatId, text);
            return;
        }
        handleReply(chatId, text);
    }

    private void handleCommand(long chatId, @NonNull String text) {
        String command = text.substring(COMMAND_DELIMITER.length());
        CommandHandler commandHandler = commandHandlers.get(command);
        if (commandHandler != null) {
            commandHandler.handle(chatId);
        } else {
            defaultCommandHandler(chatId);
        }
    }

    private void defaultCommandHandler(long chatId) {
        sender.send(sender.getSendMessage(chatId, NOT_IMPLEMENTED));
    }

    private void handleReply(long chatId, String text) {
        sender.send(sender.getSendMessage(chatId, text));
    }
}
