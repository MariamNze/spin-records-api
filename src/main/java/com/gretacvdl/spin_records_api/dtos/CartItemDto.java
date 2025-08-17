package com.gretacvdl.spin_records_api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemDto {

    @NotNull
    private Long productId;

    @Min(value = 1, message = "La quantité minimale : 1")
    private int quantity;
}