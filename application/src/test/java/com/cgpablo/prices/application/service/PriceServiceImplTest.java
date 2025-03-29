package com.cgpablo.prices.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import com.cgpablo.prices.domain.exceptions.EntityNotFoundException;
import com.cgpablo.prices.domain.model.Price;
import com.cgpablo.prices.domain.port.in.PriceService.GetApplicablePriceCommand;
import com.cgpablo.prices.domain.port.out.PriceRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PriceServiceImplTest {

  @Mock
  private CircuitBreakerRegistry circuitBreakerRegistry;

  @Mock
  private PriceRepository priceRepository;

  private PriceServiceImpl priceService;

  @Captor
  private ArgumentCaptor<UUID> priceIdCaptor;

  @BeforeEach
  void setUp() {
    priceService = new PriceServiceImpl(circuitBreakerRegistry, priceRepository);
  }

  @Test
  void getPriceById_shouldReturnPrice_whenFound() {
    // Given
    final var priceId = Instancio.create(UUID.class);
    final var price = Instancio.create(Price.class);

    when(priceRepository.findPriceById(priceId)).thenReturn(Optional.of(price));

    // When
    final var result = priceService.getPriceById(priceId);

    // Then
    assertThat(result).isEqualTo(price);
    verify(priceRepository, times(1)).findPriceById(priceIdCaptor.capture());
    assertThat(priceIdCaptor.getValue()).isEqualTo(priceId);
  }

  @Test
  void getPriceById_shouldThrowException_whenNotFound() {
    // Given
    final var priceId = Instancio.create(UUID.class);

    when(priceRepository.findPriceById(priceId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(EntityNotFoundException.class, () -> priceService.getPriceById(priceId));
    verify(priceRepository, times(1)).findPriceById(priceId);
  }

  @Test
  void getApplicablePrice_shouldReturnPrice_whenFound() {
    // Given
    final var command = Instancio.create(GetApplicablePriceCommand.class);
    final var price = Instancio.create(Price.class);

    when(priceRepository.findPricesByFilter(command))
        .thenReturn(Optional.of(price));

    // When
    final var result = priceService.getApplicablePrice(command);

    // Then
    assertThat(result).isEqualTo(price);
  }

  @Test
  void getApplicablePrice_shouldThrowException_whenNotFound() {
    // Given
    final var command = Instancio.create(GetApplicablePriceCommand.class);

    when(priceRepository.findPricesByFilter(command))
        .thenReturn(Optional.empty());

    // When & Then
    assertThrows(EntityNotFoundException.class, () -> priceService.getApplicablePrice(command));
    verify(priceRepository, times(1)).findPricesByFilter(command);
  }
}
