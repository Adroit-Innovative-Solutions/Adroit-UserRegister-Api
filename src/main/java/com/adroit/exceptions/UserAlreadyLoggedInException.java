package com.adroit.exceptions;


public class UserAlreadyLoggedInException extends RuntimeException{

    public UserAlreadyLoggedInException(String message) {
        super(message);
    }
}
