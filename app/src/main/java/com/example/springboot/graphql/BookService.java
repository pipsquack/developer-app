package com.example.springboot.graphql;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.springboot.graphql.model.Book;

@Service
public class BookService {
    private final List<Book> books = new ArrayList<>();

    public BookService() {
        books.add(new Book(UUID.randomUUID().toString(), "Spring Boot GraphQL", "John Doe"));
        books.add(new Book(UUID.randomUUID().toString(), "GraphQL for Java", "Jane Smith"));
    }

    public Book getBookById(String id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public Book addBook(String title, String author) {
        Book book = new Book(UUID.randomUUID().toString(), title, author);
        books.add(book);
        return book;
    }
}
