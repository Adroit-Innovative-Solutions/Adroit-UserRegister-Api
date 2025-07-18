package com.adroit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String combinedErrors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String field = ((FieldError) error).getField();
                    return "Invalid value for field: " + field;
                })
                .collect(Collectors.joining(", "));

        ErrorResponse.ErrorDto error = new ErrorResponse.ErrorDto(300, combinedErrors);
        ErrorResponse errorResponse = new ErrorResponse(false, "Validation error", new ArrayList<>(), error);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserInactiveException.class)
    public ResponseEntity<ErrorResponse> handleUserInactiveException(UserInactiveException ex) {
        ErrorResponse.ErrorDto error = new ErrorResponse.ErrorDto(403, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                false,
                "Access denied: Inactive user",
                new ArrayList<>(),
                error
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserException(InvalidUserException ex) {
        ErrorResponse.ErrorDto error = new ErrorResponse.ErrorDto(404, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "User not found", new ArrayList<>(), error);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeAlreadyLoggedInException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeAlreadyLoggedIn(EmployeeAlreadyLoggedInException ex) {
        String message = "Employee " + ex.getEmployeeId() + " has already logged in today.";
        ErrorResponse.ErrorDto error = new ErrorResponse.ErrorDto(300, message);
        ErrorResponse errorResponse = new ErrorResponse(false, "Validation failed", new ArrayList<>(), error);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordMismatchException(PasswordMismatchException e) {
        ErrorResponse.ErrorDto error = new ErrorResponse.ErrorDto(300, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "Password confirmation does not match", new ArrayList<>(), error);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException e) {
        ErrorResponse.ErrorDto error = new ErrorResponse.ErrorDto(300, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "Invalid credentials", new ArrayList<>(), error);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyLoggedInException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyLoggedIn(UserAlreadyLoggedInException e) {
        ErrorResponse.ErrorDto error = new ErrorResponse.ErrorDto(201, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "User already logged in", new ArrayList<>(), error);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
        ErrorResponse.ErrorDto error = new ErrorResponse.ErrorDto(404, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "User not found", new ArrayList<>(), error);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        ErrorResponse.ErrorDto error = new ErrorResponse.ErrorDto(409, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "Email Already Exists", new ArrayList<>(), error);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        ErrorResponse.ErrorDto error = new ErrorResponse.ErrorDto(409, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "User Already Exists", new ArrayList<>(), error);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse.ErrorDto error = new ErrorResponse.ErrorDto(500, "An unexpected error occurred: " + e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "Unexpected error", new ArrayList<>(), error);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
