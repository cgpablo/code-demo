package com.cgpablo.prices.rest.port.in.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.cgpablo.prices.rest.port.in.exception.InvalidDateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.Errors;

class ApplicationDateValidatorTest {

  private final ApplicationDateValidator applicationDateValidator = new ApplicationDateValidator();

  private Errors errors;

  @BeforeEach
  void setUp() {
    errors = mock(Errors.class);
  }

  @Test
  void nullOrEmptyDateShouldThrowException() {
    // When
    final var exception = assertThrows(InvalidDateException.class, () -> applicationDateValidator.validate("", errors));

    // Then
    assertEquals("date cannot be null", exception.getMessage());
    assertEquals("date is null", exception.getDescription());
  }

  @Test
  void invalidFormatShouldThrowException() {
    // When
    final var exception = assertThrows(InvalidDateException.class, () -> applicationDateValidator.validate("2025-03-29T12:00:00", errors));

    // Then
    assertEquals("date format must follow: yyyy-MM-dd HH:mm:ss", exception.getMessage());
    assertEquals("invalid date format", exception.getDescription());
  }

  @Test
  void invalidDateValueShouldThrowException() {
    // When
    final var exception = assertThrows(InvalidDateException.class, () -> applicationDateValidator.validate("2025-02-30 12:00:00", errors));

    // Then
    assertEquals("a valid value must be used", exception.getMessage());
    assertEquals("invalid date value", exception.getDescription());
  }

  @Test
  void validDateShouldNotThrowException() {
    // When & Then
    assertDoesNotThrow(() -> applicationDateValidator.validate("2025-03-29 12:00:00", errors));
  }
}
