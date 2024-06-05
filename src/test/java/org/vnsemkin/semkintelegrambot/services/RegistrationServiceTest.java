package org.vnsemkin.semkintelegrambot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.registration.RegistrationService;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.registration.UserStateHandler;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegistrationServiceTest {
    private static final long CHAT_ID = 12345L;
    @Mock
    private TgSenderInterface tgSenderInterface;
    @Mock
    private UserStateHandler userStateHandler;
    @Mock
    private Map<Long, String> messageIdToServiceMap;
    private RegistrationService registrationService;
    @Captor
    private ArgumentCaptor<SendMessage> sendMessageCaptor;
    @Captor
    private ArgumentCaptor<Message> messageCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.registrationService = new RegistrationService(messageIdToServiceMap,
            tgSenderInterface,
            userStateHandler);
    }

    @Test
    public void testStartPickUpInformation() {
        // ARRANGE
        Message mockMessage = mock(Message.class);
        User user = mock(User.class);
        when(mockMessage.getFrom()).thenReturn(user);
        when(mockMessage.getChatId()).thenReturn(CHAT_ID);
        when(tgSenderInterface.sendSendMessage(any(SendMessage.class)))
            .thenReturn(Result.success(mockMessage));
        // ACT
        registrationService.startPickUpInformation(mockMessage);

        verify(tgSenderInterface).sendSendMessage(sendMessageCaptor.capture());
        SendMessage capturedSendMessage = sendMessageCaptor.getValue();
        // ASSERT
        assertNotNull(capturedSendMessage);
        assertEquals(Long.toString(CHAT_ID), capturedSendMessage.getChatId());
        assertTrue(capturedSendMessage.getText().contains("Добро пожаловать в Мини-Банк."));
        verify(messageIdToServiceMap).put(eq(CHAT_ID), eq(CommandToServiceMap.REGISTER.value));
    }

    @Test
    public void testHandle() {
        // ARRANGE
        Message mockMessage = mock(Message.class);
        User user = mock(User.class);
        when(mockMessage.getFrom()).thenReturn(user);
        when(mockMessage.getChatId()).thenReturn(CHAT_ID);
        // ACT
        registrationService.handle(mockMessage);
        // ASSERT
        verify(userStateHandler).handleUserRegistrationState(messageCaptor.capture(), anyMap());
        Message capturedMessage = messageCaptor.getValue();

        assertNotNull(capturedMessage);
        assertEquals(mockMessage.getChatId(), capturedMessage.getChatId());
        assertEquals(mockMessage.getFrom(), capturedMessage.getFrom());
    }

    @Test
    public void testGetHandlerName() {
        String serviceName = registrationService.getHandlerName();
        assertEquals(CommandToServiceMap.REGISTER.value, serviceName);
    }
}