package com.alshadows.product.controller;

import com.alshadows.product.common.ApiResponse;
import com.alshadows.product.common.StatusConstants;
import com.alshadows.product.dto.product.ProductDTO;
import com.alshadows.product.dto.product.ProductResponse;
import com.alshadows.product.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller pour gérer les produits.
 * Expose des endpoints REST pour les opérations CRUD et les recherches sur les produits.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    // Service pour effectuer les opérations sur les produits.
    private final ProductService productService;

    /**
     * Crée un nouveau produit.
     *
     * @param productDto Les données du produit à créer, validées via @Valid.
     * @return Une réponse contenant les détails du produit créé.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductDTO productDto) {
        log.info("Creating new product with name: {}", productDto.getName());

        ProductResponse createdProduct = productService.createProduct(productDto);

        // Génère des liens pour les actions possibles sur ce produit
        Map<String, String> links = new HashMap<>();
        links.put("self", "/api/v1/products/" + createdProduct.getId());
        links.put("update", "/api/v1/products/" + createdProduct.getId());
        links.put("delete", "/api/v1/products/" + createdProduct.getId());

        // Crée la réponse API
        ApiResponse<ProductResponse> response = new ApiResponse<>(
                StatusConstants.REQUEST_SUCCESS_STATUS,
                StatusConstants.PRODUCT_CREATION_SUCCESS,
                "Produit créé avec succès",
                createdProduct,
                links
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Récupère les détails d'un produit à partir de son ID.
     *
     * @param id L'identifiant du produit.
     * @return Une réponse contenant les détails du produit.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        log.info("Fetching product with id: {}", id);

        // Récupère le produit via le service
        ProductResponse product = productService.getProduct(id);

        // Génère des liens pour ce produit
        Map<String, String> links = new HashMap<>();
        links.put("self", "/api/v1/products/" + id);
        links.put("update", "/api/v1/products/" + id);
        links.put("delete", "/api/v1/products/" + id);

        // Crée la réponse API
        ApiResponse<ProductResponse> response = new ApiResponse<>(
                StatusConstants.REQUEST_SUCCESS_STATUS,
                StatusConstants.PRODUCT_FETCH_SUCCESS,
                "Produit récupéré avec succès",
                product,
                links
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Met à jour un produit existant.
     *
     * @param id         L'identifiant du produit à mettre à jour.
     * @param productDto Les nouvelles données du produit.
     * @return Une réponse contenant les détails du produit mis à jour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDto) {
        log.info("Updating product with id: {}", id);

        // Appelle le service pour mettre à jour le produit
        ProductResponse updatedProduct = productService.updateProduct(id, productDto);

        // Génère les liens
        Map<String, String> links = new HashMap<>();
        links.put("self", "/api/v1/products/" + id);
        links.put("delete", "/api/v1/products/" + id);

        // Crée la réponse API
        ApiResponse<ProductResponse> response = new ApiResponse<>(
                StatusConstants.REQUEST_SUCCESS_STATUS,
                StatusConstants.PRODUCT_UPDATE_SUCCESS,
                "Produit mis à jour avec succès",
                updatedProduct,
                links
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Supprime un produit à partir de son ID.
     *
     * @param id L'identifiant du produit.
     * @return Une réponse confirmant la suppression.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product with id: {}", id);

        // Appelle le service pour supprimer le produit
        productService.deleteProduct(id);

        // Génère les liens
        Map<String, String> links = new HashMap<>();
        links.put("products", "/api/v1/products");

        // Crée la réponse API
        ApiResponse<Void> response = new ApiResponse<>(
                StatusConstants.REQUEST_SUCCESS_STATUS,
                StatusConstants.PRODUCT_DELETE_SUCCESS,
                "Produit supprimé avec succès",
                null,
                links
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Récupère une liste paginée de produits.
     *
     * @param page Le numéro de la page (défaut : 0).
     * @param size La taille de la page (défaut : 10).
     * @param sort Le champ de tri (défaut : id).
     * @return Une réponse contenant une page de produits.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {
        log.info("Fetching products page: {}, size: {}, sort: {}", page, size, sort);

        // Définit la pagination et le tri
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<ProductResponse> products = productService.getAllProducts(pageable);

        // Génère les liens pour la navigation
        Map<String, String> links = new HashMap<>();
        links.put("self", "/api/v1/products");
        if (products.hasNext()) {
            links.put("next", "/api/v1/products?page=" + (page + 1) + "&size=" + size);
        }
        if (products.hasPrevious()) {
            links.put("prev", "/api/v1/products?page=" + (page - 1) + "&size=" + size);
        }

        // Crée la réponse API
        ApiResponse<Page<ProductResponse>> response = new ApiResponse<>(
                StatusConstants.REQUEST_SUCCESS_STATUS,
                StatusConstants.PRODUCT_FETCH_SUCCESS,
                "Produits récupérés avec succès",
                products,
                links
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Recherche des produits par terme de recherche.
     *
     * @param searchTerm Le terme à rechercher.
     * @param page       Le numéro de la page (défaut : 0).
     * @param size       La taille de la page (défaut : 10).
     * @return Une réponse contenant les résultats de recherche paginés.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProducts(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Searching products with term: {}, page: {}, size: {}", searchTerm, page, size);

        // Définit la pagination
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> products = productService.searchProducts(searchTerm, pageable);

        // Génère les liens pour la navigation
        Map<String, String> links = new HashMap<>();
        links.put("self", "/api/v1/products/search?searchTerm=" + searchTerm);
        if (products.hasNext()) {
            links.put("next", "/api/v1/products/search?searchTerm=" + searchTerm +
                    "&page=" + (page + 1) + "&size=" + size);
        }
        if (products.hasPrevious()) {
            links.put("prev", "/api/v1/products/search?searchTerm=" + searchTerm +
                    "&page=" + (page - 1) + "&size=" + size);
        }

        // Crée la réponse API
        ApiResponse<Page<ProductResponse>> response = new ApiResponse<>(
                StatusConstants.REQUEST_SUCCESS_STATUS,
                StatusConstants.PRODUCT_FETCH_SUCCESS,
                "Résultats de recherche récupérés avec succès",
                products,
                links
        );

        return ResponseEntity.ok(response);
    }
}
