package com.assessment.maybank.error;

public class CustomConcurrencyException extends RuntimeException {

    public CustomConcurrencyException(String message) {
        super(message);
    }

}
