package com.maurer.library.services;

import com.maurer.library.dtos.AuthorDto;
import com.maurer.library.exceptions.InvalidArgumentsException;
import com.maurer.library.exceptions.ObjectDoesntExistException;
import com.maurer.library.mapper.DataMapper;
import com.maurer.library.models.Author;
import com.maurer.library.models.Book;
import com.maurer.library.repositories.AuthorRepository;
import com.maurer.library.services.interfaces.AuthorService;
import com.maurer.library.services.interfaces.BookService;
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
import static org.mockito.Mockito.when;

@WebMvcTest(AuthorService.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthorServiceTests {
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private DataMapper dataMapper;
    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    private AuthorDto authorDto;
    private Author author;

    @BeforeEach
    public void init() {
        authorDto = AuthorDto.builder().fullName("Edgar Alan Poe").birthday(new Date()).build();
        author = Author.builder().id("123").fullName("Edgar Alan Poe").birthday(new Date()).build();
    }

    @Test
    public void addAuthorTest() throws ObjectDoesntExistException, InvalidArgumentsException {
        when(authorService.findByFullName(authorDto.getFullName())).thenReturn(null);
        Author existingAuthor = authorService.findByFullName(authorDto.getFullName());
        assertNull(existingAuthor);

        when(dataMapper.dtoToAuthor(authorDto)).thenReturn(author);
        Author newAuthor = dataMapper.dtoToAuthor(authorDto);
        assertEquals(authorDto.getFullName(), author.getFullName());

        when(authorRepository.save(newAuthor)).thenReturn(author);
        Author createdAuthor = authorRepository.save(newAuthor);
        assertEquals(createdAuthor.getId(), "123");
    }

    @Test
    public void deleteAuthorTest() throws ObjectDoesntExistException, InvalidArgumentsException {
        when(authorService.findByAuthorId("123")).thenReturn(author);
        Author authorById = authorService.findByAuthorId("123");
        assertEquals(authorById.getId(), "123");

        List<Book> bookList = new ArrayList<>();
        when(bookService.findBooksByAuthorId(authorById.getId())).thenReturn(bookList);
        bookList = bookService.findBooksByAuthorId(authorById.getId());
        assertTrue(bookList.isEmpty());
    }

    @Test
    public void updateAuthorTest() throws ObjectDoesntExistException, InvalidArgumentsException {
        AuthorDto authorUpdateDto = AuthorDto.builder().fullName("Edgy Alan Poe").birthday(new Date()).build();

        when(authorService.findByAuthorId("123")).thenReturn(author);
        Author authorById = authorService.findByAuthorId("123");
        assertEquals("123", authorById.getId());

        authorById.setFullName(authorUpdateDto.getFullName());
        authorById.setBirthday(authorUpdateDto.getBirthday());

        when(authorRepository.save(authorById)).thenReturn(authorById);
        Author authorDB = authorRepository.save(authorById);
        assertEquals(authorDB.getFullName(), authorUpdateDto.getFullName());
        assertEquals(authorDB.getBirthday(), authorUpdateDto.getBirthday());
    }

    @Test
    public void findByAuthorIdTest() {
        when(authorRepository.findById("123")).thenReturn(Optional.ofNullable(author));
        Optional<Author> authorById = authorRepository.findById("123");
        assertTrue(authorById.isPresent());
        assertEquals(authorById.get().getId(), "123");
    }

    @Test
    public void findByFullNameTest() {
        when(authorRepository.findByFullName("Edgar Alan Poe")).thenReturn(Optional.ofNullable(author));
        Optional<Author> authorByFullName = authorRepository.findByFullName("Edgar Alan Poe");
        assertTrue(authorByFullName.isPresent());
        assertEquals(authorByFullName.get().getFullName(), "Edgar Alan Poe");
    }

    @Test
    public void findAllTest() {
        Pageable pageable = (Pageable) PageRequest.of(0, 10);
        List<Author> authorList = new ArrayList<>();
        authorList.add(author);
        Page<Author> authorPage = new PageImpl<>(authorList, pageable, 0);

        when(authorRepository.findAll(pageable)).thenReturn(authorPage);
        List<Author> authorMockedList = authorRepository.findAll(pageable).getContent();
        assertFalse(authorMockedList.isEmpty());
        assertEquals(authorMockedList.size(), 1);
    }

    @Test
    public void filterAuthorsTest() {
        Pageable pageable = (Pageable) PageRequest.of(0, 10);
        List<Author> authorList = new ArrayList<>();
        authorList.add(author);
        Page<Author> authorPage = new PageImpl<>(authorList, pageable, 0);

        when(authorRepository.findByFullName("Edgar Alan Poe", pageable)).thenReturn(authorPage);
        List<Author> authorMockedList = authorRepository.findByFullName("Edgar Alan Poe", pageable).getContent();
        assertFalse(authorMockedList.isEmpty());
        assertEquals(authorMockedList.size(), 1);
        assertEquals(authorMockedList.getFirst().getFullName(), "Edgar Alan Poe");
    }
}
