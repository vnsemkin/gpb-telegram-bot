package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.registration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.externals.TgInterface;
import org.vnsemkin.semkintelegrambot.domain.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.domain.models.Customer;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.MessageHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public final class RegistrationService implements MessageHandler {
    private static final String INPUT_NAME = "Введите имя";
    private static final String NEW_LINE = "\n";
    private static final String ARROW_EMOJI = "⬇";
    private static final String BOLD_START_TAG = "<b>";
    private static final String BOLD_STOP_TAG = "</b>";
    private static final String REGISTER_INFO = """
        Добро пожаловать в Мини-Банк.
        Для регистрации пожалуйста !
        """;
    private final Map<Long, Customer> customersOnRegistrationMap;
    private final Map<Long, String> messageIdToServiceMap;
    private final TgInterface tgInterface;

    private final UserStateHandler userStateHandler;

    public RegistrationService(Map<Long, String> messageIdToServiceMap,
                               TgInterface tgInterface,
                               UserStateHandler userStateHandler) {
        this.userStateHandler = userStateHandler;
        this.customersOnRegistrationMap = new ConcurrentHashMap<>();
        this.messageIdToServiceMap = messageIdToServiceMap;
        this.tgInterface = tgInterface;
    }


    public void startPickUpInformation(long chatId) {
        customersOnRegistrationMap.remove(chatId);
        Result<Message, String> messageResult = tgInterface
            .sendSendMessage(createWelcomeMessage(chatId));
        if (messageResult.isSuccess()) {
            messageResult.getData()
                .map(Message::getChatId)
                .ifPresent(id -> messageIdToServiceMap.put(id, getServiceName()));
        }
    }

    @Override
    public void handle(@NonNull Message message) {
        userStateHandler.handleUserRegistrationState(message, customersOnRegistrationMap);
    }

    private SendMessage createWelcomeMessage(long chatId) {
        String inputName = BOLD_START_TAG +
            REGISTER_INFO +
            BOLD_STOP_TAG +
            NEW_LINE +
            INPUT_NAME +
            ARROW_EMOJI;
        return new SendMessage(Long.toString(chatId), inputName);
    }

    @Override
    public String getServiceName() {
        return CommandToServiceMap.REGISTER.service;
    }
}