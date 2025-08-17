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
public class ProductDto {

    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotBlank(message = "L'artiste est obligatoire")
    private String artist;

    @NotBlank(message = "Le genre est obligatoire")
    private String genre;

    @NotNull(message = "L'année de sortie est obligatoire")
    @Min(value = 1948, message = "L'année de sortie doit être valide")
    private Integer releaseYear;

    @NotBlank(message = "Le label est obligatoire")
    private String label;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être supérieur à 0")
    @Digits(integer = 6, fraction = 2, message = "Le prix doit avoir au maximum 6 chiffres avant la virgule et 2 après")
    private BigDecimal price;

    @NotNull(message = "Le stock est obligatoire")
    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    private Integer stock;

    @NotBlank(message = "L'URL de la couverture est obligatoire")
    private String coverUrl;

    @NotBlank(message = "La description est obligatoire")
    private String description;
}