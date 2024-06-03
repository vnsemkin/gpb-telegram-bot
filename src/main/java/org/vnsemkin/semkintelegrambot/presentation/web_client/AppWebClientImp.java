package org.vnsemkin.semkintelegrambot.presentation.web_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerDto;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import reactor.core.publisher.Mono;

@Component
public final class AppWebClientImp implements AppWebClient {
    private final static String REG_ENDPOINT = "/registration";
    private final WebClient webClient;

    public AppWebClientImp(WebClient.Builder webClientBuilder,
                           @Value("${middle-service.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Result<CustomerDto, String> registerCustomer(CustomerDto customerDto) {
        return webClient
            .post()
            .uri(REG_ENDPOINT)
            .body(BodyInserters.fromValue(customerDto))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(CustomerDto.class)
                        .map(Result::<CustomerDto, String>success);
                } else {
                    return response.bodyToMono(String.class)
                        .map(Result::<CustomerDto, String>error);
                }
            })
            .onErrorResume(throwable ->
                Mono.just(Result.error(throwable.getMessage())))
            .block();
    }
}