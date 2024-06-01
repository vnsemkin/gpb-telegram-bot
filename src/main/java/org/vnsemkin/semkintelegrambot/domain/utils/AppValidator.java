package org.vnsemkin.semkintelegrambot.domain.utils;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.domain.models.Result;

@Service
public final class AppValidator {
    private static final String NOT_VALID_NAME = "Неправильное имя";
    private static final String NOT_VALID_EMAIL = "Неправильный формат email";
    private static final String NOT_VALID_PASSWORD = "Неправильный формат пароля";

    public Result<Boolean> validateName(@NonNull String name) {
        if (name.matches("^[a-zA-Zа-яА-Я]+$") && name.length() <= 10) {
            return Result.success(true);
        }
        return Result.failure(new RuntimeException(NOT_VALID_NAME));
    }

    public Result<Boolean> validateEmail(@NonNull String email) {
        if (email.matches("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+") && email.length() <= 50) {
            return Result.success(true);
        }
        return Result.failure(new RuntimeException(NOT_VALID_EMAIL));
    }

    public Result<Boolean> validatePassword(@NonNull String password) {
        if (password.length() >= 5 && password.length() <= 10) {
            return Result.success(true);
        }
        return Result.failure(new RuntimeException(NOT_VALID_PASSWORD));
    }
}
