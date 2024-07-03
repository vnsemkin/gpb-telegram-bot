package org.vnsemkin.semkintelegrambot.application.dtos;

import org.springframework.lang.NonNull;

public record CustomerInfoResponse(@NonNull String firstName, @NonNull String userName, @NonNull String email,
                                   @NonNull String uuid, String accountName) {
}
