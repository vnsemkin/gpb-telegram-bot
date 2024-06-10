package org.vnsemkin.semkintelegrambot.application.dtos;

public record CustomerRegistrationDto(long tgId, String firstName, String username, String email, String password) {
}