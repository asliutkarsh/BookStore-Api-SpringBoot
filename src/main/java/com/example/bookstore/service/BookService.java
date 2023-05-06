package com.example.bookstore.service;

import com.example.bookstore.dto.BookDTO;

import java.util.List;

public interface BookService {

    void add_book(BookDTO bookDTO);
    void update_book(Long id,BookDTO bookDTO);
    void delete_book(Long id);
    BookDTO get_book(Long id);
    List<BookDTO> get_all_books();
    List<BookDTO> get_books_with_filter(String author,String name);

}
