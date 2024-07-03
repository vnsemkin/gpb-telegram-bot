package org.vnsemkin.semkintelegrambot.application.dtos;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public record AccountDto(@NonNull String uuid, @NonNull String accountName, @NonNull BigDecimal balance) {
}
