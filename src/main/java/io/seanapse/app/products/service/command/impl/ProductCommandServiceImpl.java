package io.seanapse.app.products.service.command.impl;

import io.seanapse.app.products.domain.entity.Product;
import io.seanapse.app.products.domain.event.ProductEvent;
import io.seanapse.app.products.domain.repository.ProductRepository;
import io.seanapse.app.products.infrastructure.exception.exception.ResourceNotFoundException;
import io.seanapse.app.products.service.command.ProductCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {
    private final ApplicationEventPublisher publisher;
    private final ProductRepository productRepository;

    @Autowired
    public ProductCommandServiceImpl(ApplicationEventPublisher publisher, ProductRepository productRepository) {
        this.publisher = publisher;
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Long> createProduct(Product product) {
        product.setCreatedAt(Instant.now());
        return productRepository.save(product)
                .doOnSuccess(item -> publishProductEvent(ProductEvent.PRODUCT_CREATED, item))
                .flatMap(item -> Mono.just(item.getId()));
    }

    @Override
    public void updateProduct(Product product) throws ResourceNotFoundException {
        Mono<Product> productMono = productRepository.findBySku(product.getSku())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ResourceNotFoundException("[PRODUCTCOMMDANDSERVICE] Product not found for sku :" + product.getSku()))));

        productMono.subscribe(
                value -> {
                    value.setName(product.getName());
                    value.setDescription(product.getDescription());
                    value.setPrice(product.getPrice());
                    value.setInventory(product.getInventory());
                    value.setModifiedAt(Instant.now());

                    productRepository.save(value)
                            .doOnSuccess(item -> publishProductEvent(ProductEvent.PRODUCT_UPDATED, item))
                            .subscribe();
                }
        );
    }

    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product).subscribe();
    }

    private void publishProductEvent(String eventType, Product product) {
        this.publisher.publishEvent(new ProductEvent(eventType, product));
    }
}
