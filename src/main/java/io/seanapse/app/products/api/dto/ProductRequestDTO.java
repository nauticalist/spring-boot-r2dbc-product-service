package io.seanapse.app.products.api.dto;

import io.seanapse.app.products.domain.entity.Category;
import io.seanapse.app.products.infrastructure.validation.IEnumValidator;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;


public class ProductRequestDTO {
    @NotNull
    @NotEmpty(message = "Product Id cannot be null or empty")
    private String productId;

    @NotEmpty(message = "Name cannot be null or empty")
    @NotNull
    private String name;

    @NotEmpty(message = "Description cannot be null or empty")
    @NotNull
    private String description;

    @NotNull
    @IEnumValidator(
            enumClazz = Category.class,
            message = "Invalid category provided"
    )
    private String category;

    @NotNull(message = "Price cannot be null or empty")
    @NotNull
    private Double price;

    @NotNull(message = "Inventory cannot be null or empty")
    @NotNull
    private Integer inventory;

    public ProductRequestDTO() {
    }

    public ProductRequestDTO(@NotNull @NotEmpty(message = "SKU cannot be null or empty") String productId, @NotEmpty(message = "Name cannot be null or empty") @NotNull String name, @NotEmpty(message = "Description cannot be null or empty") @NotNull String description, @NotNull String category, @NotNull(message = "Price cannot be null or empty") @NotNull Double price, @NotNull(message = "Inventory cannot be null or empty") @NotNull Integer inventory) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.inventory = inventory;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductRequestDTO that = (ProductRequestDTO) o;
        return Objects.equals(productId, that.productId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, category);
    }

    @Override
    public String toString() {
        return "ProductRequestDTO{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", inventory=" + inventory +
                '}';
    }
}
