package com.cgpablo.prices.rest.port.in.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cgpablo.prices.rest.port.in.exception.handler.PriceExceptionHandler;
import com.cgpablo.prices.rest.port.in.mapper.PriceMapper;
import com.cgpablo.prices.rest.port.in.model.ApplicablePriceResponseDTO;
import com.cgpablo.prices.rest.port.in.model.PriceResponseDTO;
import com.cgpablo.prices.rest.port.in.validator.ApplicationDateValidator;
import com.cgpablo.prices.domain.exceptions.EntityNotFoundException;
import com.cgpablo.prices.domain.model.Price;
import com.cgpablo.prices.domain.port.in.PriceService;
import com.cgpablo.prices.domain.port.in.PriceService.GetApplicablePriceCommand;
import com.cgpablo.prices.rest.port.in.validator.UUIDValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PriceController.class)
@Import(PriceExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = PriceController.class)
class PriceControllerTest {

  private static final String URI_TEMPLATE = "/prices";

  private static final Long BRAND_ID = 1L;

  private static final Long PRODUCT_ID = 100L;

  private static final String APPLICATION_DATE = LocalDateTime.now().toString();

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ApplicationDateValidator applicationDateValidator;

  @MockitoBean
  private UUIDValidator uuidValidator;

  @MockitoBean
  private PriceService priceService;

  @MockitoBean
  private PriceMapper priceMapper;

  private PriceController priceController;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void shouldReturnApplicablePrice_whenPriceExists() throws Exception {
    // Given
    final var command = Instancio.create(GetApplicablePriceCommand.class);
    final var price = Instancio.create(Price.class);
    final var dto = Instancio.create(ApplicablePriceResponseDTO.class);

    when(priceMapper.asGetApplicablePriceCommand(BRAND_ID, PRODUCT_ID, APPLICATION_DATE)).thenReturn(command);
    when(priceService.getApplicablePrice(command)).thenReturn(price);
    when(priceMapper.asApplicablePriceResponseDTO(price)).thenReturn(dto);

    // When & Then
    mockMvc.perform(get(URI_TEMPLATE)
            .queryParam("brandId", String.valueOf(BRAND_ID))
            .queryParam("productId", String.valueOf(PRODUCT_ID))
            .queryParam("applicationDate", APPLICATION_DATE)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(dto)));

    verify(priceService).getApplicablePrice(command);
    verify(priceMapper).asApplicablePriceResponseDTO(price);
  }

  @Test
  void shouldReturnNotFound_whenPriceDoesNotExist() throws Exception {
    // Given
    final var command = Instancio.create(GetApplicablePriceCommand.class);

    when(priceMapper.asGetApplicablePriceCommand(BRAND_ID, PRODUCT_ID, APPLICATION_DATE)).thenReturn(command);
    when(priceService.getApplicablePrice(command)).thenThrow(new EntityNotFoundException(""));

    // When & Then
    mockMvc.perform(get(URI_TEMPLATE)
            .queryParam("brandId", String.valueOf(BRAND_ID))
            .queryParam("productId", String.valueOf(PRODUCT_ID))
            .queryParam("applicationDate", APPLICATION_DATE)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    verify(priceService).getApplicablePrice(command);
  }

  @Test
  void shouldReturnPriceById_whenPriceExists() throws Exception {
    // Given
    final var priceId = Instancio.create(UUID.class);
    final var price = Instancio.create(Price.class);
    final var dto = Instancio.create(PriceResponseDTO.class);

    when(priceService.getPriceById(priceId)).thenReturn(price);
    when(priceMapper.asPriceResponseDTO(price)).thenReturn(dto);

    // When & Then
    mockMvc.perform(get(URI_TEMPLATE + "/{priceId}", priceId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(dto)));

    verify(priceService).getPriceById(priceId);
    verify(priceMapper).asPriceResponseDTO(price);
  }

  @Test
  void shouldReturnNotFound_whenPriceIdDoesNotExist() throws Exception {
    // Given
    final var priceId = Instancio.create(UUID.class);
    when(priceService.getPriceById(priceId)).thenThrow(new EntityNotFoundException(""));

    // When & Then
    mockMvc.perform(get(URI_TEMPLATE + "/{priceId}", priceId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    verify(priceService).getPriceById(priceId);
  }
}
