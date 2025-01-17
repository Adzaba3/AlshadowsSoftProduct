package com.alshadows.product.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Le nom est requis")
    private String name;

    @NotBlank(message = "La description est requise")
    private String description;

    @Min(value = 0, message = "Le prix doit être positif")
    private Double price;
}
