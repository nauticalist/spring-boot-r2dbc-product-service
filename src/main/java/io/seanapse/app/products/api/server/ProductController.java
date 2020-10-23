package io.seanapse.app.products.api.server;

import io.seanapse.app.products.api.dto.ProductRequestDTO;
import io.seanapse.app.products.api.dto.ProductResponseDTO;
import io.seanapse.app.products.api.dto.ResourceIdentity;
import io.seanapse.app.products.api.mapper.ProductMapper;
import io.seanapse.app.products.domain.entity.Product;
import io.seanapse.app.products.infrastructure.exception.exception.FileStorageException;
import io.seanapse.app.products.infrastructure.exception.exception.ResourceNotFoundException;
import io.seanapse.app.products.service.command.FileStorageService;
import io.seanapse.app.products.service.command.ProductCommandService;
import io.seanapse.app.products.service.query.ProductQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private final FileStorageService fileStorageService;
    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    private final ProductMapper productMapper;

    @Autowired
    public ProductController(FileStorageService fileStorageService, ProductCommandService productCommandService, ProductQueryService productQueryService, ProductMapper productMapper) {
        this.fileStorageService = fileStorageService;
        this.productCommandService = productCommandService;
        this.productQueryService = productQueryService;
        this.productMapper = productMapper;
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<ResponseEntity<ResourceIdentity>> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        Product product = productMapper.mapProductRequestDTOtoProduct(productRequestDTO);
        Mono<Long> productId = productCommandService.createProduct(product);
        return productId.map(
                value -> ResponseEntity.status(HttpStatus.CREATED).body(new ResourceIdentity(value)));
    }

    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    public Flux<ProductResponseDTO> getProducts() {
        return productQueryService.getProducts()
                .map(productMapper::mapProductToProductResponseDTO)
                .doOnError(ex -> LOG.warn("[PRODUCTCONTROLLER] get all products request failed with error: " + ex.toString()))
                .log();
    }

    @GetMapping(value = "/{productId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<ResponseEntity<ProductResponseDTO>> getProduct(@PathVariable String productId) throws ResourceNotFoundException {
        return productQueryService.getProduct(productId)
                .map(productMapper::mapProductToProductResponseDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnError(ex -> LOG.warn("[PRODUCTCONTROLLER] get product by Id failed " + productId + ". Error: " + ex.toString()))
                .log();
    }

    @PutMapping(value = "/{productId}")
    @ResponseStatus(value = HttpStatus.OK)
    public  Mono<ResponseEntity<ProductResponseDTO>> updateProduct(@PathVariable String productId, @Valid @RequestBody ProductRequestDTO productRequestDTO) throws ResourceNotFoundException{
        try {
            return productQueryService.getProduct(productId)
                    .flatMap(product -> productCommandService.updateProduct(productMapper.mapProductRequestDTOtoProduct(productRequestDTO)))
                    .map(updatedProduct -> ResponseEntity.ok().body(productMapper.mapProductToProductResponseDTO(updatedProduct)))
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            LOG.warn("Update product command failed to execute {}", e.toString());
            throw e;
        }
    }

    @DeleteMapping(value = "/{productId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable String productId) throws ResourceNotFoundException {
        return productQueryService.getProduct(productId)
                .flatMap(product -> {
                    productCommandService.deleteProduct(product);
                    return Mono.just(new ResponseEntity<Void>(HttpStatus.OK));
                })
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/{productId}/image")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<ResponseEntity<Void>> uploadProductImage(@PathVariable String productId, @RequestPart FilePart filePart) throws ResourceNotFoundException {
        return productQueryService.getProduct(productId)
                .flatMap(product -> {
                    try {
                        fileStorageService.storeFile(filePart).block();
                    } catch (FileStorageException e) {
                        LOG.warn("[PRODUCTCONTROLLER] Uploading product image failed. Error: {}", e.toString());
                    }
                    return Mono.just(new ResponseEntity<Void>(HttpStatus.OK));
                })
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
