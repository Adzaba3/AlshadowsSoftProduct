package com.alshadows.product.mapper;

import com.alshadows.product.dto.product.ProductDTO;
import com.alshadows.product.dto.product.ProductResponse;
import com.alshadows.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Product toEntity(ProductDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Product updateEntityFromDto(ProductDTO dto, @MappingTarget Product entity);

    ProductResponse toResponse(Product entity);
}