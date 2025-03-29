package com.cgpablo.prices.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.cgpablo.prices.infrastructure.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceJPARepository extends JpaRepository<PriceEntity, Long> {

  @Query("""
          SELECT price
          FROM PriceEntity price
          WHERE price.priceId = :priceId
      """)
  Optional<PriceEntity> findByPriceId(final UUID priceId);

  @Query("""
          SELECT price
          FROM PriceEntity price
          WHERE price.brandId = :brandId
              AND price.productId = :productId
              AND :applicationDate BETWEEN price.startDate AND price.endDate
          ORDER BY price.priority DESC
          LIMIT 1
      """)
  Optional<PriceEntity> findByFilter(final Long brandId, final Long productId, final LocalDateTime applicationDate);
}
