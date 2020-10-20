package io.seanapse.app.products.service.query;

import io.seanapse.app.products.domain.entity.Product;
import io.seanapse.app.products.infrastructure.exception.exception.ResourceNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductQueryService {
    Flux<Product> getProducts();

    Mono<Product> getProduct(String sku) throws ResourceNotFoundException;
}
