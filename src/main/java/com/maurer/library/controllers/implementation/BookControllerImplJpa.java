package com.maurer.library.controllers.implementation;

import com.maurer.library.controllers.interfaces.BookController;
import com.maurer.library.dtos.BookDto;
import com.maurer.library.dtos.BookUpdateDto;
import com.maurer.library.exceptions.AlreadyExistException;
import com.maurer.library.exceptions.InvalidArgumentsException;
import com.maurer.library.exceptions.ObjectDoesntExistException;
import com.maurer.library.models.Book;
import com.maurer.library.services.interfaces.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jpa/book")
public class BookControllerImplJpa implements BookController {

    @Autowired
    private BookService bookService;

    @Override
    @PostMapping("/create")
    public ResponseEntity<Book> create(@Valid @RequestBody BookDto bookDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        Book createdBook = bookService.addBook(bookDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<Book> edit(@PathVariable("id") String bookId,@Valid @RequestBody BookUpdateDto bookUpdateDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        Book updatedBook = bookService.updateBook(bookUpdateDto, bookId);

        return ResponseEntity.ok().body(updatedBook);

    }

    @Override
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String bookId) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        boolean deleted = bookService.deleteBook(bookId);

        return ResponseEntity.ok().body(deleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Book> bookPage(@PathVariable("id") String bookId) throws ObjectDoesntExistException, InvalidArgumentsException {

        Book book = bookService.findBookById(bookId);

        return ResponseEntity.ok().body(book);
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<Book>> list() {

        List<Book> books = bookService.findAllBooks();

        return ResponseEntity.ok().body(books);
    }

    @Override
    @GetMapping("/query")
    public ResponseEntity<List<Book>> filterList(@RequestParam Map<String, String> allParams) throws InvalidArgumentsException {

        if(allParams.isEmpty()) throw new InvalidArgumentsException("Invalid amount of params sent!");

        List<Book> filteredBooks = bookService.filterBooks(allParams);

        return ResponseEntity.ok().body(filteredBooks);
    }
}
