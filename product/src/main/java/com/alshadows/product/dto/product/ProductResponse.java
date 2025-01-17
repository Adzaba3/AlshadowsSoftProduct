package com.alshadows.product.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
}

