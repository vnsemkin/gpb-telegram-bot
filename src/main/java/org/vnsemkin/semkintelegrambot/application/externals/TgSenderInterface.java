package org.vnsemkin.semkintelegrambot.application.externals;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.domain.models.Result;

public interface TgSenderInterface {
    Result<Message, String> sendText(long chatId, @NonNull String text);
    Result<Message, String> sendSendMessage(@NonNull SendMessage sm);
}