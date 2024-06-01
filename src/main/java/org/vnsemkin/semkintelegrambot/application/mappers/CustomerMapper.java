package org.vnsemkin.semkintelegrambot.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerDto;
import org.vnsemkin.semkintelegrambot.domain.models.Customer;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDto toDto(Customer customer);
}
