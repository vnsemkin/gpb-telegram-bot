package org.vnsemkin.semkintelegrambot.application.dtos;

import org.springframework.lang.NonNull;

public record CustomerRegistrationDto(long tgId, @NonNull String firstName, @NonNull String userName,
                                      @NonNull String email, @NonNull String password) {
}