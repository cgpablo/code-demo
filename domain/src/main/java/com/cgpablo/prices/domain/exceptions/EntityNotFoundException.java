package com.cgpablo.prices.domain.exceptions;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(final String message) {
    super(message);
  }
}
