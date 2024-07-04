package org.vnsemkin.semkintelegrambot.domain.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboard {
    private final static String WELCOME_MESSAGE = "Для получения главного меню нажмите на кнопку 😊";


    public static ReplyKeyboardMarkup replyKeyboardMarkup() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows());
        return replyKeyboardMarkup;
    }

    private static List<KeyboardRow> keyboardRows() {
        // Set webApp
        WebAppInfo wi = new WebAppInfo();
        wi.setUrl("https://vnsemkin.github.io/gpb-telegram-bot/");
        // Create button
        KeyboardButton button = new KeyboardButton();
        button.setWebApp(wi);
        button.setText(WELCOME_MESSAGE);
        // Create buttonRow
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(button);
        // Create List of KeyboardRow
        final List<KeyboardRow> keyboardRowsList = new ArrayList<>();
        keyboardRowsList.add(keyboardRow);
        return keyboardRowsList;
    }
}
