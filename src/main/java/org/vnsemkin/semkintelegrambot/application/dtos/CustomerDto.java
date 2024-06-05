package org.vnsemkin.semkintelegrambot.application.dtos;

public record CustomerDto(long tgId, String firstName, String username, String email, String password) {
}