package org.vnsemkin.semkintelegrambot.domain.services.update_handlers;


import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.vnsemkin.semkintelegrambot.domain.services.command_handlers.CommandHandler;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.MessageHandler;
import org.vnsemkin.semkintelegrambot.presentation.tg_client.TgInterfaceImp;

import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public final class UpdateMessageHandler implements UpdateHandler {
    private static final String COMMAND_DELIMITER = "/";
    private static final String NEW_LINE = "\n";
    private static final String NOT_IMPLEMENTED = "Не понял запрос. Введите команду:" + NEW_LINE;
    private static final String COMMAND_NOT_IMPLEMENTED = "Нет же такой команды";
    private final Map<String, CommandHandler> commandHandlers;
    private final Map<String, MessageHandler> messageHandlers;
    private final Map<Long, String> messageIdToServiceMap;
    private final TgInterfaceImp tgSenderImp;

    public UpdateMessageHandler(@Qualifier("commandHandlers") Map<String, CommandHandler> commandHandlers,
                                @Qualifier("messageHandlers") Map<String, MessageHandler> messageHandlers,
                                Map<Long, String> messageIdToServiceMap,
                                TgInterfaceImp tgSenderImp) {
        this.commandHandlers = commandHandlers;
        this.messageHandlers = messageHandlers;
        this.messageIdToServiceMap = messageIdToServiceMap;
        this.tgSenderImp = tgSenderImp;
    }


    @Override
    public void handle(@NonNull Update update) {
        if (update.getMessage() == null) {
            return;
        }
        final Message message = update.getMessage();
        if (message.hasText()) {
            handleMessage(message);
        }
    }

    private void handleMessage(@NotNull Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();
        if (text.startsWith(COMMAND_DELIMITER)) {
            handleCommand(chatId, text);
            return;
        }
        handleReply(message);
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

    private void handleReply(Message message) {
        String service = messageIdToServiceMap.get(message.getChatId());
        if (service == null) {
            defaultMessageHandler(message.getChatId());
        } else {
            messageHandlers.get(service).handle(message);
        }
    }

    private void defaultCommandHandler(long chatId) {
        tgSenderImp.sendText(chatId, COMMAND_NOT_IMPLEMENTED);
    }

    private void defaultMessageHandler(long chatId) {
        String commands = commandHandlers.keySet().stream()
            .map(key -> COMMAND_DELIMITER + key)
            .collect(Collectors.joining(NEW_LINE));
        tgSenderImp.sendText(chatId, NOT_IMPLEMENTED + commands);
    }
}