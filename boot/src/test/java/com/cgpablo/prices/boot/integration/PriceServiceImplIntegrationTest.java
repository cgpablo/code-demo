package com.cgpablo.prices.boot.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import com.cgpablo.prices.application.service.PriceServiceImpl;
import com.cgpablo.prices.boot.Application;
import com.cgpablo.prices.domain.exceptions.CircuitBreakerOpenException;
import com.cgpablo.prices.domain.model.Price;
import com.cgpablo.prices.domain.port.out.PriceRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.QueryTimeoutException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@EnableAutoConfiguration
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PriceServiceImplIntegrationTest {

  @Autowired
  private PriceServiceImpl priceService;

  @MockitoBean
  private PriceRepository priceRepository;

  @Autowired
  private CircuitBreakerRegistry circuitBreakerRegistry;

  @Test
  public void returnsPriceWhenCircuitBreakerIsClosedAndBackendIsFunctional() {
    // Given
    final var priceId = Instancio.create(UUID.class);

    circuitBreakerRegistry.circuitBreaker(PriceServiceImpl.PRICE_SERVICE_CB)
        .transitionToClosedState();

    final var price = Instancio.create(Price.class);
    when(priceRepository.findPriceById(eq(priceId))).thenReturn(Optional.of(price));

    final var result = priceService.getPriceById(priceId);

    assertEquals(price, result);
    verify(priceRepository, times(1)).findPriceById(eq(priceId));
  }

  @Test
  public void persistenceExceptionWhenCircuitBreakerIsClosedButBackendTimesOut() {
    // Given
    final var priceId = Instancio.create(UUID.class);

    circuitBreakerRegistry.circuitBreaker(PriceServiceImpl.PRICE_SERVICE_CB)
        .transitionToClosedState();

    when(priceRepository.findPriceById(eq(priceId))).thenThrow(new QueryTimeoutException(""));

    try {
      priceService.getPriceById(priceId);
      fail("Expected PersistenceException to be thrown");
    } catch (PersistenceException e) {
      assertEquals(QueryTimeoutException.class, e.getClass());
      verify(priceRepository, times(1)).findPriceById(eq(priceId));
    }
  }

  @Test
  public void circuitBreakerOpenExceptionWhenCircuitBreakerIsOpen() {
    // Given
    final var priceId = Instancio.create(UUID.class);

    circuitBreakerRegistry.circuitBreaker(PriceServiceImpl.PRICE_SERVICE_CB)
        .transitionToOpenState();

    when(priceRepository.findPriceById(eq(priceId)))
        .thenThrow(new QueryTimeoutException(""))
        .thenThrow(new QueryTimeoutException(""))
        .thenThrow(new QueryTimeoutException(""))
        .thenThrow(new QueryTimeoutException(""))
        .thenThrow(new QueryTimeoutException(""));

    // When & Then
    try {
      priceService.getPriceById(priceId);
      fail("Expected CircuitBreakerOpenException to be thrown");
    } catch (RuntimeException e) {
      assertEquals(CircuitBreakerOpenException.class, e.getClass());
      verifyNoInteractions(priceRepository);
    }
  }
}