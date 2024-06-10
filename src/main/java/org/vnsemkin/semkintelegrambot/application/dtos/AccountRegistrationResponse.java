package org.vnsemkin.semkintelegrambot.application.dtos;

import java.math.BigDecimal;

public record AccountRegistrationResponse(String accountName, BigDecimal balance, String info) {
}
