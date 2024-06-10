package org.vnsemkin.semkintelegrambot.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerRegistrationDto;
import org.vnsemkin.semkintelegrambot.application.mappers.AppMapper;
import org.vnsemkin.semkintelegrambot.domain.models.Customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AppMapperTest {
    private static final long TG_ID = 120198730L;
    private static final String TG_USERNAME = "test";
    private static final String NAME = "John Doe";
    private static final String EMAIL = "john.doe@example.com";
    private static final String PASSWORD = "password123";
    private final AppMapper mapper = Mappers.getMapper(AppMapper.class);

    @Test
    public void shouldMapCustomerToCustomerDto() {
        Customer customer = new Customer(TG_ID, NAME, TG_USERNAME);
        customer.setEmail(EMAIL);
        customer.setPassword(PASSWORD);

        CustomerRegistrationDto customerDto = mapper.toDto(customer);

        assertNotNull(customerDto);
        assertEquals(TG_ID, customerDto.tgId());
        assertEquals(NAME, customerDto.firstName());
        assertEquals(TG_USERNAME, customerDto.username());
        assertEquals(EMAIL, customerDto.email());
        assertEquals(PASSWORD, customerDto.password());
    }
}