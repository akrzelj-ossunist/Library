package com.maurer.library.services.implementation;

import com.maurer.library.dtos.BookDto;
import com.maurer.library.dtos.BookUpdateDto;
import com.maurer.library.exceptions.AlreadyExistException;
import com.maurer.library.exceptions.InvalidArgumentsException;
import com.maurer.library.exceptions.ObjectDoesntExistException;
import com.maurer.library.mapper.DataMapper;
import com.maurer.library.models.Author;
import com.maurer.library.models.Book;
import com.maurer.library.models.RentEntry;
import com.maurer.library.models.enums.Genre;
import com.maurer.library.repositories.BookRepository;
import com.maurer.library.services.interfaces.AuthorService;
import com.maurer.library.services.interfaces.BookService;
import com.maurer.library.services.interfaces.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *  Implementation of book service which stores data into java collection
 **/
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    private final DataMapper dataMapper;
    private final RentService rentService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, DataMapper dataMapper, @Lazy RentService rentService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.dataMapper = dataMapper;
        this.rentService = rentService;
    }

    @Override
    public Book addBook(BookDto bookDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        if(bookDto == null) throw new ObjectDoesntExistException("Sent book cannot be null!");

        if(findBookByTitle(bookDto.getTitle()) != null) throw new AlreadyExistException("Book you wanna add already exists!");

        return bookRepository.save(dataMapper.dtoToBook(bookDto, authorService));
    }

    @Override
    public Book updateBook(BookUpdateDto bookUpdateDto, String bookId) throws ObjectDoesntExistException, InvalidArgumentsException, AlreadyExistException {

        if(bookUpdateDto == null) throw new ObjectDoesntExistException("Sent book cannot be null!");

        Book book = findBookById(bookId);

        if(book == null) throw new ObjectDoesntExistException("Book you wanna update doesn't exist!");

        return bookRepository.save(editBook(book, bookUpdateDto));
    }

    public Book editBook(Book book, BookUpdateDto bookUpdateDto) throws InvalidArgumentsException, ObjectDoesntExistException {

        book.setAuthor(authorService.findByAuthorId(bookUpdateDto.getAuthor()));
        book.setGenre(Genre.valueOf(bookUpdateDto.getGenre().toUpperCase()));
        book.setTitle(bookUpdateDto.getTitle());
        book.setIsbn(bookUpdateDto.getIsbn());
        book.setNote(bookUpdateDto.getNote());

        return book;

    }

    @Override
    public Boolean deleteBook(String bookId) throws InvalidArgumentsException, AlreadyExistException, ObjectDoesntExistException {

        if(bookId == null) throw new InvalidArgumentsException("Sent arguments cannot be null!");

        List<RentEntry> rentEntryList = rentService.findRentsByBookId(bookId);
        if(!rentEntryList.isEmpty()) throw new AlreadyExistException("Book is currently being rented so it cannot be deleted!");

        Book book = findBookById(bookId);

        if(book == null) throw new ObjectDoesntExistException("Book doesn't exist so it cannot be deleted!");

        bookRepository.delete(book);

        return true;
    }

    @Override
    public Book findBookByTitle(String title) throws InvalidArgumentsException, ObjectDoesntExistException {

        if(title == null) throw new InvalidArgumentsException("Sent arguments cannot be null!");

        Optional<Book> book = bookRepository.findByTitle(title);

        if(book.isEmpty()) throw new ObjectDoesntExistException("Object you are looking for doesn't exist!");

        return book.get();
    }

    @Override
    public List<Book> findAvailableBooks() throws InvalidArgumentsException {
        return bookRepository.findByIsAvilable(true);
    }

    @Override
    public List<Book> findAllBooks(Map<String, String> allParams) {
        int page = allParams.get("page") != null ? Integer.parseInt(allParams.get("page")) - 1 : 0;
        int size = allParams.get("size") != null ? Integer.parseInt(allParams.get("size")) : 10;

        Pageable pageable = (Pageable) PageRequest.of(page, size);
        return bookRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Book> findBooksByGenre(String genre) throws InvalidArgumentsException {

        if(genre == null) throw new InvalidArgumentsException("Sent arguments cannot be null!");

        return bookRepository.findByGenre(genre);
    }

    @Override
    public List<Book> findBooksByAuthorId(String authorId) throws InvalidArgumentsException, ObjectDoesntExistException {

        if(authorId == null) throw new InvalidArgumentsException("Sent arguments cannot be null!");

        Author author = authorService.findByAuthorId(authorId);

        return bookRepository.findByAuthor(author);
    }

    @Override
    public Book findBookById(String bookId) throws InvalidArgumentsException, ObjectDoesntExistException {

        if(bookId == null) throw new InvalidArgumentsException("Sent arguments cannot be null!");

        Optional<Book> book = bookRepository.findById(bookId);

        if(book.isEmpty()) throw new ObjectDoesntExistException("Book you are looking for doesn't exist!");

        return book.get();
    }

    @Override
    public Book updateIsAvailable(Boolean status, String bookId) throws InvalidArgumentsException, ObjectDoesntExistException, AlreadyExistException {

        if(status == null) throw new InvalidArgumentsException("Sent arguments cannot be null!");

        Book book = findBookById(bookId);

        if(book == null) throw new ObjectDoesntExistException("Book you wanna update doesn't exist!");

        book.setIsAvilable(status);

        return bookRepository.save(book);
    }

    @Override
    public List<Book> filterBooks(Map<String, String> allParams) throws InvalidArgumentsException, ObjectDoesntExistException {


        int page = allParams.get("page") != null ? Integer.parseInt(allParams.get("page")) - 1 : 0;
        int size = allParams.get("size") != null ? Integer.parseInt(allParams.get("size")) : 10;

        Pageable pageable = (Pageable) PageRequest.of(page, size);

        String title = allParams.get("title");
        String isbn = allParams.get("isbn");
        Author author = authorService.findByFullName(allParams.get("author"));
        Genre genre = Genre.valueOf(allParams.get("genre"));
        Boolean isAvailable = Boolean.valueOf(allParams.get("available"));

        return bookRepository.findByTitleAndAuthorAndIsAvailableAndIsbnAndGenre(title, author, isAvailable, isbn, genre, pageable).getContent();
    }
}
