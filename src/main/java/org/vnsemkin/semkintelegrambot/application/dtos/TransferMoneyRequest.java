package org.vnsemkin.semkintelegrambot.application.dtos;

public record TransferMoneyRequest(String from, String to, String amount) {
}