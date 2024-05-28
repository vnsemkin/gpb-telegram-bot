package org.vnsemkin.semkintelegrambot.presentation.web_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerDto;
import org.vnsemkin.semkintelegrambot.application.dtos.ResultDto;
import org.vnsemkin.semkintelegrambot.application.externals.WebInterface;
import reactor.core.publisher.Mono;

@Component
public class MiddleServiceWebClient implements WebInterface {
    private final static String REG_ENDPOINT = "/registration";
    private final WebClient webClient;

    public MiddleServiceWebClient(WebClient.Builder webClientBuilder,
                                  @Value("${middle-service.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public ResultDto<CustomerDto> registerCustomer(CustomerDto customerDto) {
        ParameterizedTypeReference<ResultDto<CustomerDto>> responseType =
            new ParameterizedTypeReference<>() {};

        return webClient
            .post()
            .uri(REG_ENDPOINT)
            .body(BodyInserters.fromValue(customerDto))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(responseType);
                } else {
                    return response.bodyToMono(responseType)
                        .map(resultDto -> ResultDto.failure(resultDto.getError()));
                }
            })
            .onErrorResume(throwable -> Mono.just(ResultDto.failure(throwable.getMessage())))
            .block();
    }
}

