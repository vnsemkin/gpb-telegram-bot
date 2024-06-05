package org.vnsemkin.semkintelegrambot.domain.utils;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.domain.models.Result;

import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.MAX_EMAIL_LENGTH;
import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.MAX_NAME_LENGTH;
import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.MAX_PASSWORD_LENGTH;
import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.MIN_PASSWORD_LENGTH;

@Service
public final class AppValidator {
    private static final String NOT_VALID_NAME = "Неправильное имя";
    private static final String NOT_VALID_EMAIL = "Неправильный формат email";
    private static final String NOT_VALID_PASSWORD = "Неправильный формат пароля";
    private static final String NAME_REGEX_VALIDATION = "^[a-zA-Zа-яА-Я]+$";
    private static final String EMAIL_REGEX_VALIDATION = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    public Result<Boolean, String> validateName(@NonNull String name) {
        if (name.matches(NAME_REGEX_VALIDATION) &&
            name.length() <= MAX_NAME_LENGTH) {
            return Result.success(true);
        }
        return Result.error(NOT_VALID_NAME);
    }

    public Result<Boolean, String> validateEmail(@NonNull String email) {
        if (email.matches(EMAIL_REGEX_VALIDATION) &&
            email.length() <= MAX_EMAIL_LENGTH) {
            return Result.success(true);
        }
        return Result.error(NOT_VALID_EMAIL);
    }

    public Result<Boolean, String> validatePassword(@NonNull String password) {
        if (password.length() >= MIN_PASSWORD_LENGTH &&
            password.length() <= MAX_PASSWORD_LENGTH) {
            return Result.success(true);
        }
        return Result.error(NOT_VALID_PASSWORD);
    }
}