package org.vnsemkin.semkintelegrambot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.vnsemkin.semkintelegrambot.domain.services.command_handlers.CommandHandler;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.MessageHandler;
import org.vnsemkin.semkintelegrambot.domain.services.update_handlers.UpdateMessageHandler;
import org.vnsemkin.semkintelegrambot.presentation.tg_client.TgInterfaceImp;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class UpdateMessageHandlerTest {
    private static final String VALID_COMMAND = "/start";
    private static final String INVALID_COMMAND = "/invalid";
    private static final String VALID_TEXT = "Hello";
    private static final long CHAT_ID = 12345L;

    @Mock
    private Map<String, CommandHandler> commandHandlers;

    @Mock
    private Map<String, MessageHandler> messageHandlers;

    @Mock
    private Map<Long, String> messageIdToServiceMap;

    @Mock
    private TgInterfaceImp tgSenderImp;

    @InjectMocks
    private UpdateMessageHandler updateMessageHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        updateMessageHandler = new UpdateMessageHandler(commandHandlers,
            messageHandlers, messageIdToServiceMap, tgSenderImp);
    }

    @Test
    public void testHandle_updateWithoutMessage() {
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(null);

        updateMessageHandler.handle(update);

        verifyNoInteractions(tgSenderImp);
    }

    @Test
    public void testHandle_commandMessage() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(VALID_COMMAND);
        when(message.getChatId()).thenReturn(CHAT_ID);

        CommandHandler commandHandler = mock(CommandHandler.class);
        when(commandHandlers.get("start")).thenReturn(commandHandler);

        updateMessageHandler.handle(update);

        verify(commandHandler).handle(CHAT_ID);
        verifyNoMoreInteractions(tgSenderImp);
    }

    @Test
    public void testHandle_invalidCommandMessage() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(INVALID_COMMAND);
        when(message.getChatId()).thenReturn(CHAT_ID);

        when(commandHandlers.get("invalid")).thenReturn(null);

        updateMessageHandler.handle(update);

        verify(tgSenderImp).sendText(CHAT_ID, "Нет же такой команды");
    }

    @Test
    public void testHandle_replyMessageWithService() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(VALID_TEXT);
        when(message.getChatId()).thenReturn(CHAT_ID);

        when(messageIdToServiceMap.get(CHAT_ID)).thenReturn("someService");
        MessageHandler messageHandler = mock(MessageHandler.class);
        when(messageHandlers.get("someService")).thenReturn(messageHandler);

        updateMessageHandler.handle(update);

        verify(messageHandler).handle(message);
        verifyNoMoreInteractions(tgSenderImp);
    }

    @Test
    public void testHandle_replyMessageWithoutService() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(VALID_TEXT);
        when(message.getChatId()).thenReturn(CHAT_ID);

        when(messageIdToServiceMap.get(CHAT_ID)).thenReturn(null);

        updateMessageHandler.handle(update);

        verify(tgSenderImp).sendText(anyLong(), contains("Не понял запрос. Введите команду:"));
    }
}