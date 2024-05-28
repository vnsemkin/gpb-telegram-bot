package org.vnsemkin.semkintelegrambot.domain.services.senders;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerDto;
import org.vnsemkin.semkintelegrambot.application.dtos.ResultDto;
import org.vnsemkin.semkintelegrambot.application.externals.WebInterface;
import org.vnsemkin.semkintelegrambot.presentation.web_client.MiddleServiceWebClient;

@Component
public class ExternalSender {
    private final WebInterface webInterface;

    public ExternalSender(MiddleServiceWebClient middleServiceWebClient) {
        this.webInterface = middleServiceWebClient;
    }

    public ResultDto<CustomerDto> requestRegistration(@NonNull CustomerDto customerDto) {
        return webInterface.registerCustomer(customerDto);
    }
}
