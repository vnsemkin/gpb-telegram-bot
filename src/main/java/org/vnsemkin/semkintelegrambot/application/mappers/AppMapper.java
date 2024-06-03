package org.vnsemkin.semkintelegrambot.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerDto;
import org.vnsemkin.semkintelegrambot.domain.models.Customer;

@Mapper
public interface AppMapper {
    AppMapper INSTANCE = Mappers.getMapper(AppMapper.class);
    CustomerDto toDto(Customer customer);
}