package com.gretacvdl.spin_records_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDto {

    private Long id;
    private String customerEmail;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;
}