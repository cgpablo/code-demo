package com.cgpablo.prices.rest.port.in.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.cgpablo.prices.rest.port.in.exception.InvalidUUIDException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.Errors;

class UUIDValidatorTest {

  private final UUIDValidator uuidValidator = new UUIDValidator();

  private Errors errors;

  @BeforeEach
  void setUp() {
    errors = mock(Errors.class);
  }

  @Test
  void testValidateEmptyUUID() {
    // When
    final var exception = assertThrows(InvalidUUIDException.class, () -> uuidValidator.validate("", errors));

    // Then
    assertEquals("UUID cannot be null or empty", exception.getMessage());
    assertEquals("uuid is null or empty", exception.getDescription());
  }

  @Test
  void testValidateInvalidUUIDFormat() {
    // When
    final var exception = assertThrows(InvalidUUIDException.class, () -> uuidValidator.validate("invalid-uuid", errors));

    // Then
    assertEquals("invalid UUID format", exception.getMessage());
    assertEquals("invalid uuid format", exception.getDescription());
  }

  @Test
  void testValidateValidUUID() {
    // When & Then
    assertDoesNotThrow(() -> uuidValidator.validate("123e4567-e89b-12d3-a456-426614174000", errors));
  }
}
