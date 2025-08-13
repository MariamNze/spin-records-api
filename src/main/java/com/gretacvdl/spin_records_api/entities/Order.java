package com.gretacvdl.spin_records_api.entities;

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
public class Order {

    private Long id;
    private Long customerId;
    private BigDecimal total;
    private LocalDateTime createdAt;
}