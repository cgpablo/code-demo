package com.cgpablo.prices.infrastructure.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRICES")
public class PriceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "PRICE_ID")
  private UUID priceId;

  @Column(name = "BRAND_ID", nullable = false)
  private Long brandId;

  @Column(name = "START_DATE", nullable = false)
  private LocalDateTime startDate;

  @Column(name = "END_DATE", nullable = false)
  private LocalDateTime endDate;

  @Column(name = "PRICE_LIST", nullable = false)
  private Long priceList;

  @Column(name = "PRODUCT_ID", nullable = false)
  private Long productId;

  @Column(name = "PRIORITY", nullable = false)
  private Integer priority;

  @Column(name = "PRICE", nullable = false)
  private BigDecimal amount;

  @Column(name = "CURR", nullable = false)
  private String currency;
}
