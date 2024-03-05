package com.maurer.library.services.interfaces;

import com.maurer.library.dtos.UserDto;
import com.maurer.library.dtos.UserLoginDto;
import com.maurer.library.dtos.UserPasswordDto;
import com.maurer.library.dtos.UserUpdateDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *  Functionalities of user service
 **/
public interface UserService {

    /** Add user in library **/
    public User addUser(UserDto userDto) throws InvalidArgumentsException, AlreadyExistException, PasswordMismatchException, EmailMismatchException;

    Boolean validateLogin(UserLoginDto userLoginDto) throws ObjectDoesntExistException, InvalidArgumentsException;

    /**
     * Deletes user from library
     * Deletes only users which didn't borrow any book
     * **/
    public Boolean deleteUser(String userId) throws InvalidArgumentsException, AlreadyExistException, ObjectDoesntExistException;

    /** Updates author in library **/
    public User updateUser(String userId, UserUpdateDto userDto) throws InvalidArgumentsException, ObjectDoesntExistException, AlreadyExistException;

    /** Find author from library by his id **/
    public User findUserById(String userId) throws InvalidArgumentsException, ObjectDoesntExistException;

    /** Find author from library by his email **/
    public User findUserByEmail(String email) throws InvalidArgumentsException, ObjectDoesntExistException;

    /** Find all authors from library **/
    public List<User> findAllUsers();

    /** Changes existing user password **/
    public Boolean userChangePassword(String userId, UserPasswordDto userPasswordDto) throws PasswordMismatchException, InvalidArgumentsException, ObjectDoesntExistException, AlreadyExistException;

    /** filters list of objects with params we send **/
    List<User> filterUsers(Map<String, String> allParams) throws InvalidArgumentsException;



}