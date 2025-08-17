package com.gretacvdl.spin_records_api.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartRequestDto {

    @Email(message = "Email invalide")
    @NotBlank(message = "Email obligatoire")
    private String email;

    private String name;

    @NotEmpty(message = "La liste des articles ne peut être vide")
    @Valid
    private List<CartItemDto> items;
}