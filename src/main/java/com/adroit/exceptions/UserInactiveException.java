package com.adroit.exceptions;

public class UserInactiveException extends RuntimeException {
    public UserInactiveException(String message) {
        super(message); // Pass the message to the parent constructor
    }
}

