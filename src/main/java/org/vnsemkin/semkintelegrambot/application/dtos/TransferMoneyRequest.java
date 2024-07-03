package org.vnsemkin.semkintelegrambot.application.dtos;

import org.springframework.lang.NonNull;

public record TransferMoneyRequest(@NonNull String from, @NonNull String to, @NonNull String amount) {
}