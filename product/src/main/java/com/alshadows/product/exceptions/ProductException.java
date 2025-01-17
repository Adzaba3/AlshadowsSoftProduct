package com.alshadows.product.exceptions;

import lombok.Getter;

@Getter
public class ProductException extends RuntimeException {
    private final String errorCode;

    public ProductException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
