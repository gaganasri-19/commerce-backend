package com.product.commerce.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
//unchecked exception means no need to catch or declare it or handle it explicitly
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
