package com.maurer.library.services.interfaces;

import com.maurer.library.dtos.BookDto;
import com.maurer.library.dtos.BookUpdateDto;
import com.maurer.library.exceptions.AlreadyExistException;
import com.maurer.library.exceptions.InvalidArgumentsException;
import com.maurer.library.exceptions.ObjectDoesntExistException;
import com.maurer.library.models.Book;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 *  Functionalities of book service
 **/
public interface BookService {

    /** Adds book in library **/
    Book addBook(BookDto bookDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException;

    /** Updates author in library **/
    Book updateBook(BookUpdateDto bookUpdateDto, String bookId) throws ObjectDoesntExistException, InvalidArgumentsException, AlreadyExistException;

    /** If book not a single book is rented we can delete that book from library **/
    Boolean deleteBook(String bookId) throws InvalidArgumentsException, AlreadyExistException, ObjectDoesntExistException;

    /** Filters list of books by book title **/
    Book findBookByTitle(String title) throws InvalidArgumentsException, ObjectDoesntExistException;

    /** Returns list of available books in library **/
    List<Book> findAvailableBooks() throws InvalidArgumentsException;

    /** Returns list of books **/
    Page<Book> findAllBooks(Map<String, String> allParams);

    /** Filters list of books by there genre **/
    List<Book> findBooksByGenre(String genre) throws InvalidArgumentsException;

    /** Filters books by specific author **/
    public List<Book> findBooksByAuthorId(String authorId) throws InvalidArgumentsException, ObjectDoesntExistException;

    /** Looks up for book by books id **/
    public Book findBookById(String bookId) throws InvalidArgumentsException, ObjectDoesntExistException;

    /** Updates current availability of the book **/
    Book updateIsAvailable(Boolean status, String bookId) throws InvalidArgumentsException, ObjectDoesntExistException, AlreadyExistException;

    Page<Book> filterBooks(Map<String, String> allParams) throws InvalidArgumentsException, ObjectDoesntExistException;
}
