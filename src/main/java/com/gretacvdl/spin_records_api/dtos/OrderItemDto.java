package com.gretacvdl.spin_records_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemDto {

    private Long productId;
    private String title;
    private String artist;
    private Integer quantity;
    private BigDecimal unitPrice;
}