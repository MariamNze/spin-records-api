package com.gretacvdl.spin_records_api.dtos;

import jakarta.validation.constraints.*;
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

    @Email(message = "Email client invalide")
    @NotBlank(message = "Email client obligatoire")
    private String customerEmail;

    @NotNull(message = "Le total est obligatoire")
    @Positive(message = "Le total doit être supérieur à 0")
    @Digits(integer = 8, fraction = 2, message = "Le total doit avoir au maximum 8 chiffres avant la virgule et 2 après")
    private BigDecimal total;

    private LocalDateTime createdAt;

    @NotEmpty(message = "La commande doit contenir au moins un article")
    private List<OrderItemDto> items;
}