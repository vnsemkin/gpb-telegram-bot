package org.vnsemkin.semkintelegrambot.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerDto;
import org.vnsemkin.semkintelegrambot.application.mappers.AppMapper;
import org.vnsemkin.semkintelegrambot.domain.models.Customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AppMapperTest {
    private static final String NAME = "John Doe";
    private static final String EMAIL = "john.doe@example.com";
    private static final String PASSWORD = "password123";
    private final AppMapper mapper = Mappers.getMapper(AppMapper.class);

    @Test
    public void shouldMapCustomerToCustomerDto() {
        // ARRANGE
        Customer customer = new Customer();
        customer.setName(NAME);
        customer.setEmail(EMAIL);
        customer.setPassword(PASSWORD);

        // ACT
        CustomerDto customerDto = mapper.toDto(customer);

        // ASSERT
        assertNotNull(customerDto);
        assertEquals(NAME, customerDto.name());
        assertEquals(EMAIL, customerDto.email());
        assertEquals(PASSWORD, customerDto.password());
    }
}