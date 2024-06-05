package org.vnsemkin.semkintelegrambot.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerDto;
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
        // ARRANGE
        Customer customer = new Customer();
        customer.setTgId(TG_ID);
        customer.setFirstName(NAME);
        customer.setUsername(TG_USERNAME);
        customer.setEmail(EMAIL);
        customer.setPassword(PASSWORD);

        // ACT
        CustomerDto customerDto = mapper.toDto(customer);

        // ASSERT
        assertNotNull(customerDto);
        assertEquals(TG_ID, customerDto.tgId());
        assertEquals(NAME, customerDto.firstName());
        assertEquals(TG_USERNAME, customerDto.username());
        assertEquals(EMAIL, customerDto.email());
        assertEquals(PASSWORD, customerDto.password());
    }
}