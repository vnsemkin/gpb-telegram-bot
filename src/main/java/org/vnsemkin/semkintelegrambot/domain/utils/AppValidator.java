package org.vnsemkin.semkintelegrambot.domain.utils;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.domain.models.Result;

import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.INT_FIFTY;
import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.INT_FIVE;
import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.INT_TEN;

@Service
public final class AppValidator {
    private static final String NOT_VALID_NAME = "Неправильное имя";
    private static final String NOT_VALID_EMAIL = "Неправильный формат email";
    private static final String NOT_VALID_PASSWORD = "Неправильный формат пароля";
    private static final String NAME_REGEX_VALIDATION = "^[a-zA-Zа-яА-Я]+$";
    private static final String EMAIL_REGEX_VALIDATION = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    public Result<Boolean, String> validateName(@NonNull String name) {
        if (name.matches(NAME_REGEX_VALIDATION) && name.length() <= INT_TEN) {
            return Result.success(true);
        }
        return Result.error(NOT_VALID_NAME);
    }

    public Result<Boolean, String> validateEmail(@NonNull String email) {
        if (email.matches(EMAIL_REGEX_VALIDATION) &&
            email.length() <= INT_FIFTY) {
            return Result.success(true);
        }
        return Result.error(NOT_VALID_EMAIL);
    }

    public Result<Boolean, String> validatePassword(@NonNull String password) {
        if (password.length() >= INT_FIVE && password.length() <= INT_TEN) {
            return Result.success(true);
        }
        return Result.error(NOT_VALID_PASSWORD);
    }
}