package org.vnsemkin.semkintelegrambot.config;

import java.math.BigDecimal;

public class TestConstants {
    public static final String INVALID_USERNAME = "test!";
    public static final String VALID_USERNAME = "test";
    public static final String INVALID_AMOUNT = "500asas";
    public static final String INVALID_DATA = "Invalid data";
    public static final String INVALID_TRANSFER_REQUEST = "Неправильный формат!";
    public static final String INVALID_TRANSFER_USERNAME = "test! 500";
    public static final String INVALID_TRANSFER_AMOUNT = "test 500asas";
    public static final String DEFAULT_ACCOUNT_NAME = "Акционный";
    public static final String USER_OR_ACCOUNT_NOT_EXIST = "Не найдет пользователь или аккаунт!";
    public static final String INPUT_EMAIL = "Введите email";
    public static final String NEW_LINE = "\n";
    public static final String EMPTY_LINE = "";
    public static final String CUSTOMER_ACCOUNT_MESSAGE = "Счет пользователя: ";
    public static final String ACCOUNT_BALANCE_MESSAGE = "Баланс счета: ";
    public static final String ACCOUNT_NO_INFO = "Нет информации по счету";
    public static final long CHAT_ID = 123L;
    public static final long USER_ID = 456L;
    public static final String UUID = "8dd4b7af-f765-4b5a-80da-7913ff678a07";
    public static final String ACCOUNT_NAME = "Test";
    public static final String EMAIL = "test@test.ru";
    public final static String ACCOUNT_CREATED = "Счет успешно открыт ";
    public final static String SMT_WRONG = "Что-то пошло не так. Попробуйте позднее.";
    public static final BigDecimal BIG_DECIMAL_ACCOUNT_BALANCE = BigDecimal.valueOf(100);
    public static final String CUSTOMER_NOT_FOUND = "Пользователь не найден";
    public static final String CUSTOMER_PREFIX = "Пользователь: ";
    public static final String CUSTOMER_ALREADY_REGISTER = " уже зарегистрирован !";
    public static final String EMAIL_PREFIX = "Email: ";
    public static final String USERNAME_PREFIX = "Username: ";
    public static final String ACCOUNT_PREFIX = "Счет: ";
    public static final String ACCOUNT_NOT_OPEN = "Счет не открыт ! ";
    public static final String DELIMITER_LINE = "-------------------";
    public static final String FIRSTNAME = "Ivan";
    public static final String USERNAME = "Ivanov";
    public static final String ARROW_EMOJI = "⬇";
    public static final String BOLD_START_TAG = "<b>";
    public static final String BOLD_STOP_TAG = "</b>";
    public static final String REGISTER_INFO = """
        Привет %s
        Добро пожаловать в Мини-Банк.
        Для регистрации пожалуйста !
        """;
    public static final String TRANSFER_REQUEST = "Введите запрос на перевод в формате :" + NEW_LINE +
        "[TelegramUsername] [amount]" + NEW_LINE +
        "Пример: " + NEW_LINE +
        "ivanov 500";
}
