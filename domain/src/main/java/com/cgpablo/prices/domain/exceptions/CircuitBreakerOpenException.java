package com.cgpablo.prices.domain.exceptions;

public class CircuitBreakerOpenException extends RuntimeException {

  public CircuitBreakerOpenException(final String message) {
    super(message);
  }
}
