package com.example.bookstore.exception;

public class BookExistsException extends RuntimeException{

    public BookExistsException() {
        super("Book with this ISBN already exists");
    }
}
