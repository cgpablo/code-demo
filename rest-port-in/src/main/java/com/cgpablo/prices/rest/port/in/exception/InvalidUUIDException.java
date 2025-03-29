package com.cgpablo.prices.rest.port.in.exception;

import lombok.Getter;

@Getter
public class InvalidUUIDException extends IllegalArgumentException {

  private final String description;

  public InvalidUUIDException(final String message, final String description) {
    super(message);
    this.description = description;
  }
}