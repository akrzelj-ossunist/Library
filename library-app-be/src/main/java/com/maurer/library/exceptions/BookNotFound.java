package com.maurer.library.exceptions;

import java.util.ResourceBundle;


public class BookNotFound extends Exception {
    public static final String ERROR_MESSAGE = "Book not found. ";
    public BookNotFound(String message) {
        super(ERROR_MESSAGE + message);
    }
}
