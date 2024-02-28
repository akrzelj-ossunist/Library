package com.maurer.library.controllers.interfaces;

import com.maurer.library.dtos.BookUpdateDto;
import com.maurer.library.dtos.BookDto;
import com.maurer.library.exceptions.AlreadyExistException;
import com.maurer.library.exceptions.InvalidArgumentsException;
import com.maurer.library.exceptions.ObjectDoesntExistException;
import com.maurer.library.models.Book;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BookController {

    /**
     * Receives new book from client side and sends it to service side
     * In case new object is created returns created object otherwise returns bad request
     **/
    ResponseEntity<Book> create(BookDto bookDto) throws InvalidArgumentsException, ObjectDoesntExistException, AlreadyExistException;

    /**
     * Receives info for book we want to edit from client side and sends it to service side
     * In case book is edited returns edited book if not returns bad request
     **/
    ResponseEntity<Book> edit(String bookId, BookUpdateDto bookUpdateDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException;

    /**
     * Receives book id of book we want to delete from client side and sends it to service side
     * In case book is deleted returns true if not returns bad request
     **/
    ResponseEntity<Boolean> delete(String bookId) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException;

    /** Takes book id from params and looks for book if exist send book to client side if not sends bad req **/
    ResponseEntity<Book> bookPage(String bookId) throws ObjectDoesntExistException, InvalidArgumentsException;

    /** Sends list of books from server to client side **/
    ResponseEntity<List<Book>> list();

    /** Takes all params from link and filters all books then sends that list to client side **/
    ResponseEntity<List<Book>> filterList(Map<String, String> allParams) throws InvalidArgumentsException;
}
