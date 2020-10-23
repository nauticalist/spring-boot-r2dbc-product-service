package io.seanapse.app.products.service.query.impl;

import io.seanapse.app.products.domain.entity.Product;
import io.seanapse.app.products.domain.repository.ProductRepository;
import io.seanapse.app.products.infrastructure.exception.exception.ResourceNotFoundException;
import io.seanapse.app.products.service.query.ProductQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductQueryServiceImpl implements ProductQueryService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductQueryServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Flux<Product> getProducts() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        return productRepository.findAll(sort);
    }

    @Override
    public Mono<Product> getProduct(String productId) throws ResourceNotFoundException {
        return productRepository.findByProductId(productId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ResourceNotFoundException("[PRODUCTCOMMDANDSERVICE] Product not found for productId :" + productId))));
    }
}
