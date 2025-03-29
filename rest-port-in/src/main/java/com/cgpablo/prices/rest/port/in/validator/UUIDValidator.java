package com.cgpablo.prices.rest.port.in.validator;

import java.util.UUID;

import com.cgpablo.prices.rest.port.in.exception.InvalidUUIDException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UUIDValidator implements Validator {

  @Override
  public boolean supports(final Class<?> clazz) {
    return String.class.equals(clazz);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    final String uuidString = (String) target;

    if (StringUtils.isBlank(uuidString)) {
      throw new InvalidUUIDException("UUID cannot be null or empty", "uuid is null or empty");
    }

    final boolean isValidUUID = uuidString.matches("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$");

    if (!isValidUUID) {
      throw new InvalidUUIDException("invalid UUID format", "invalid uuid format");
    }
  }
}