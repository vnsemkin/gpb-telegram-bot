package org.vnsemkin.semkintelegrambot.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerRegistrationDto;
import org.vnsemkin.semkintelegrambot.domain.models.Customer;

@Mapper
public interface AppMapper {
    AppMapper INSTANCE = Mappers.getMapper(AppMapper.class);
    CustomerRegistrationDto toDto(Customer customer);
}