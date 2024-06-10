package org.vnsemkin.semkintelegrambot.application.dtos;

import java.math.BigDecimal;

public record AccountDto (String uuid, String accountName, BigDecimal balance){
}
