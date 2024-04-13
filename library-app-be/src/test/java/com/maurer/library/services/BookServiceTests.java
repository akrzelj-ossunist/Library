package com.maurer.library.services;

import com.maurer.library.dtos.AuthorDto;
import com.maurer.library.dtos.BookDto;
import com.maurer.library.dtos.BookUpdateDto;
import com.maurer.library.exceptions.AlreadyExistException;
import com.maurer.library.exceptions.InvalidArgumentsException;
import com.maurer.library.exceptions.ObjectDoesntExistException;
import com.maurer.library.mapper.DataMapper;
import com.maurer.library.models.Author;
import com.maurer.library.models.Book;
import com.maurer.library.models.RentEntry;
import com.maurer.library.models.User;
import com.maurer.library.models.enums.Genre;
import com.maurer.library.models.enums.Status;
import com.maurer.library.repositories.BookRepository;
import com.maurer.library.services.interfaces.AuthorService;
import com.maurer.library.services.interfaces.BookService;
import com.maurer.library.services.interfaces.RentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(BookService.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookServiceTests {
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private DataMapper dataMapper;
    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private RentService rentService;
    private BookDto bookDto;
    private Book book;
    private AuthorDto authorDto;
    private Author author;
    private RentEntry rentEntry;

    @BeforeEach
    public void init() {
        authorDto = AuthorDto.builder().fullName("Edgar Alan Poe").birthday(new Date()).build();
        author = Author.builder().id("Author123").fullName("Edgar Alan Poe").birthday(new Date()).build();
        bookDto = BookDto.builder().author(author.getFullName()).genre("THRILLER").isbn("1234567890").title("Gavran").build();
        book = Book.builder().id("Book123").author(author).isAvilable(true).genre(Genre.THRILLER).isbn("1234567890").title("Gavran").build();
        rentEntry = RentEntry.builder().id("Rent123").book(book).lendingDate(new Date()).status(Status.RENTED).user(User.builder().build()).build();
    }

    @Test
    public void addBookTest() throws ObjectDoesntExistException, InvalidArgumentsException {
        when(bookService.findBookByTitle(bookDto.getTitle())).thenReturn(null);
        Book exsitingBook = bookService.findBookByTitle(bookDto.getTitle());
        assertNull(exsitingBook);

        when(dataMapper.dtoToBook(bookDto, authorService)).thenReturn(book);
        Book mappedBook = dataMapper.dtoToBook(bookDto, authorService);
        assertEquals(mappedBook.getTitle(), bookDto.getTitle());

        when(bookRepository.save(mappedBook)).thenReturn(book);
        Book savedBook = bookRepository.save(mappedBook);
        assertEquals(savedBook.getId(), "Book123");
    }

    @Test
    public void updateBookTest() throws ObjectDoesntExistException, InvalidArgumentsException, AlreadyExistException {
        String bookId = "Book123";
        BookUpdateDto bookUpdateDto = BookUpdateDto.builder()
                .author("Author123")
                .genre("ROMANCE")
                .isbn("0987654321")
                .title("Updated Title")
                .note("Updated Note")
                .build();

        when(bookService.findBookById(bookId)).thenReturn(book);
        when(authorService.findByAuthorId(bookUpdateDto.getAuthor())).thenReturn(author);

        book.setAuthor(authorService.findByAuthorId(bookUpdateDto.getAuthor()));
        book.setGenre(Genre.valueOf(bookUpdateDto.getGenre().toUpperCase()));
        book.setTitle(bookUpdateDto.getTitle());
        book.setIsbn(bookUpdateDto.getIsbn());
        book.setNote(bookUpdateDto.getNote());

        when(bookRepository.save(book)).thenReturn(book);

        Book updatedBook = bookRepository.save(book);

        assertNotNull(updatedBook);
        assertEquals(bookUpdateDto.getAuthor(), updatedBook.getAuthor().getId());
        assertEquals(bookUpdateDto.getGenre().toUpperCase(), updatedBook.getGenre().name());
        assertEquals(bookUpdateDto.getIsbn(), updatedBook.getIsbn());
        assertEquals(bookUpdateDto.getTitle(), updatedBook.getTitle());
        assertEquals(bookUpdateDto.getNote(), updatedBook.getNote());
    }

    @Test
    public void deleteBookTest() throws InvalidArgumentsException, AlreadyExistException, ObjectDoesntExistException {
        when(rentService.findRentsByBookId(book.getId())).thenReturn(Collections.emptyList());
        List<RentEntry> rentEntryList = rentService.findRentsByBookId(book.getId());
        assertTrue(rentEntryList.isEmpty());

        when(bookService.findBookById(book.getId())).thenReturn(book);
        Book foundBook = bookService.findBookById(book.getId());
        assertNotNull(foundBook);

        bookRepository.delete(book);

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    public void findBookByTitleTest() throws InvalidArgumentsException, ObjectDoesntExistException {
        when(bookRepository.findByTitle("Gavran")).thenReturn(Optional.of(book));
        Optional<Book> bookByTitle = bookRepository.findByTitle("Gavran");

        assertTrue(bookByTitle.isPresent());
        assertEquals(bookByTitle.get().getTitle(), "Gavran");
    }

    @Test
    public void findAvailableBooksTest() {
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookRepository.findByIsAvilable(true)).thenReturn(books);

        List<Book> foundBooks = bookRepository.findByIsAvilable(true);
        assertEquals(foundBooks.getFirst().getIsAvilable(), true);
    }

    @Test
    public void findAllBooks() {
        Pageable pageable = (Pageable) PageRequest.of(0, 10);
        List<Book> books = new ArrayList<>();
        books.add(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, 0);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        List<Book> bookList = bookRepository.findAll(pageable).getContent();
        assertFalse(bookList.isEmpty());
    }

    @Test
    public void updateIsAvailableTest() throws ObjectDoesntExistException, InvalidArgumentsException {
        when(bookService.findBookById(book.getId())).thenReturn(book);
        Book bookById = bookService.findBookById(book.getId());
        assertNotNull(bookById);

        book.setIsAvilable(false);
        when(bookRepository.save(book)).thenReturn(book);
        assertEquals(book.getIsAvilable(), false);
    }

    @Test
    public void filterBooksTest() {
        Pageable pageable = (Pageable) PageRequest.of(0, 10);
        List<Book> books = new ArrayList<>();
        books.add(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, 0);

        when(bookRepository.findByTitleAndAuthorAndIsAvailableAndIsbnAndGenre("Gavran", author, true, "1234567890", Genre.THRILLER, pageable)).thenReturn(bookPage);
        List<Book> bookMockedList = bookRepository.findByTitleAndAuthorAndIsAvailableAndIsbnAndGenre("Gavran", author, true, "1234567890", Genre.THRILLER, pageable).getContent();
        assertFalse(bookMockedList.isEmpty());
        assertEquals(bookMockedList.size(), 1);
        assertEquals(bookMockedList.getFirst().getId(), "Book123");
    }
}
