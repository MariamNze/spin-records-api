package com.gretacvdl.spin_records_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto {

    private Long id;
    private String title;
    private String artist;
    private String genre;
    private Integer releaseYear;
    private String label;
    private BigDecimal price;
    private Integer stock;
    private String coverUrl;
    private String description;
}