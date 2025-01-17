package com.alshadows.product.services;

import com.alshadows.product.dto.product.ProductDTO;
import com.alshadows.product.dto.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse createProduct(ProductDTO productDto);
    ProductResponse getProduct(Long id);
    ProductResponse updateProduct(Long id, ProductDTO productDto);
    void deleteProduct(Long id);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    Page<ProductResponse> searchProducts(String searchTerm, Pageable pageable);
}
