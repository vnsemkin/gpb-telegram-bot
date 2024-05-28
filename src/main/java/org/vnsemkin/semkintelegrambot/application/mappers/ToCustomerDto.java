package org.vnsemkin.semkintelegrambot.application.mappers;

import org.vnsemkin.semkintelegrambot.application.dtos.CustomerDto;
import org.vnsemkin.semkintelegrambot.domain.models.Customer;

public class ToCustomerDto {
    public static CustomerDto toCustomerDto(Customer customer) {
        return new CustomerDto(customer.getName(),
            customer.getEmail(),
            customer.getPassword());
    }
}
