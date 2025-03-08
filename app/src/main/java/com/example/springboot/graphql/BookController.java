package com.example.springboot.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.springboot.graphql.model.Book;

@Controller
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @QueryMapping
    public Book getBookById(@Argument String id) {
        return bookService.getBookById(id);
    }

    @QueryMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @MutationMapping
    public Book addBook(@Argument String title, @Argument String author) {
        return bookService.addBook(title, author);
    }
}
