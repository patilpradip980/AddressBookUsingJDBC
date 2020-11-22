package com.bridgelabz.AddressBook;

public class InvalidException extends Exception {

    enum ExceptionType {
       SQL_EXCEPTION
    }

    ExceptionType type;

    public InvalidException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
