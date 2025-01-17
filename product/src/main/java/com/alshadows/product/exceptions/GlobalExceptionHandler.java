package com.alshadows.product.exceptions;


import com.alshadows.product.common.ApiResponse;
import com.alshadows.product.common.StatusConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ApiResponse<?>> handleProductException(ProductException ex) {
        logger.error("Product error: {}", ex.getMessage(), ex);
        ApiResponse<?> response = new ApiResponse<>(
                StatusConstants.REQUEST_FAILURE_STATUS,
                ex.getErrorCode(),
                ex.getMessage(),
                null,
                null
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        ApiResponse<?> response = new ApiResponse<>(
                StatusConstants.REQUEST_FAILURE_STATUS,
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                null,
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
