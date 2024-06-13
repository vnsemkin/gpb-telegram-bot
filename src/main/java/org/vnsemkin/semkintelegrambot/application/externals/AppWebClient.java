package org.vnsemkin.semkintelegrambot.application.externals;

import org.springframework.stereotype.Component;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountDto;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountRegistrationRequest;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountRegistrationResponse;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerInfoResponse;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerRegistrationDto;
import org.vnsemkin.semkintelegrambot.application.dtos.TransferMoneyRequest;
import org.vnsemkin.semkintelegrambot.application.dtos.TransferMoneyResponse;
import org.vnsemkin.semkintelegrambot.domain.models.Result;

@Component
public interface AppWebClient {
   Result<CustomerRegistrationDto, String> registerCustomer(CustomerRegistrationDto customerDto);
   Result<AccountRegistrationResponse, String> registerAccount(AccountRegistrationRequest request);
   Result<CustomerInfoResponse, String> getCustomerInfo(long tgId);
   Result<AccountDto, String> getCustomerAccount(long tgId);
   Result<TransferMoneyResponse, String> transferMoney(TransferMoneyRequest request);
}