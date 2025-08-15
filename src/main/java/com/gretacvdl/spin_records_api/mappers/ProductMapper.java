package com.gretacvdl.spin_records_api.mappers;

import com.gretacvdl.spin_records_api.dtos.ProductDto;
import com.gretacvdl.spin_records_api.entities.Product;

public class ProductMapper {

    // Entity Product ==> DTO ProductDto
    public static ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getArtist(),
                product.getGenre(),
                product.getReleaseYear(),
                product.getLabel(),
                product.getPrice(),
                product.getStock(),
                product.getCoverUrl(),
                product.getDescription()
        );
    }

    // DTO ProductDto ==> Entity Product
    public static Product toEntity(ProductDto dto) {
        return new Product(
                dto.getId(),
                dto.getTitle(),
                dto.getArtist(),
                dto.getGenre(),
                dto.getReleaseYear(),
                dto.getLabel(),
                dto.getPrice(),
                dto.getStock(),
                dto.getCoverUrl(),
                dto.getDescription(),
                null // created_at
        );
    }
}