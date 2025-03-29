package com.cgpablo.prices.rest.port.in.validator;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.function.Predicate;

import com.cgpablo.prices.rest.port.in.exception.InvalidDateException;
import org.instancio.internal.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ApplicationDateValidator implements Validator {

  private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

  @Override
  public boolean supports(final Class<?> clazz) {
    return String.class.equals(clazz);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    final var applicationDate = (String) target;

    if (StringUtils.isBlank(applicationDate)) {
      throw new InvalidDateException("date cannot be null", "date is null");
    }

    final Predicate<String> matchesFormat = date ->
        date.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");

    final Predicate<String> isValidDate = date -> {
      final var parts = date.split(" ");
      return validateDate(parts[0]) && validateTime(parts[1]);
    };

    if (!matchesFormat.test(applicationDate)) {
      throw new InvalidDateException("date format must follow: " + DATE_FORMAT, "invalid date format");
    }

    if (!isValidDate.test(applicationDate)) {
      throw new InvalidDateException("a valid value must be used", "invalid date value");
    }
  }

  private boolean validateDate(final String dateStr) {
    final var dateParts = dateStr.split("-");

    final var year = Integer.parseInt(dateParts[0]);
    final var month = Integer.parseInt(dateParts[1]) - 1;
    final var day = Integer.parseInt(dateParts[2]);

    Calendar calendar = Calendar.getInstance();
    calendar.setLenient(false);
    calendar.set(year, month, day);

    try {
      calendar.getTime();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean validateTime(final String timeStr) {
    final var timeParts = timeStr.split(":");

    final var hours = Integer.parseInt(timeParts[0]);
    final var minutes = Integer.parseInt(timeParts[1]);
    final var seconds = Integer.parseInt(timeParts[2]);

    return hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59 && seconds >= 0 && seconds <= 59;
  }
}
