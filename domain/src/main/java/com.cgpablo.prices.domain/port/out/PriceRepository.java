package com.cgpablo.prices.domain.port.out;

import java.util.Optional;
import java.util.UUID;

import com.cgpablo.prices.domain.model.Price;
import com.cgpablo.prices.domain.port.in.PriceService.GetApplicablePriceCommand;

public interface PriceRepository {

  /**
   * Retrieves the price for a given price id.
   *
   * @param priceId the price id
   * @return a price that meets the criteria
   */
  Optional<Price> findPriceById(final UUID priceId);

  /**
   * Retrieves the applicable price for a given product and brand given a specific date.
   *
   * @param command contains the brand id, the product id and the application date of the price
   * @return a list of prices that meet the criteria
   */
  Optional<Price> findPricesByFilter(final GetApplicablePriceCommand command);
}
