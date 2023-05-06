package com.example.bookstore.exception;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException() {
        super("This book was not found");
    }
}
