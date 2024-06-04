package org.vnsemkin.semkintelegrambot.application.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vnsemkin.semkintelegrambot.domain.services.command_handlers.CommandHandler;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.MessageHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AppConfig {
    @Bean
    public Map<Long, String> messageIdToServiceMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    @Qualifier("commandHandlers")
    public Map<String, CommandHandler> commandHandlers(List<CommandHandler> commandHandlerList) {
        Map<String, CommandHandler> commandHandlers = new HashMap<>();
        for (CommandHandler handler : commandHandlerList) {
            commandHandlers.put(handler.getHandlerName(), handler);
        }
        return commandHandlers;
    }

    @Bean
    @Qualifier("messageHandlers")
    public Map<String, MessageHandler> messageHandlers(List<MessageHandler> messageHandlerList) {
        Map<String, MessageHandler> messageHandlers = new HashMap<>();
        for (MessageHandler handler : messageHandlerList) {
            messageHandlers.put(handler.getHandlerName(), handler);
        }
        return messageHandlers;
    }
}