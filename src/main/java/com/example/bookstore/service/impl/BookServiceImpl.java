package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.BookExistsException;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void add_book(BookDTO bookDTO) {
        if (bookRepository.existsByIsbn(bookDTO.getIsbn())){
            throw new BookExistsException();
        }
        var book = new Book(bookDTO);
        bookRepository.save(book);

        log.info("Book Created");
    }

    @Override
    public void update_book(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        if (bookRepository.existsByIsbn(bookDTO.getIsbn())){
            throw new BookExistsException();
        }
        book = book.update(bookDTO);
        bookRepository.save(book);
        log.info("Book Updated: {}",book);
    }

    @Override
    public BookDTO get_book(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return book.toDTO();
    }

    @Override
    public List<BookDTO> get_all_books() {
        return bookRepository.findAll()
                .stream().map(Book::toDTO)
                .toList();
    }

    @Override
    public List<BookDTO> get_books_with_filter(String author, String name) {
        return bookRepository.findAllByAuthorContainingAndNameContaining(author,name)
                .stream().map(Book::toDTO)
                .toList();
    }


}
