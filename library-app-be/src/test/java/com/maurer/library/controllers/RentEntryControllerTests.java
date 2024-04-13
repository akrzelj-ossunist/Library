package com.maurer.library.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maurer.library.controllers.interfaces.RentEntryController;
import com.maurer.library.dtos.RentBookDto;
import com.maurer.library.dtos.RentEntryResDto;
import com.maurer.library.exceptions.AlreadyExistException;
import com.maurer.library.mapper.DataMapper;
import com.maurer.library.models.Book;
import com.maurer.library.models.RentEntry;
import com.maurer.library.models.User;
import com.maurer.library.models.enums.Status;
import com.maurer.library.services.interfaces.RentService;
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

@WebMvcTest(RentEntryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RentEntryControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RentService rentService;
    @MockBean
    private DataMapper dataMapper;
    private RentBookDto rentBookDto;
    private RentEntry rentEntry;
    private RentEntryResDto rentEntryResDto;


    @BeforeEach
    public void init() {
        rentBookDto = RentBookDto.builder().userId("123User").bookId("123Book").build();
        rentEntry = RentEntry.builder().id("123").lendingDate(new Date()).returnDate(null).status(Status.RENTED).note(null).book(Book.builder().id("123Book").title("Book1").build()).user(User.builder().id("123User").build()).build();
        rentEntryResDto = RentEntryResDto.builder().id("123").lendingDate(new Date()).returnDate(null).status(Status.RENTED).note(null).book(Book.builder().id("123Book").title("Book1").build()).user(User.builder().id("123User").build()).build();
    }

    @Test
    void createTest() throws Exception, AlreadyExistException {
        when(rentService.rentBook(any(RentBookDto.class))).thenReturn(rentEntry);
        when(dataMapper.rentEntryToDto(any(RentEntry.class))).thenReturn(rentEntryResDto);

        ResultActions response = mockMvc.perform(post("/api/v1/rent-entry/create")
                .param("bookId", "123Book")
                .param("userId", "123User")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(rentEntryResDto.getId()))
                .andExpect(jsonPath("$.user.id").value(rentBookDto.getUserId()))
                .andExpect(jsonPath("$.book.id").value(rentBookDto.getBookId()));
    }

    @Test
    void returnBookTest() throws Exception, AlreadyExistException {
        when(rentService.returnBook("123", "no note")).thenReturn(rentEntry);
        rentEntryResDto.setReturnDate(new Date());
        rentEntryResDto.setNote("no note");
        rentEntryResDto.setStatus(Status.RETURNED);
        when(dataMapper.rentEntryToDto(any(RentEntry.class))).thenReturn(rentEntryResDto);

        ResultActions response = mockMvc.perform(put("/api/v1/rent-entry/return/{id}", "123")
                .param("note", "no note")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rentEntryResDto.getId()))
                .andExpect(jsonPath("$.status").value("RETURNED"));
    }

    @Test
    void listTest() throws Exception {
        List<RentEntry> rentEntryList = Collections.singletonList(rentEntry);
        List<RentEntryResDto> rentEntryResDtos = Collections.singletonList(rentEntryResDto);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("page", "1");
        resultMap.put("size", "10");

        when(rentService.findAllRentEntries(resultMap)).thenReturn(rentEntryList);
        when(dataMapper.listRentToListDto(rentEntryList)).thenReturn(rentEntryResDtos);

        ResultActions response = mockMvc.perform(get("/api/v1/rent-entry/list")
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
    void rentEntryPage() throws Exception {
        when(rentService.findRentEntryById("123")).thenReturn(rentEntry);
        when(dataMapper.rentEntryToDto(rentEntry)).thenReturn(rentEntryResDto);

        ResultActions response = mockMvc.perform(get("/api/v1/rent-entry/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"));
    }

    @Test
    void filterList()  throws Exception {
        List<RentEntry> rentEntryList = Collections.singletonList(rentEntry);
        List<RentEntryResDto> rentEntryResDtoList = Collections.singletonList(rentEntryResDto);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("page", "1");
        resultMap.put("size", "10");
        resultMap.put("status", "RENTED");
        resultMap.put("book", "Book1");
        resultMap.put("user", "123User");

        when(rentService.filterRentEntries(resultMap)).thenReturn(rentEntryList);
        when(dataMapper.listRentToListDto(rentEntryList)).thenReturn(rentEntryResDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/rent-entry/query")
                .param("size", "10")
                .param("page", "1")
                .param("status", "RENTED")
                .param("book", "Book1")
                .param("user", "123User")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].status").value("RENTED"))
                .andExpect(jsonPath("$[0].book.title").value("Book1"))
                .andExpect(jsonPath("$[0].user.id").value("123User"))
                .andExpect(jsonPath("$.length()").value(1));
    }
}
