package io.seanapse.app.products.service.command;

import io.seanapse.app.products.domain.entity.Product;
import io.seanapse.app.products.infrastructure.exception.exception.ResourceNotFoundException;
import reactor.core.publisher.Mono;

public interface ProductCommandService {
    Mono<Long> createProduct(Product product);
    Mono<Product> updateProduct(Product product);
    void deleteProduct(Product product);
}
