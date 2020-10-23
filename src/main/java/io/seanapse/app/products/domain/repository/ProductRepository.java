package io.seanapse.app.products.domain.repository;

import io.seanapse.app.products.domain.entity.Product;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveSortingRepository<Product, Long> {
    Mono<Product> findByProductId(String productId);
}
