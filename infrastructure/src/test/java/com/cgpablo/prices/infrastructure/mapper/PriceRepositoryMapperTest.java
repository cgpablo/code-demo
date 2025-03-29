package com.cgpablo.prices.infrastructure.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.cgpablo.prices.infrastructure.entity.PriceEntity;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

class PriceRepositoryMapperTest {

  private final PriceRepositoryMapper mapper = new PriceRepositoryMapperImpl();

  @Test
  void shouldMapPriceEntityToPrice() {
    // Given
    final var priceEntity = Instancio.create(PriceEntity.class);

    // When
    final var price = mapper.asPrice(priceEntity);

    // Then
    assertThat(price)
        .isNotNull()
        .usingRecursiveComparison()
        .isEqualTo(priceEntity);
  }

  @Test
  void shouldReturnNull_WhenMappingNullEntity() {
    // When
    final var price = mapper.asPrice(null);

    // Then
    assertThat(price).isNull();
  }
}
