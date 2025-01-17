package com.alshadows.product.services;

import com.alshadows.product.dto.product.ProductDTO;
import com.alshadows.product.dto.product.ProductResponse;
import com.alshadows.product.exceptions.ProductException;
import com.alshadows.product.mapper.ProductMapper;
import com.alshadows.product.model.Product;
import com.alshadows.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductDTO productDto) {
        log.info("Creating new product with name: {}", productDto.getName());

        // Vérifier si le produit existe déjà
        if (productRepository.existsByName(productDto.getName())) {
            throw new ProductException(
                    "Un produit avec le nom " + productDto.getName() + " existe déjà",
                    "PRODUCT_ALREADY_EXISTS"
            );
        }

        // Convertir DTO en entité et sauvegarder
        Product product = productMapper.toEntity(productDto);
        Product savedProduct = productRepository.save(product);

        log.info("Product created successfully with id: {}", savedProduct.getId());
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse getProduct(Long id) {
        log.info("Fetching product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException(
                        "Produit non trouvé avec l'id: " + id,
                        "PRODUCT_NOT_FOUND"
                ));

        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductDTO productDto) {
        log.info("Updating product with id: {}", id);

        // Vérifier si le produit existe
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductException(
                        "Produit non trouvé avec l'id: " + id,
                        "PRODUCT_NOT_FOUND"
                ));

        // Vérifier si le nouveau nom n'est pas déjà utilisé par un autre produit
        if (!existingProduct.getName().equals(productDto.getName()) &&
                productRepository.existsByName(productDto.getName())) {
            throw new ProductException(
                    "Un produit avec le nom " + productDto.getName() + " existe déjà",
                    "PRODUCT_NAME_ALREADY_EXISTS"
            );
        }

        // Mettre à jour le produit
        Product updatedProduct = productMapper.updateEntityFromDto(productDto, existingProduct);
        Product savedProduct = productRepository.save(updatedProduct);

        log.info("Product updated successfully with id: {}", savedProduct.getId());
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);

        // Vérifier si le produit existe
        if (!productRepository.existsById(id)) {
            throw new ProductException(
                    "Produit non trouvé avec l'id: " + id,
                    "PRODUCT_NOT_FOUND"
            );
        }

        productRepository.deleteById(id);
        log.info("Product deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        log.info("Fetching products page: {}", pageable.getPageNumber());

        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String searchTerm, Pageable pageable) {
        log.info("Searching products with term: {}", searchTerm);

        Page<Product> productPage = productRepository.search(searchTerm, pageable);
        return productPage.map(productMapper::toResponse);
    }
}