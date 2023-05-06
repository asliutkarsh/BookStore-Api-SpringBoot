package com.example.bookstore.service;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.BookExistsException;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    void add_book_should_add_book_to_repository() {
        //given
        BookDTO bookDTO = new BookDTO("Test Book", "Test Description", 15.25,"Test Author", "978-4-8096-6988-0");
        Book book = new Book(bookDTO);
        when(bookRepository.existsByIsbn(bookDTO.getIsbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        //when
        bookService.add_book(bookDTO);

        //then
        verify(bookRepository, times(1)).existsByIsbn(bookDTO.getIsbn());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void add_book_should_throw_book_exists_exception_when_book_already_exists() {
        //given
        BookDTO bookDTO = new BookDTO("Test Book", "Test Description", 9.99,"Test Author", "1234567890");

        //when
        when(bookRepository.existsByIsbn(bookDTO.getIsbn())).thenReturn(true);

        //then
        assertThrows(BookExistsException.class, () -> bookService.add_book(bookDTO));
        verify(bookRepository, times(1)).existsByIsbn(bookDTO.getIsbn());
        verify(bookRepository, never()).save(any(Book.class));


    }


    @Test
    void update_book_should_update_book_in_repository() {
        Long id = 1L;
        BookDTO bookDTO = new BookDTO("Updated Book", "Updated Description", 19.99, "Updated Author", "1234567890");
        Book existingBook = new Book(id,"Existing Book", "Existing Description", 9.99, "Existing Author", "1234567890");
        Book updatedBook = new Book(id,"Updated Book", "Updated Description", 19.99, "Updated Author", "1234567890");

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(bookRepository.existsByIsbn(bookDTO.getIsbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        bookService.update_book(id, bookDTO);

        verify(bookRepository).findById(id);
        verify(bookRepository).existsByIsbn(bookDTO.getIsbn());
        verify(bookRepository).save(updatedBook);
        assertEquals(updatedBook, existingBook);

    }

    @Test
    void update_book_should_throw_book_not_found_exception_when_book_not_found() {
        Long id = 1L;
        BookDTO bookDTO = new BookDTO("Test Book", "Test Description", 15.25,"Test Author", "978-4-8096-6988-0");

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.update_book(id, bookDTO));
        verify(bookRepository).findById(id);
        verify(bookRepository, never()).existsByIsbn(any());
        verify(bookRepository, never()).save(any());

    }

    @Test
    void update_book_should_throw_book_exists_exception_when_book_with_same_isbn_already_exists() {
        Long id = 1L;
        BookDTO bookDTO = new BookDTO("Updated Book", "Updated Description", 19.99, "Updated Author", "1234567890");
        Book existingBook = new Book(id,"Existing Book", "Existing Description", 9.99, "Existing Author", "1234567890");

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(bookRepository.existsByIsbn(bookDTO.getIsbn())).thenReturn(true);

        assertThrows(BookExistsException.class, () -> bookService.update_book(id, bookDTO));
        verify(bookRepository).findById(id);
        verify(bookRepository).existsByIsbn(bookDTO.getIsbn());
        verify(bookRepository, never()).save(any());

    }

    @Test
    void get_book_should_return_book_with_given_id() {
        Long id =1L;
        Book book = new Book(id,"Test Book", "Test Description", 15.25,"Test Author", "978-4-8096-6988-0");

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        BookDTO result = bookService.get_book(id);

        verify(bookRepository).findById(id);
        assertEquals(book.toDTO(), result);
    }

    @Test
    void get_book_should_throw_exception_if_book_not_found() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            bookService.get_book(id);
        });
        verify(bookRepository).findById(id);

    }

    @Test
    void get_all_books_should_return_all_books() {
        Book book1 = new Book(1L, "Book 1", "Description 1", 10.99, "Author 1", "1234567890");
        Book book2 = new Book(2L, "Book 2", "Description 2", 15.99, "Author 2", "0987654321");

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<BookDTO> result = bookService.get_all_books();

        verify(bookRepository,times(1)).findAll();

    }

    @Test
    void get_books_with_filter_should_return_filtered_books() {
        Book book1 = new Book(1L, "Book 1", "Description 1", 10.99, "Author 1", "1234567890");
        Book book2 = new Book(2L, "Book 2", "Description 2", 15.99, "Author 2", "0987654321");

        when(bookRepository.findAllByAuthorContainingAndNameContaining("Author", "2")).thenReturn(List.of(book2));

        List<BookDTO> result = bookService.get_books_with_filter("Author", "2");

        assertEquals(result.size(),1);
    }
}