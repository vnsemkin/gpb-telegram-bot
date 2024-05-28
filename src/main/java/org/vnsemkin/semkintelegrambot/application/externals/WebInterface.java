package org.vnsemkin.semkintelegrambot.application.externals;

import org.vnsemkin.semkintelegrambot.application.dtos.CustomerDto;
import org.vnsemkin.semkintelegrambot.application.dtos.ResultDto;

public interface WebInterface {
   ResultDto<CustomerDto> registerCustomer(CustomerDto customerDto);
}
