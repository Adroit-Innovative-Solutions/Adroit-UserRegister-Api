package com.adroit.exceptions;


import java.util.List;

public class ErrorResponse {

    private boolean success;
    private String message;
    private List data;
    private ErrorDto error; ;


    public static class ErrorDto{

        private int errorCode;
        private String errorMessage;


        public ErrorDto() {
        }

        public ErrorDto(int errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    public ErrorResponse() {

    }

    public ErrorResponse(boolean success, String message, List data, ErrorDto error) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public ErrorDto getError() {
        return error;
    }

    public void setError(ErrorDto error) {
        this.error = error;
    }
}
