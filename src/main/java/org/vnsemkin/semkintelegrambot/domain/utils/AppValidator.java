package org.vnsemkin.semkintelegrambot.domain.utils;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.domain.models.Result;

@Service
public final class AppValidator {
    private static final String NOT_VALID_NAME = "Неправильное имя";
    private static final String NOT_VALID_EMAIL = "Неправильный формат email";
    private static final String NOT_VALID_PASSWORD = "Неправильный формат пароля";

    public Result<Boolean, String> validateName(@NonNull String name) {
        if (name.matches("^[a-zA-Zа-яА-Я]+$") && name.length() <= 10) {
            return Result.success(true);
        }
        return Result.error(NOT_VALID_NAME);
    }

    public Result<Boolean, String> validateEmail(@NonNull String email) {
        if (email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$") && email.length() <= 50) {
            return Result.success(true);
        }
        return Result.error(NOT_VALID_EMAIL);
    }

    public Result<Boolean, String> validatePassword(@NonNull String password) {
        if (password.length() >= 5 && password.length() <= 10) {
            return Result.success(true);
        }
        return Result.error(NOT_VALID_PASSWORD);
    }
}