package com.cgpablo.prices.infrastructure.mapper;

import com.cgpablo.prices.domain.model.Price;
import com.cgpablo.prices.infrastructure.entity.PriceEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface PriceRepositoryMapper {

  Price asPrice(final PriceEntity priceEntity);
}