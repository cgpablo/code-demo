package com.cgpablo.prices.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price {

  private UUID priceId;

  private Long brandId;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  private Long priceList;

  private Long productId;

  private Integer priority;

  private BigDecimal amount;

  private String currency;
}
