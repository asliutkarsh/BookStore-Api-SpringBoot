package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.exception.BookExistsException;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/book")
public class BookController {
    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }



    @PostMapping("/")
    public ResponseEntity<?> addBook(@RequestBody BookDTO book) {
        try {
            bookService.add_book(book);
            return new ResponseEntity<>("Book Created Successfully",HttpStatus.CREATED);
        }catch (BookExistsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }catch (Exception e){
            return new ResponseEntity<>("Internal Error Adding Book", HttpStatus.INTERNAL_SERVER_ERROR);
        }    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookDTO book) {
        try {
            bookService.update_book(id,book);
            return new ResponseEntity<>("Book Updated Successfully",HttpStatus.CREATED);
        }catch (BookNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (BookExistsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }catch (Exception e){
            return new ResponseEntity<>("Internal Error Updating Book", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookService.get_book(id));
        }catch (BookNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Internal Error Getting Book", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllBooks() {
        try {
            return ResponseEntity.ok(bookService.get_all_books());
        }catch (Exception e){
            return new ResponseEntity<>("Internal Error Getting Book", HttpStatus.INTERNAL_SERVER_ERROR);
        }    }

    @GetMapping("/filter")
    public ResponseEntity<?> getBooksWithFilter(@RequestParam(required = false,defaultValue = "") String author,
                                                @RequestParam(required = false,defaultValue = "") String name) {
        try {
            return ResponseEntity.ok(bookService.get_books_with_filter(author,name));
        }catch (Exception e){
            return new ResponseEntity<>("Internal Error Getting Book", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
