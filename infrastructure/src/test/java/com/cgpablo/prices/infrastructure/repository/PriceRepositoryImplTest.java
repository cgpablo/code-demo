package com.cgpablo.prices.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.cgpablo.prices.domain.model.Price;
import com.cgpablo.prices.domain.port.in.PriceService.GetApplicablePriceCommand;
import com.cgpablo.prices.domain.port.out.PriceRepository;
import com.cgpablo.prices.infrastructure.entity.PriceEntity;
import com.cgpablo.prices.infrastructure.mapper.PriceRepositoryMapper;
import com.cgpablo.prices.infrastructure.repository.adapter.PriceRepositoryImpl;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PriceRepositoryImplTest {

  @Mock
  private PriceJPARepository jpaRepository;

  @Mock
  private PriceRepositoryMapper repositoryMapper;

  private PriceRepository priceRepository;

  @BeforeEach
  void setUp() {
    priceRepository = new PriceRepositoryImpl(jpaRepository, repositoryMapper);
  }

  @Test
  void findPriceById_ShouldReturnPrice_WhenExists() {
    // Given
    final var priceId = Instancio.create(UUID.class);
    final var priceEntity = Instancio.create(PriceEntity.class);
    final var price = Instancio.create(Price.class);

    when(jpaRepository.findByPriceId(priceId)).thenReturn(Optional.of(priceEntity));
    when(repositoryMapper.asPrice(priceEntity)).thenReturn(price);

    // When
    final var result = priceRepository.findPriceById(priceId);

    // Then
    assertThat(result).isPresent().contains(price);
    verify(jpaRepository, times(1)).findByPriceId(priceId);
    verify(repositoryMapper, times(1)).asPrice(priceEntity);
  }

  @Test
  void findPriceById_ShouldReturnEmpty_WhenDoesNotExist() {
    // Given
    final var priceId = Instancio.create(UUID.class);
    when(jpaRepository.findByPriceId(priceId)).thenReturn(Optional.empty());

    // When
    final var result = priceRepository.findPriceById(priceId);

    // Then
    assertThat(result).isEmpty();
    verify(jpaRepository, times(1)).findByPriceId(priceId);
    verifyNoInteractions(repositoryMapper);
  }

  @Test
  void findPricesByFilter_ShouldReturnMappedPrice() {
    // Given
    final var brandId = 1L;
    final var productId = 100L;
    final var applicationDate = LocalDateTime.now();

    final var priceEntity = Instancio.create(PriceEntity.class);
    final var expectedPrice = Instancio.create(Price.class);

    when(jpaRepository.findByFilter(brandId, productId, applicationDate)).thenReturn(Optional.of(priceEntity));
    when(repositoryMapper.asPrice(any())).thenReturn(expectedPrice);

    // When
    final var result = priceRepository.findPricesByFilter(new GetApplicablePriceCommand(brandId, productId, applicationDate));

    // Then
    assertThat(result).isPresent().contains(expectedPrice);
    verify(jpaRepository, times(1)).findByFilter(brandId, productId, applicationDate);
    verify(repositoryMapper, times(1)).asPrice(any(PriceEntity.class));
  }

  @Test
  void findPricesByFilter_ShouldReturnEmpty_WhenCriteriaIsNotMet() {
    // Given
    final var brandId = 1L;
    final var productId = 100L;
    final var applicationDate = LocalDateTime.now();

    when(jpaRepository.findByFilter(brandId, productId, applicationDate)).thenReturn(Optional.empty());

    // When
    final var result = priceRepository.findPricesByFilter(new GetApplicablePriceCommand(brandId, productId, applicationDate));

    // Then
    assertThat(result).isEmpty();
    verify(jpaRepository, times(1)).findByFilter(brandId, productId, applicationDate);
    verifyNoInteractions(repositoryMapper);
  }
}
