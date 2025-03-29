package com.cgpablo.prices.application.service;

import java.util.UUID;

import com.cgpablo.prices.domain.exceptions.CircuitBreakerOpenException;
import com.cgpablo.prices.domain.exceptions.EntityNotFoundException;
import com.cgpablo.prices.domain.model.Price;
import com.cgpablo.prices.domain.port.in.PriceService;
import com.cgpablo.prices.domain.port.out.PriceRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

  public static final String PRICE_SERVICE_CB = "pricesService";

  private final CircuitBreakerRegistry circuitBreakerRegistry;

  private final PriceRepository priceRepository;

  @Override
  @Cacheable(value = "pricesCache", key = "#priceId")
  @CircuitBreaker(name = PRICE_SERVICE_CB, fallbackMethod = "fallbackGetPriceById")
  public Price getPriceById(final UUID priceId) {
    return priceRepository.findPriceById(priceId)
        .orElseThrow(() -> new EntityNotFoundException(String.format("price with priceId: %s not found", priceId)));
  }

  @Override
  @Cacheable(value = "applicablePricesCache",
      key = "#command.productId + '-' + #command.brandId + '-' + #command.applicationDate.toString()")
  @CircuitBreaker(name = PRICE_SERVICE_CB, fallbackMethod = "fallbackGetApplicablePrice")
  public Price getApplicablePrice(final GetApplicablePriceCommand command) {
    return priceRepository.findPricesByFilter(command).orElseThrow(() -> new EntityNotFoundException(
        String.format("price with brandId: %s, productId: %s and applicationDate: %s not found", command.brandId(), command.productId(),
            command.applicationDate())));
  }

  private Price fallbackGetPriceById(final UUID priceId, final Throwable ex) {
    return handleFallback(priceId, ex);

  }

  private Price fallbackGetApplicablePrice(final GetApplicablePriceCommand command, final Throwable ex) {
    return handleFallback(command, ex);
  }

  private Price handleFallback(Object context, Throwable ex) {
    if (ex instanceof EntityNotFoundException) {
      throw (EntityNotFoundException) ex;
    }
    if (ex instanceof PersistenceException) {
      throw (PersistenceException) ex;
    }
    if (isCircuitOpen()) {
      throw new CircuitBreakerOpenException("Call not permitted");
    }
    log.error("Circuit Breaker triggered. Context: {}, Error: {}", context, ex.getMessage());
    return null; // maybe call another service which could retrieve the info from another source
  }

  private boolean isCircuitOpen() {
    return circuitBreakerRegistry.circuitBreaker(PRICE_SERVICE_CB).getState() == State.OPEN;
  }
}
