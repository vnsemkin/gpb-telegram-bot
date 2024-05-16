package org.vnsemkin.semkintelegrambot.service;


import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.vnsemkin.semkintelegrambot.constant.BotConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public final class UpdateMessageHandler implements UpdateHandler {
    private final Sender sender;
    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();

    public UpdateMessageHandler(Sender sender, List<CommandHandler> commandHandlerList) {
        this.sender = sender;
        commandHandlerList.forEach(handler -> commandHandlers.put(handler.getCommand(), handler));
    }

    @Override
    public void handle(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            if (message.hasText()) {
                checkOnCommand(chatId, message.getText());
                return;
            }
            handleReply(chatId, BotConstant.NOT_IMPLEMENTED);
        }
    }

    private void checkOnCommand(long chatId, @NotNull String text) {
        if (text.startsWith(BotConstant.COMMAND_DELIMITER)) {
            handleCommand(chatId, text);
            return;
        }
        handleReply(chatId, text);
    }

    private void handleCommand(long chatId, String text) {
        String command = text.substring(BotConstant.COMMAND_DELIMITER.length());
        CommandHandler commandHandler = commandHandlers.get(command);
        if (commandHandler != null) {
            commandHandler.handle(chatId, command);
        } else {
            defaultCommandHandler(chatId);
        }
    }

    private void defaultCommandHandler(long chatId) {
        sender.send(sender.getSendMessage(chatId, BotConstant.NOT_IMPLEMENTED));
    }

    private void handleReply(long chatId, String text) {
        sender.send(sender.getSendMessage(chatId, text));
    }
}
