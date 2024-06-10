package org.vnsemkin.semkintelegrambot.presentation.web_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountDto;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountRegistrationRequest;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountRegistrationResponse;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerInfoResponse;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerRegistrationDto;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import reactor.core.publisher.Mono;

@Component
public final class AppWebClientImp implements AppWebClient {
    private final static String CUSTOMERS_ENDPOINT = "/customers";
    private final static String CUSTOMER_INFO_ENDPOINT = "/customers/%d";
    private final static String ACCOUNTS_ENDPOINT = "/customers/%d/accounts";
    private final WebClient webClient;

    public AppWebClientImp(WebClient.Builder webClientBuilder,
                           @Value("${middle-service.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Result<CustomerRegistrationDto, String> registerCustomer(CustomerRegistrationDto customerDto) {
        return webClient
            .post()
            .uri(CUSTOMERS_ENDPOINT)
            .body(BodyInserters.fromValue(customerDto))
            .exchangeToMono(response ->
                response.statusCode().is2xxSuccessful() ?
                    response.bodyToMono(CustomerRegistrationDto.class)
                        .map(Result::<CustomerRegistrationDto, String>success) :
                    response.bodyToMono(String.class)
                        .map(Result::<CustomerRegistrationDto, String>error))
            .onErrorResume(throwable ->
                Mono.just(Result.error(throwable.getMessage())))
            .block();
    }

    public Result<AccountRegistrationResponse, String> registerAccount(AccountRegistrationRequest request) {
        return webClient
            .post()
            .uri(String.format(ACCOUNTS_ENDPOINT, request.tgId()))
            .exchangeToMono(response ->
                response.statusCode().is2xxSuccessful() ?
                    response.bodyToMono(AccountRegistrationResponse.class)
                        .map(Result::<AccountRegistrationResponse, String>success) :
                    response.bodyToMono(String.class)
                        .map(Result::<AccountRegistrationResponse, String>error))
            .onErrorResume(throwable ->
                Mono.just(Result.error(throwable.getMessage())))
            .block();
    }

    public Result<CustomerInfoResponse, String> getCustomerInfo(long tgId) {
        return webClient
            .get()
            .uri(String.format(CUSTOMER_INFO_ENDPOINT, tgId))
            .exchangeToMono(response ->
                response.statusCode().is2xxSuccessful() ?
                    response.bodyToMono(CustomerInfoResponse.class)
                        .map(Result::<CustomerInfoResponse, String>success) :
                    response.bodyToMono(String.class)
                        .map(Result::<CustomerInfoResponse, String>error))
            .onErrorResume(throwable ->
                Mono.just(Result.error(throwable.getMessage())))
            .block();
    }

    public Result<AccountDto, String> getCustomerAccount(long tgId) {
        return webClient
            .get()
            .uri(String.format(ACCOUNTS_ENDPOINT, tgId))
            .exchangeToMono(response ->
                response.statusCode().is2xxSuccessful() ?
                    response.bodyToMono(AccountDto.class)
                        .map(Result::<AccountDto, String>success) :
                    response.bodyToMono(String.class)
                        .map(Result::<AccountDto, String>error))
            .onErrorResume(throwable ->
                Mono.just(Result.error(throwable.getMessage())))
            .block();
    }
}