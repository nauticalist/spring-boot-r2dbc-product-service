package io.seanapse.app.products.api.mapper;

import io.seanapse.app.products.api.dto.ProductRequestDTO;
import io.seanapse.app.products.api.dto.ProductResponseDTO;
import io.seanapse.app.products.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "modifiedAt", ignore = true),
    })
    Product mapProductRequestDTOtoProduct(ProductRequestDTO productRequestDTO);

    ProductResponseDTO mapProductToProductResponseDTO(Product product);
}
