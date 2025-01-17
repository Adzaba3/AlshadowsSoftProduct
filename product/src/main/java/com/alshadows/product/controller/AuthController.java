package com.alshadows.product.controller;

import com.alshadows.product.common.ApiResponse;
import com.alshadows.product.common.StatusConstants;
import com.alshadows.product.dto.auth.LoginRequest;
import com.alshadows.product.dto.auth.LoginResponse;
import com.alshadows.product.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur pour la gestion de l'authentification.
 * Fournit des endpoints REST pour les opérations liées à l'authentification des utilisateurs.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    // Service de gestion de l'authentification
    private final AuthService authService;

    // Logger supplémentaire (optionnel, utilisé pour des besoins spécifiques)
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /**
     * Endpoint pour l'authentification des utilisateurs.
     *
     * @param loginRequest Les informations d'identification de l'utilisateur, validées via @Valid.
     * @return Une réponse contenant un jeton d'authentification et des informations sur l'utilisateur.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        // Log de l'intention de connexion
        log.info("Tentative de connexion pour l'utilisateur: {}", loginRequest.getUsername());

        // Appelle le service d'authentification pour vérifier les identifiants
        LoginResponse loginResponse = authService.authenticate(loginRequest);

        // Création des liens pour les ressources disponibles après connexion
        Map<String, String> links = new HashMap<>();
        links.put("products", "/api/v1/products"); // Lien vers les produits pour naviguer après login

        // Préparation de la réponse API
        ApiResponse<LoginResponse> response = new ApiResponse<>(
                StatusConstants.REQUEST_SUCCESS_STATUS, // Statut HTTP
                StatusConstants.AUTH_SUCCESS,          // Message de succès de l'authentification
                StatusConstants.AUTH_WIN,              // Message personnalisé pour la réponse
                loginResponse,                         // Contenu de la réponse (détails de l'utilisateur connecté)
                links                                  // Liens associés
        );

        // Retourne une réponse HTTP avec le statut OK et le corps de réponse
        return ResponseEntity.ok(response);
    }
}
