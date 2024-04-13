package com.maurer.library.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maurer.library.controllers.interfaces.AuthorController;
import com.maurer.library.dtos.AuthorDto;
import com.maurer.library.dtos.AuthorResDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.mapper.DataMapper;
import com.maurer.library.models.Author;
import com.maurer.library.services.interfaces.AuthorService;
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

@WebMvcTest(AuthorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthorControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private DataMapper dataMapper;
    private AuthorDto authorDto;
    private Author author;
    private AuthorResDto authorResDto;


    @BeforeEach
    public void init() {
        authorDto = AuthorDto.builder().fullName("Edgar Alan Poe").birthday(new Date()).build();
        author = Author.builder().id("123").fullName("Edgar Alan Poe").birthday(new Date()).build();
        authorResDto = AuthorResDto.builder().id("123").fullName("Edgar Alan Poe").birthday(new Date()).build();
    }

    @Test
    void createTest() throws Exception, AlreadyExistException, InvalidAuthorException {
        when(authorService.addAuthor(any(AuthorDto.class))).thenReturn(author);
        when(dataMapper.authorToDto(any(Author.class))).thenReturn(authorResDto);

        ResultActions response = mockMvc.perform(post("/api/v1/author/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(authorDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(authorResDto.getId()))
                .andExpect(jsonPath("$.fullName").value(authorResDto.getFullName()));
    }

    @Test
    void editTest() throws Exception, AlreadyExistException, InvalidAuthorException {
        authorDto.setFullName("Edgy Alan Poe");
        authorResDto.setFullName("Edgy Alan Poe");
        when(authorService.updateAuthor(any(String.class), any(AuthorDto.class))).thenReturn(author);
        when(dataMapper.authorToDto(any(Author.class))).thenReturn(authorResDto);

        ResultActions response = mockMvc.perform(put("/api/v1/author/update/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(authorDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authorResDto.getId()))
                .andExpect(jsonPath("$.fullName").value(authorDto.getFullName()));
    }

    @Test
    void deleteTest() throws Exception, InvalidAuthorException {
        when(authorService.deleteAuthor("123")).thenReturn(true);

        ResultActions response = mockMvc.perform(delete("/api/v1/author/delete/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void authorProfileTest() throws Exception {
        when(authorService.findByAuthorId("123")).thenReturn(author);
        when(dataMapper.authorToDto(author)).thenReturn(authorResDto);

        ResultActions response = mockMvc.perform(get("/api/v1/author/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"));
    }

    @Test
    void listTest() throws Exception {
        List<Author> authorList = Collections.singletonList(author);
        List<AuthorResDto> authorResDtoList = Collections.singletonList(authorResDto);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("page", "1");
        resultMap.put("size", "10");

        when(authorService.findAllAuthors(resultMap)).thenReturn(authorList);
        when(dataMapper.listAuthorToListDto(authorList)).thenReturn(authorResDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/author/list")
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
        List<Author> authorList = Collections.singletonList(author);
        List<AuthorResDto> authorResDtoList = Collections.singletonList(authorResDto);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("page", "1");
        resultMap.put("size", "10");
        resultMap.put("fullName", "Edgar Alan Poe");

        when(authorService.filterAuthors(resultMap)).thenReturn(authorList);
        when(dataMapper.listAuthorToListDto(authorList)).thenReturn(authorResDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/author/query")
                .param("size", "10")
                .param("page", "1")
                .param("fullName", "Edgar Alan Poe")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].fullName").value("Edgar Alan Poe"))
                .andExpect(jsonPath("$.length()").value(1));
    }
}
