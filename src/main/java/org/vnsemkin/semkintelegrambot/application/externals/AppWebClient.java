package org.vnsemkin.semkintelegrambot.application.externals;

import org.vnsemkin.semkintelegrambot.application.dtos.AccountRegistrationRequest;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountRegistrationResponse;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerInfoResponse;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerRegistrationDto;
import org.vnsemkin.semkintelegrambot.domain.models.Result;

public interface AppWebClient {
   Result<CustomerRegistrationDto, String> registerCustomer(CustomerRegistrationDto customerDto);
   Result<AccountRegistrationResponse, String> registerAccount(AccountRegistrationRequest request);
   Result<CustomerInfoResponse, String> getCustomerInfo(long tgId);
}