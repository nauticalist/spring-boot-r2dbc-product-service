package io.seanapse.app.products.api.dto;

import io.seanapse.app.products.domain.entity.Category;
import io.seanapse.app.products.infrastructure.validation.IEnumValidator;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ProductResponseDTO {
    private Long id;

    private String sku;

    private String name;

    private String description;

    private String category;

    private Double price;

    private Integer inventory;
}
