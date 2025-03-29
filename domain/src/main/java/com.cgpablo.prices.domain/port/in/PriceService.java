package com.cgpablo.prices.domain.port.in;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.cgpablo.prices.domain.model.Price;

public interface PriceService {

  /**
   * Retrieves the price for a given price id.
   *
   * @param priceId the price id
   * @return the price or empty if none matches the criteria
   */
  Price getPriceById(final UUID priceId);

  /**
   * Retrieves the applicable price for a given product and brand given a specific date. If there are multiple prices for the same product
   * and brand on the given date, retrieves the highest priority one.
   *
   * @param command contains the brand id, the product id and the application date of the price
   * @return the applicable price or empty if none matches the criteria
   */
  Price getApplicablePrice(final GetApplicablePriceCommand command);

  record GetApplicablePriceCommand(Long brandId, Long productId, LocalDateTime applicationDate) {

  }
}
