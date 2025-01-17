package com.alshadows.product.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Le nom d'utilisateur est requis")
    private String username;

    @NotBlank(message = "Le mot de passe est requis")
    private String password;
}