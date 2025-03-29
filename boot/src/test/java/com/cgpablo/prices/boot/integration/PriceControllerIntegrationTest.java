package com.cgpablo.prices.boot.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PriceControllerIntegrationTest {

  public static final String URI_TEMPLATE = "/prices";

  private static final String BRAND_ID = "1";

  private static final String PRODUCT_ID = "35455";

  @LocalServerPort
  private int port;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldGetApplicablePrice_at_10_on_day_14() throws Exception {
    // Given
    final var applicationDate = getApplicationDate(14, 10);

    // When & Then
    mockMvc.perform(get(URI_TEMPLATE)
            .queryParam("brandId", BRAND_ID)
            .queryParam("productId", PRODUCT_ID)
            .queryParam("applicationDate", applicationDate)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.applicablePrice.brandId").value(1))
        .andExpect(jsonPath("$.applicablePrice.productId").value(35455))
        .andExpect(jsonPath("$.applicablePrice.startDate").value("2020-06-14 00:00:00"))
        .andExpect(jsonPath("$.applicablePrice.endDate").value("2020-12-31 23:59:59"))
        .andExpect(jsonPath("$.applicablePrice.amount").value(35.50));
  }

  @Test
  void shouldGetApplicablePrice_at_16_on_day_14() throws Exception {
    // Given
    final var applicationDate = getApplicationDate(14, 16);

    // When & Then
    mockMvc.perform(get(URI_TEMPLATE)
            .queryParam("brandId", BRAND_ID)
            .queryParam("productId", PRODUCT_ID)
            .queryParam("applicationDate", applicationDate))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.applicablePrice.brandId").value(1))
        .andExpect(jsonPath("$.applicablePrice.productId").value(35455))
        .andExpect(jsonPath("$.applicablePrice.startDate").value("2020-06-14 15:00:00"))
        .andExpect(jsonPath("$.applicablePrice.endDate").value("2020-06-14 18:30:00"))
        .andExpect(jsonPath("$.applicablePrice.amount").value(25.45));
  }

  @Test
  void shouldGetApplicablePrice_at_21_on_day_14() throws Exception {
    // Given
    final var applicationDate = getApplicationDate(14, 21);

    // When & Then
    mockMvc.perform(get(URI_TEMPLATE)
            .queryParam("brandId", BRAND_ID)
            .queryParam("productId", PRODUCT_ID)
            .queryParam("applicationDate", applicationDate))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.applicablePrice.brandId").value(1))
        .andExpect(jsonPath("$.applicablePrice.productId").value(35455))
        .andExpect(jsonPath("$.applicablePrice.startDate").value("2020-06-14 00:00:00"))
        .andExpect(jsonPath("$.applicablePrice.endDate").value("2020-12-31 23:59:59"))
        .andExpect(jsonPath("$.applicablePrice.amount").value(35.50));
  }

  @Test
  void shouldGetApplicablePrice_at_10_on_day_15() throws Exception {
    // Given
    final var applicationDate = getApplicationDate(15, 10);

    // When & Then
    mockMvc.perform(get(URI_TEMPLATE)
            .queryParam("brandId", BRAND_ID)
            .queryParam("productId", PRODUCT_ID)
            .queryParam("applicationDate", applicationDate))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.applicablePrice.brandId").value(1))
        .andExpect(jsonPath("$.applicablePrice.productId").value(35455))
        .andExpect(jsonPath("$.applicablePrice.startDate").value("2020-06-15 00:00:00"))
        .andExpect(jsonPath("$.applicablePrice.endDate").value("2020-06-15 11:00:00"))
        .andExpect(jsonPath("$.applicablePrice.amount").value(30.5));
  }

  @Test
  void shouldGetApplicablePrice_at_21_on_day_16() throws Exception {
    // Given
    final var applicationDate = getApplicationDate(16, 21);

    // When & Then
    mockMvc.perform(get(URI_TEMPLATE)
            .queryParam("brandId", BRAND_ID)
            .queryParam("productId", PRODUCT_ID)
            .queryParam("applicationDate", applicationDate))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.applicablePrice.brandId").value(1))
        .andExpect(jsonPath("$.applicablePrice.productId").value(35455))
        .andExpect(jsonPath("$.applicablePrice.startDate").value("2020-06-15 16:00:00"))
        .andExpect(jsonPath("$.applicablePrice.endDate").value("2020-12-31 23:59:59"))
        .andExpect(jsonPath("$.applicablePrice.amount").value(38.95));
  }

  @Test
  void shouldGetPriceById() throws Exception {
    // Given
    final var priceId = "19372cf6-0c3a-43f5-891e-1f25671da991";

    // When & Then
    mockMvc.perform(get(URI_TEMPLATE + "/{priceId}", priceId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.price.priceId").value(priceId))
        .andExpect(jsonPath("$.price.brandId").value(1))
        .andExpect(jsonPath("$.price.productId").value(35455))
        .andExpect(jsonPath("$.price.startDate").value("2020-06-14 00:00:00"))
        .andExpect(jsonPath("$.price.endDate").value("2020-12-31 23:59:59"))
        .andExpect(jsonPath("$.price.amount").value(35.50));
  }

  private String getApplicationDate(final int day, final int hour) {
    final var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.of(2020, 6, day, hour, 0, 0)
        .format(formatter);
  }
}
