package com.alshadows.product.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private String errorCode;
    private String message;
    private T data;
    private Map<String, String> links;


}
