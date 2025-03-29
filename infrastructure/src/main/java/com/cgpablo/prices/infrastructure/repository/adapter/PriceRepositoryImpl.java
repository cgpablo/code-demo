package com.cgpablo.prices.infrastructure.repository.adapter;

import java.util.Optional;
import java.util.UUID;

import com.cgpablo.prices.domain.model.Price;
import com.cgpablo.prices.domain.port.in.PriceService.GetApplicablePriceCommand;
import com.cgpablo.prices.domain.port.out.PriceRepository;
import com.cgpablo.prices.infrastructure.mapper.PriceRepositoryMapper;
import com.cgpablo.prices.infrastructure.repository.PriceJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceRepositoryImpl implements PriceRepository {

  private final PriceJPARepository jpaRepository;

  private final PriceRepositoryMapper repositoryMapper;

  @Override
  public Optional<Price> findPriceById(final UUID priceId) {
    return jpaRepository.findByPriceId(priceId)
        .map(repositoryMapper::asPrice);
  }

  @Override
  public Optional<Price> findPricesByFilter(final GetApplicablePriceCommand command) {
    return jpaRepository.findByFilter(command.brandId(), command.productId(), command.applicationDate()).stream()
        .map(repositoryMapper::asPrice)
        .findFirst();
  }
}
