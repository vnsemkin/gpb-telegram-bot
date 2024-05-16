package org.vnsemkin.semkintelegrambot.service;


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
public class UpdateMessageHandler implements UpdateHandler {
    private final Sender sender;
    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();

    public UpdateMessageHandler(Sender sender, List<CommandHandler> commandHandlerList) {
        this.sender = sender;
        commandHandlerList.forEach(handler -> commandHandlers.put(handler.getCommand(), handler));
    }

    @Override
    public void handle(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                if (text.startsWith(BotConstant.COMMAND_DELIMITER)) {
                    handleCommand(chatId, text);
                } else {
                    handleReply(chatId, text);
                }
            } else {
                handleReply(chatId, BotConstant.NOT_IMPLEMENTED);
            }
        }
    }

    private void handleCommand(long chatId, String text) {
        String command = text.substring(BotConstant.COMMAND_DELIMITER.length());
        CommandHandler commandHandler = commandHandlers.get(command);
        if (commandHandler != null) {
            commandHandler.handle(chatId, command);
        } else {
            defaultCommandHandler(chatId, command);
        }
    }

    private void defaultCommandHandler(long chatId, String command) {
        sender.send(sender.getSendMessage(chatId, BotConstant.NOT_IMPLEMENTED));
    }

    private void handleReply(long chatId, String text) {
        sender.send(sender.getSendMessage(chatId, text));
    }
}
