package org.vnsemkin.semkintelegrambot.domain.utils;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.vnsemkin.semkintelegrambot.domain.services.command_handlers.CommandHandler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class InlineKeyboard {
    private final static String UNDERSCORE = "_";

    public static InlineKeyboardMarkup getInlineKeyboard(@NonNull Set<String> buttons,
                                                         CommandHandler command) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(
            buttons.stream()
                .map(text -> {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(text);
                    button.setCallbackData(command.getServiceName() + UNDERSCORE + text);
                    return button;
                })
                .map(List::of)
                .collect(Collectors.toList())
        );
        return inlineKeyboardMarkup;
    }
}
