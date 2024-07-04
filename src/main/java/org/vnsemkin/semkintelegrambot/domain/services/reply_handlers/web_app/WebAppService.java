package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.web_app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.MessageHandler;
import org.vnsemkin.semkintelegrambot.domain.utils.ReplyKeyboard;

@Service
@RequiredArgsConstructor
public class WebAppService implements MessageHandler {
    private final TgSenderInterface sender;

    @Override
    public void handle(Message message) {
        Long chatId = message.getChatId();
        String text = "Запуск web_app";
        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboard.replyKeyboardMarkup();
        SendMessage sendMessage = new SendMessage(chatId.toString(), text);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sender.sendSendMessage(sendMessage);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.WEBAPP.value;
    }
}
