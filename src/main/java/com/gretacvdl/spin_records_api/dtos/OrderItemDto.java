package com.gretacvdl.spin_records_api.dtos;

import jakarta.validation.constraints.*;
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

    private Long id;

    private Long orderId;

    private Long productId;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantity;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @Positive(message = "Le prix unitaire doit être supérieur à 0")
    @Digits(integer = 6, fraction = 2, message = "Le prix unitaire doit avoir au maximum 6 chiffres avant la virgule et 2 après")
    private BigDecimal unitPrice;

    @NotBlank(message = "Le titre du produit est obligatoire")
    private String productTitle;
}