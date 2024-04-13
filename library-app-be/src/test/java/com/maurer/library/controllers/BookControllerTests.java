package com.maurer.library.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maurer.library.controllers.interfaces.AuthorController;
import com.maurer.library.controllers.interfaces.BookController;
import com.maurer.library.dtos.*;
import com.maurer.library.exceptions.AlreadyExistException;
import com.maurer.library.exceptions.InvalidAuthorException;
import com.maurer.library.mapper.DataMapper;
import com.maurer.library.models.Author;
import com.maurer.library.models.Book;
import com.maurer.library.models.enums.Genre;
import com.maurer.library.services.interfaces.AuthorService;
import com.maurer.library.services.interfaces.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookService bookService;
    @MockBean
    private DataMapper dataMapper;
    private Book book;
    private BookDto bookDto;
    private BookResDto bookResDto;
    private BookUpdateDto bookUpdateDto;

    @BeforeEach
    public void init() {
        book = Book.builder()
                .id("123")
                .title("Sample Book")
                .author(Author.builder().id("123Author").fullName("Edgar Alan Poe").build()) // Assuming you have an instance of Author
                .isAvilable(true)
                .note("Sample note")
                .createdDate(new Date())
                .isbn("1234567890")
                .genre(Genre.THRILLER)
                .build();

        bookDto = BookDto.builder()
                .title("Sample Book")
                .author("123Author")
                .note("Sample note")
                .isbn("1234567890")
                .genre("THRILLER")
                .build();

        bookResDto = BookResDto.builder()
                .id("123")
                .title("Sample Book")
                .author(Author.builder().id("123Author").fullName("Edgar Alan Poe").build()) // Assuming you have an instance of Author
                .isAvilable(true)
                .note("Sample note")
                .createdDate(new Date())
                .isbn("1234567890")
                .genre(Genre.THRILLER)
                .build();

        bookUpdateDto = BookUpdateDto.builder()
                .title("Updated Book Title")
                .author("321Author")
                .note("Updated note")
                .isbn("9876543210")
                .genre("THRILLER")
                .build();

    }

    @Test
    void createTest() throws Exception, AlreadyExistException, InvalidAuthorException {
        when(bookService.addBook(any(BookDto.class))).thenReturn(book);
        when(dataMapper.bookToDto(any(Book.class))).thenReturn(bookResDto);

        ResultActions response = mockMvc.perform(post("/api/v1/book/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(bookDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bookResDto.getId()))
                .andExpect(jsonPath("$.title").value(bookDto.getTitle()));
    }

    @Test
    void editTest() throws Exception, AlreadyExistException, InvalidAuthorException {
        book.setAuthor(Author.builder().id(bookUpdateDto.getAuthor()).fullName("Edgar Alan Poe").build());
        book.setNote(bookUpdateDto.getNote());
        book.setIsbn(bookUpdateDto.getIsbn());
        book.setTitle(bookUpdateDto.getTitle());
        book.setGenre(Genre.valueOf(bookUpdateDto.getGenre()));

        bookResDto.setAuthor(Author.builder().id(bookUpdateDto.getAuthor()).fullName("Edgar Alan Poe").build());
        bookResDto.setNote(bookUpdateDto.getNote());
        bookResDto.setIsbn(bookUpdateDto.getIsbn());
        bookResDto.setTitle(bookUpdateDto.getTitle());
        bookResDto.setGenre(Genre.valueOf(bookUpdateDto.getGenre()));

        when(bookService.updateBook(any(BookUpdateDto.class), any(String.class))).thenReturn(book);
        when(dataMapper.bookToDto(any(Book.class))).thenReturn(bookResDto);

        ResultActions response = mockMvc.perform(put("/api/v1/book/update/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(bookUpdateDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookResDto.getId()))
                .andExpect(jsonPath("$.title").value(bookUpdateDto.getTitle()));
    }

    @Test
    void deleteTest() throws Exception, InvalidAuthorException, AlreadyExistException {
        when(bookService.deleteBook("123")).thenReturn(true);

        ResultActions response = mockMvc.perform(delete("/api/v1/book/delete/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void bookPage() throws Exception {
        when(bookService.findBookById("123")).thenReturn(book);
        when(dataMapper.bookToDto(book)).thenReturn(bookResDto);

        ResultActions response = mockMvc.perform(get("/api/v1/book/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"));
    }

    @Test
    void list() throws Exception {
        List<Book> bookList = Collections.singletonList(book);
        List<BookResDto> bookResDtoList = Collections.singletonList(bookResDto);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("page", "1");
        resultMap.put("size", "10");

        when(bookService.findAllBooks(resultMap)).thenReturn(bookList);
        when(dataMapper.listBookToListDto(bookList)).thenReturn(bookResDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/book/list")
                .param("size", "10")
                .param("page", "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void filterList()  throws Exception {
        List<Book> bookList = Collections.singletonList(book);
        List<BookResDto> bookResDtoList = Collections.singletonList(bookResDto);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("page", "1");
        resultMap.put("size", "10");
        resultMap.put("title", "Sample Book");
        resultMap.put("isbn", "1234567890");
        resultMap.put("author", "Edgar Alan Poe");
        resultMap.put("genre", "THRILLER");
        resultMap.put("available", "True");

        when(bookService.filterBooks(resultMap)).thenReturn(bookList);
        when(dataMapper.listBookToListDto(bookList)).thenReturn(bookResDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/book/query")
                .param("size", "10")
                .param("page", "1")
                .param("title", "Sample Book")
                .param("isbn", "1234567890")
                .param("author", "Edgar Alan Poe")
                .param("genre", "THRILLER")
                .param("available", "True")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").value("Sample Book"))
                .andExpect(jsonPath("$[0].author.fullName").value("Edgar Alan Poe"))
                .andExpect(jsonPath("$[0].isbn").value("1234567890"))
                .andExpect(jsonPath("$[0].genre").value("THRILLER"))
                .andExpect(jsonPath("$[0].isAvilable").value("true"))
                .andExpect(jsonPath("$.length()").value(1));
    }
}