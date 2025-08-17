package com.gretacvdl.spin_records_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDto {

    private Long id;

    @Email(message = "Email invalide")
    @NotBlank(message = "Email obligatoire")
    private String email;

    private String name;
}