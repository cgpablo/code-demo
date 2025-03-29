package com.cgpablo.prices.rest.port.in.exception.handler;

import java.util.List;

import com.cgpablo.prices.domain.exceptions.CircuitBreakerOpenException;
import com.cgpablo.prices.domain.exceptions.EntityNotFoundException;
import com.cgpablo.prices.rest.port.in.exception.InvalidDateException;
import com.cgpablo.prices.rest.port.in.exception.InvalidUUIDException;
import com.cgpablo.prices.rest.port.in.model.ErrorDTO;
import com.cgpablo.prices.rest.port.in.model.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PriceExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleEntityNotFound(final EntityNotFoundException ex) {

    final ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setCode("NOT_FOUND");
    errorDTO.setDescription("price not found");
    errorDTO.setDetails(List.of(ex.getMessage()));

    final ErrorResponseDTO errorResponse = new ErrorResponseDTO();
    errorResponse.setError(errorDTO);

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(InvalidDateException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidDateException(final InvalidDateException ex) {

    final ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setCode("BAD_REQUEST");
    errorDTO.setDescription(ex.getDescription());
    errorDTO.setDetails(List.of(ex.getMessage()));

    final ErrorResponseDTO errorResponse = new ErrorResponseDTO();
    errorResponse.setError(errorDTO);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(InvalidUUIDException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidUUIDException(final InvalidUUIDException ex) {

    final ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setCode("BAD_REQUEST");
    errorDTO.setDescription(ex.getDescription());
    errorDTO.setDetails(List.of(ex.getMessage()));

    final ErrorResponseDTO errorResponse = new ErrorResponseDTO();
    errorResponse.setError(errorDTO);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(CircuitBreakerOpenException.class)
  public ResponseEntity<ErrorResponseDTO> handleCircuitBreakerOpenException(final CircuitBreakerOpenException ex) {

    final ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setCode("SERVICE_UNAVAILABLE");
    errorDTO.setDescription("CircuitBreaker open");
    errorDTO.setDetails(List.of(ex.getMessage()));

    final ErrorResponseDTO errorResponse = new ErrorResponseDTO();
    errorResponse.setError(errorDTO);

    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDTO> handleGeneralException(final Exception ex) {

    final ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setCode("INTERNAL_ERROR");
    errorDTO.setDescription("Unexpected error occurred");
    errorDTO.setDetails(List.of(ex.getMessage()));

    final ErrorResponseDTO errorResponse = new ErrorResponseDTO();
    errorResponse.setError(errorDTO);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
