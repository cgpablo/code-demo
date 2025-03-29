package com.cgpablo.prices.rest.port.in.exception;

import lombok.Getter;

@Getter
public class InvalidDateException extends IllegalArgumentException {

  private final String description;

  public InvalidDateException(final String message, final String description) {
    super(message);
    this.description = description;
  }
}
