package com.hes.account.exception;

public class AccountException extends Exception {
    private String message;

    public AccountException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
