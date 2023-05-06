package com.example.bookstore.entity;

import com.example.bookstore.dto.BookDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;

import java.util.Objects;

@Entity
@Table(name = "book")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private String author;
    private String isbn;

    private static ModelMapper modelMapper = new ModelMapper();

    public Book(BookDTO bookDTO){
        this.name = bookDTO.getName();
        this.description = bookDTO.getDescription();
        this.price = bookDTO.getPrice();
        this.author = bookDTO.getAuthor();
        this.isbn = bookDTO.getIsbn();
    }

    public Book update(BookDTO bookDTO){
        this.name = bookDTO.getName() !=null ? bookDTO.getName() : this.name;
        this.description = bookDTO.getDescription() !=null ? bookDTO.getDescription() : this.description;
        this.price = bookDTO.getPrice() != 0.0d ? bookDTO.getPrice() : this.price;
        this.author = bookDTO.getAuthor() !=null ? bookDTO.getAuthor() : this.author;
        this.isbn = bookDTO.getIsbn() !=null ? bookDTO.getIsbn() : this.isbn;
        return this;
    }

    public BookDTO toDTO(){
        return modelMapper.map(this,BookDTO.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Book book = (Book) o;
        return getId() != null && Objects.equals(getId(), book.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
