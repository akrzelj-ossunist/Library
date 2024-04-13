package com.maurer.library.controllers.implementation;

import com.maurer.library.controllers.interfaces.BookController;
import com.maurer.library.dtos.BookDto;
import com.maurer.library.dtos.BookResDto;
import com.maurer.library.dtos.BookUpdateDto;
import com.maurer.library.exceptions.AlreadyExistException;
import com.maurer.library.exceptions.InvalidArgumentsException;
import com.maurer.library.exceptions.ObjectDoesntExistException;
import com.maurer.library.mapper.DataMapper;
import com.maurer.library.models.Book;
import com.maurer.library.services.interfaces.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/book")
@CrossOrigin
public class BookControllerImpl implements BookController {

    private final BookService bookService;
    private final DataMapper dataMapper;

    @Autowired
    public BookControllerImpl(DataMapper dataMapper, BookService bookService) {
        this.dataMapper = dataMapper;
        this.bookService = bookService;
    }

    @Override
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<BookResDto> create(@Valid @RequestBody BookDto bookDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        Book createdBook = bookService.addBook(bookDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(dataMapper.bookToDto(createdBook));
    }

    @Override
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<BookResDto> edit(@PathVariable("id") String bookId,@Valid @RequestBody BookUpdateDto bookUpdateDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        Book updatedBook = bookService.updateBook(bookUpdateDto, bookId);

        return ResponseEntity.ok().body(dataMapper.bookToDto(updatedBook));

    }

    @Override
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String bookId) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        boolean deleted = bookService.deleteBook(bookId);

        return ResponseEntity.ok().body(deleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookResDto> bookPage(@PathVariable("id") String bookId) throws ObjectDoesntExistException, InvalidArgumentsException {

        Book book = bookService.findBookById(bookId);

        return ResponseEntity.ok().body(dataMapper.bookToDto(book));
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<BookResDto>> list(@RequestParam Map<String, String> allParams) {

        List<Book> books = bookService.findAllBooks(allParams);

        return ResponseEntity.ok().body(dataMapper.listBookToListDto(books));
    }

    @Override
    @GetMapping("/query")
    public ResponseEntity<List<BookResDto>> filterList(@RequestParam Map<String, String> allParams) throws InvalidArgumentsException, ObjectDoesntExistException {

        if(allParams.isEmpty()) throw new InvalidArgumentsException("Invalid amount of params sent!");

        List<Book> filteredBooks = bookService.filterBooks(allParams);

        return ResponseEntity.ok().body(dataMapper.listBookToListDto(filteredBooks));
    }
}
