package com.cgpablo.prices.rest.port.in.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.cgpablo.prices.rest.port.in.model.ApplicablePriceDTO;
import com.cgpablo.prices.rest.port.in.model.ApplicablePriceResponseDTO;
import com.cgpablo.prices.rest.port.in.model.PriceDTO;
import com.cgpablo.prices.rest.port.in.model.PriceResponseDTO;
import com.cgpablo.prices.domain.model.Price;
import com.cgpablo.prices.domain.port.in.PriceService.GetApplicablePriceCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface PriceMapper {

  @Mapping(target = "applicablePrice", source = ".")
  ApplicablePriceResponseDTO asApplicablePriceResponseDTO(final Price price);

  @Mapping(target = "endDate", source = "price.endDate", qualifiedByName = "mapToString")
  @Mapping(target = "startDate", source = "price.startDate", qualifiedByName = "mapToString")
  ApplicablePriceDTO asApplicablePriceDTO(final Price price);

  PriceResponseDTO asPriceResponseDTO(final Price price);

  @Mapping(target = "endDate", source = "price.endDate", qualifiedByName = "mapToString")
  @Mapping(target = "startDate", source = "price.startDate", qualifiedByName = "mapToString")
  PriceDTO asPriceDTO(final Price price);

  @Mapping(target = "applicationDate", source = "applicationDate", qualifiedByName = "mapToLocalDateTime")
  GetApplicablePriceCommand asGetApplicablePriceCommand(Long brandId, Long productId, String applicationDate);

  @Named("mapToString")
  default String localDateTimeToString(final LocalDateTime date) {
    return Objects.nonNull(date) ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
  }

  @Named("mapToLocalDateTime")
  default LocalDateTime stringToLocalDateTime(final String date) {
    return LocalDateTime.parse(date.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }
}
