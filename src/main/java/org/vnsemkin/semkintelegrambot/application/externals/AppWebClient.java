package org.vnsemkin.semkintelegrambot.application.externals;

import org.vnsemkin.semkintelegrambot.application.dtos.CustomerDto;
import org.vnsemkin.semkintelegrambot.domain.models.Result;

public interface AppWebClient {
   Result<CustomerDto, String> registerCustomer(CustomerDto customerDto);
}