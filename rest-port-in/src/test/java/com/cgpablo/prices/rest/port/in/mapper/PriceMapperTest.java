package com.cgpablo.prices.rest.port.in.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.function.BiFunction;

import com.cgpablo.prices.rest.port.in.model.ApplicablePriceDTO;
import com.cgpablo.prices.rest.port.in.model.PriceDTO;
import com.cgpablo.prices.domain.model.Price;
import com.cgpablo.prices.domain.port.in.PriceService.GetApplicablePriceCommand;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

class PriceMapperTest {

  private final PriceMapper priceMapper = new PriceMapperImpl();

  final BiFunction<String, BigDecimal, Integer> compareStringToBigDecimal = (str, target) ->
      target.compareTo(new BigDecimal(str.trim()));

  @Test
  void shouldMapPriceToApplicablePriceResponseDTO() {
    // Given
    final var price = Instancio.create(Price.class);

    // When
    final var dto = priceMapper.asApplicablePriceResponseDTO(price);

    // Then
    assertThat(dto).isNotNull();
    verifyApplicablePriceAttributeValues(price, dto.getApplicablePrice());
  }

  @Test
  void shouldMapPriceToApplicablePriceDTO() {
    // Given
    final var price = Instancio.create(Price.class);

    // When
    final var dto = priceMapper.asApplicablePriceDTO(price);

    // Then
    verifyApplicablePriceAttributeValues(price, dto);
  }

  @Test
  void shouldMapPriceToPriceResponseDTO() {
    // Given
    final var price = Instancio.create(Price.class);

    // When
    final var dto = priceMapper.asPriceResponseDTO(price);

    // Then
    assertThat(dto).isNotNull();
    verifyPriceAttributeValues(price, dto.getPrice());
  }

  @Test
  void shouldMapPriceToPriceDTO() {
    // Given
    final var price = Instancio.create(Price.class);

    // When
    final var dto = priceMapper.asPriceDTO(price);

    // Then
    verifyPriceAttributeValues(price, dto);
  }

  @Test
  void shouldMapToGetApplicablePriceCommand() {
    // Given
    final var brandId = 1L;
    final var productId = 2L;
    final var applicationDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // When
    final var command = priceMapper.asGetApplicablePriceCommand(brandId, productId, applicationDate);

    // Then
    assertThat(command)
        .isNotNull()
        .extracting(
            GetApplicablePriceCommand::brandId,
            GetApplicablePriceCommand::productId,
            GetApplicablePriceCommand::applicationDate
        )
        .containsExactly(
            brandId,
            productId,
            priceMapper.stringToLocalDateTime(applicationDate)
        );
  }

  @Test
  void shouldMapLocalDateTimeToString() {
    // Given
    final var localDateTime = LocalDateTime.of(2025, 3, 27, 23, 6, 44);

    // When
    final var formattedDate = priceMapper.localDateTimeToString(localDateTime);

    // Then
    assertThat(formattedDate)
        .isNotNull()
        .isEqualTo("2025-03-27 23:06:44");
  }

  @Test
  void shouldMapStringToLocalDateTime() {
    // Given
    final var str = "2025-03-27 23:06:44";

    // When
    final var localDateTime = priceMapper.stringToLocalDateTime(str);

    // Then
    assertThat(localDateTime)
        .isNotNull()
        .isEqualTo(LocalDateTime.of(2025, 3, 27, 23, 6, 44));
  }

  private void verifyApplicablePriceAttributeValues(final Price price, final ApplicablePriceDTO dto) {
    assertThat(dto)
        .isNotNull()
        .usingRecursiveComparison()
        .withComparatorForFields((str, bigDecimal) ->
            compareStringToBigDecimal.apply((String) str, (BigDecimal) bigDecimal), "amount")
        .withComparatorForFields((str, dateTime) ->
                compareLocalDateTimes(LocalDateTime.parse((String) str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    (LocalDateTime) dateTime),
            "startDate", "endDate")
        .isEqualTo(price);
  }

  private void verifyPriceAttributeValues(final Price price, final PriceDTO dto) {
    assertThat(dto)
        .isNotNull()
        .usingRecursiveComparison()
        .withComparatorForFields((str, uuid) ->
            UUID.fromString((String) str).compareTo((UUID) uuid), "priceId")
        .withComparatorForFields((str, bigDecimal) ->
            compareStringToBigDecimal.apply((String) str, (BigDecimal) bigDecimal), "amount")
        .withComparatorForFields((str, dateTime) ->
                compareLocalDateTimes(LocalDateTime.parse((String) str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    (LocalDateTime) dateTime),
            "startDate", "endDate")
        .isEqualTo(price);
  }

  private int compareLocalDateTimes(final LocalDateTime expected, final LocalDateTime actual) {
    long expectedSeconds = Duration.between(expected, LocalDateTime.of(expected.toLocalDate(), expected.toLocalTime())).getSeconds();
    long actualSeconds = Duration.between(actual, LocalDateTime.of(actual.toLocalDate(), actual.toLocalTime())).getSeconds();

    return Long.compare(expectedSeconds, actualSeconds);
  }
}
