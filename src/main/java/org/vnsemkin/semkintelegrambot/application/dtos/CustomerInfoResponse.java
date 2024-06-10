package org.vnsemkin.semkintelegrambot.application.dtos;

public record CustomerInfoResponse(String firstName, String username, String email,
                                   String uuid, String accountName) {}
