package com.maurer.library.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maurer.library.controllers.interfaces.UserController;
import com.maurer.library.dtos.*;
import com.maurer.library.exceptions.AlreadyExistException;
import com.maurer.library.mapper.DataMapper;
import com.maurer.library.models.User;
import com.maurer.library.models.enums.UserRole;
import com.maurer.library.services.interfaces.UserService;
import com.maurer.library.utils.TokenGenerator;
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

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private TokenGenerator tokenGenerator;
    @MockBean
    private DataMapper dataMapper;
    @Autowired
    private ObjectMapper objectMapper;
    private UserDto userDto;
    private User user;
    private UserResDto userResDto;
    private UserUpdateDto userUpdateDto;

    @BeforeEach
    public void init() {
        userDto = UserDto.builder().fullName("John Doe").email("john@example.com").emailRepeat("john@example.com").password("password").passwordRepeat("password").address("123 Street").birthday(new Date()).build();

        userUpdateDto = UserUpdateDto.builder().fullName("Johnny Doe").address("321 Street").birthday(new Date()).build();

        user = User.builder().fullName("John Doe").email("john@example.com").password("password").address("123 Street").birthday(new Date()).role(UserRole.ADMIN).build();

        userResDto = UserResDto.builder().id("123").fullName("John Doe").email("john@example.com").address("123 Street").birthday(new Date()).role(UserRole.ADMIN).build();
    }

    @Test
    public void registerTest() throws Exception, AlreadyExistException {

        when(userService.addUser(any(UserDto.class))).thenReturn(user);
        when(dataMapper.userToDto(any(User.class))).thenReturn(userResDto);

        ResultActions response = mockMvc.perform(post("/api/v1/user/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(userDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.fullName").value(userDto.getFullName()));
    }

    @Test
    public void loginTest() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("john.doe@gmail.com");
        userLoginDto.setPassword("password");

        String jwtToken = "1245fdhg2t643zhzg";

        when(userService.validateLogin(any(UserLoginDto.class))).thenReturn(true);
        when(userService.findUserByEmail(any(String.class))).thenReturn(user);
        when(tokenGenerator.generateToken(any(String.class), any(String.class))).thenReturn(jwtToken);

        ResultActions response = mockMvc.perform(post("/api/v1/user/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(userLoginDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(jwtToken));
    }

    @Test
    public void editTest() throws Exception, AlreadyExistException {
        user.setFullName(userUpdateDto.getFullName());
        user.setAddress(userUpdateDto.getAddress());
        user.setBirthday(userUpdateDto.getBirthday());

        userResDto.setFullName(userUpdateDto.getFullName());
        userResDto.setAddress(userUpdateDto.getAddress());
        userResDto.setBirthday(userUpdateDto.getBirthday());

        when(userService.updateUser(any(String.class), any(UserUpdateDto.class))).thenReturn(user);
        when(dataMapper.userToDto(any(User.class))).thenReturn(userResDto);

        ResultActions response = mockMvc.perform(put("/api/v1/user/edit/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(userUpdateDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(userUpdateDto.getAddress()))
                .andExpect(jsonPath("$.fullName").value(userUpdateDto.getFullName()));
    }

    @Test
    public void changePasswordTest() throws Exception, AlreadyExistException {
        UserPasswordDto userPasswordDto = new UserPasswordDto();
        userPasswordDto.setPassword("password");
        userPasswordDto.setPasswordRepeat("password");

        when(userService.userChangePassword("123", userPasswordDto)).thenReturn(true);

        ResultActions response = mockMvc.perform(put("/api/v1/user/change-password/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(userPasswordDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

    }

    @Test
    public void deleteTest() throws Exception, AlreadyExistException {

        when(userService.deleteUser("123")).thenReturn(true);

        ResultActions response = mockMvc.perform(delete("/api/v1/user/delete/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    public void listTest() throws Exception {
        List<User> userList = Collections.singletonList(user);
        List<UserResDto> userResDtoList = Collections.singletonList(userResDto);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("page", "1");
        resultMap.put("size", "10");

        when(userService.findAllUsers(resultMap)).thenReturn(userList);
        when(dataMapper.listUserToListDto(userList)).thenReturn(userResDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/user/list")
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
    public void profileTest() throws Exception {

        when(userService.findUserById("123")).thenReturn(user);
        when(dataMapper.userToDto(user)).thenReturn(userResDto);

        ResultActions response = mockMvc.perform(get("/api/v1/user/profile/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"));
    }

    @Test
    public void queryListTest() throws Exception {
        List<User> userList = Collections.singletonList(user);
        List<UserResDto> userResDtoList = Collections.singletonList(userResDto);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("page", "1");
        resultMap.put("size", "10");
        resultMap.put("fullName", "John Doe");
        resultMap.put("email", "john@example.com");

        when(userService.filterUsers(resultMap)).thenReturn(userList);
        when(dataMapper.listUserToListDto(userList)).thenReturn(userResDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/user/query")
                .param("size", "10")
                .param("page", "1")
                .param("fullName", "John Doe")
                .param("email", "john@example.com")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].fullName").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$.length()").value(1));
    }
}
