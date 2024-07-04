package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.web_app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.MessageHandler;
import org.vnsemkin.semkintelegrambot.domain.utils.ReplyKeyboard;

@Service
@RequiredArgsConstructor
public class WebAppService implements MessageHandler {
    private final TgSenderInterface sender;
    private final static String WELCOME_MESSAGE ="<b>Добро пожаловать в Мини-банк!</b> \n" +
        "Для получения главного меню нажмите на кнопку ниже. 😊";
    private static final String HTML_MARKUP = "HTML";

    @Override
    public void handle(Message message) {
        SendMessage sendMessage =
            new SendMessage(message.getChatId().toString(), WELCOME_MESSAGE);
        sendMessage.setReplyMarkup(ReplyKeyboard.replyKeyboardMarkup());
        sendMessage.setParseMode(HTML_MARKUP);
        sender.sendSendMessage(sendMessage);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.WEBAPP.value;
    }
}
