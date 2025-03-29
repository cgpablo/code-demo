package com.cgpablo.prices.rest.port.in.controller;

import java.util.UUID;

import com.cgpablo.prices.domain.port.in.PriceService;
import com.cgpablo.prices.rest.port.in.api.PricesApi;
import com.cgpablo.prices.rest.port.in.mapper.PriceMapper;
import com.cgpablo.prices.rest.port.in.model.ApplicablePriceResponseDTO;
import com.cgpablo.prices.rest.port.in.model.PriceResponseDTO;
import com.cgpablo.prices.rest.port.in.validator.ApplicationDateValidator;
import com.cgpablo.prices.rest.port.in.validator.UUIDValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PriceController implements PricesApi {

  private final ApplicationDateValidator applicationDateValidator;

  private final UUIDValidator uuidValidator;

  private final PriceService service;

  private final PriceMapper mapper;

  @Override
  public ResponseEntity<ApplicablePriceResponseDTO> getPricesByFilter(final Long brandId, final Long productId,
      final String applicationDate) {

    final var bindingResult = new BeanPropertyBindingResult(applicationDate, "applicationDate");
    applicationDateValidator.validate(applicationDate, bindingResult);

    final var command = mapper.asGetApplicablePriceCommand(brandId, productId, applicationDate);
    final var price = service.getApplicablePrice(command);

    return ResponseEntity.ok(mapper.asApplicablePriceResponseDTO(price));
  }

  @Override
  public ResponseEntity<PriceResponseDTO> getPriceById(final String priceId) {

    final var bindingResult = new BeanPropertyBindingResult(priceId, "priceId");
    uuidValidator.validate(priceId, bindingResult);

    final var price = service.getPriceById(UUID.fromString(priceId));
    return ResponseEntity.ok(mapper.asPriceResponseDTO(price));
  }
}
