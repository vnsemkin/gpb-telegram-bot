package org.vnsemkin.semkintelegrambot.domain.utils;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.vnsemkin.semkintelegrambot.domain.models.Result;

import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.MAX_AMOUNT_LENGTH;
import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.MAX_EMAIL_LENGTH;
import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.MAX_NAME_LENGTH;
import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.MAX_PASSWORD_LENGTH;
import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.MIN_AMOUNT_LENGTH;
import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.MIN_NAME_LENGTH;
import static org.vnsemkin.semkintelegrambot.application.constants.AppConstants.MIN_PASSWORD_LENGTH;

@Service
public final class AppValidator {
    private static final String NOT_VALID_EMAIL = "Неправильный формат email";
    private static final String EMPTY_NAME = "Имя не может быть пустым.";
    private static final String INVALID_NAME_LENGTH = "Имя не может быть меньше 2 или больше 15 символов";
    private static final String INVALID_NAME_CHARACTERS = "Имя должно состоять из латинский букв нижнего регистра";
    private static final String EMPTY_AMOUNT = "Сумма не может быть пустой";
    private static final String INVALID_AMOUNT_LENGTH = "Сумма должна быть от 0 до 1_000_000";
    private static final String INVALID_AMOUNT_DECIMAL_LENGTH = "Сумма не должна превышать 2 десятичных разряда";
    private static final String NOT_VALID_AMOUNT = "Неправильный формат amount";
    private static final String NOT_VALID_PASSWORD = "Неправильный формат пароля";
    private static final String EMAIL_REGEX_VALIDATION = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

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

    public Result<Boolean, String> validateName(@NonNull String name) {
        if (name.isEmpty()) {
            return Result.error(EMPTY_NAME);
        }

        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            return Result.error(INVALID_NAME_LENGTH);
        }

        if (!name.matches("[a-z]+")) {
            return Result.error(INVALID_NAME_CHARACTERS);
        }

        return Result.success(true);
    }


    public Result<Boolean, String> validateAmount(@NonNull String amount) {
        if (amount.isEmpty()) {
            return Result.error(EMPTY_AMOUNT);
        }

        try {
            double value = Double.parseDouble(amount);

            if (value < MIN_AMOUNT_LENGTH || value > MAX_AMOUNT_LENGTH) {
                return Result.error(INVALID_AMOUNT_LENGTH);
            }

            if (amount.contains(".")) {
                return validateDecimalPart(amount.split("\\."));
            }

            return Result.success(true);
        } catch (NumberFormatException e) {
            return Result.error(NOT_VALID_AMOUNT);
        }
    }

    private Result<Boolean, String> validateDecimalPart(String[] parts){
        if (parts.length > 2 || (parts.length == 2 && parts[1].length() > 2)) {
            return Result.error(INVALID_AMOUNT_DECIMAL_LENGTH);
        }
        return Result.success(true);
    }
}