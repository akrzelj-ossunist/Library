package com.maurer.library.controllers.interfaces;

import com.maurer.library.dtos.UserDto;
import com.maurer.library.dtos.UserLoginDto;
import com.maurer.library.dtos.UserPasswordDto;
import com.maurer.library.dtos.UserUpdateDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.models.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserController {

    /**
     * Receives new user from client side and sends it to service side
     * In case new object is created returns created object otherwise returns bad request
     **/
    ResponseEntity<User> register(UserDto userDto) throws EmailMismatchException, PasswordMismatchException, AlreadyExistException, InvalidArgumentsException;

    /**
     * Receives login info for user from client side and sends it to service side
     * In case user exists returns true if not returns bad request
     **/
    ResponseEntity<Boolean> login(UserLoginDto userLoginDto) throws InvalidArgumentsException, ObjectDoesntExistException;

    /**
     * Receives info for user we want to edit from client side and sends it to service side
     * In case user is edited returns edited user if not returns bad request
     **/
    ResponseEntity<User> edit(String userId, UserUpdateDto userDto) throws InvalidArgumentsException, ObjectDoesntExistException, AlreadyExistException;

    /**
     * Receives user id of user we want to delete from client side and sends it to service side
     * In case user is deleted returns true if not returns bad request
     **/
    ResponseEntity<Boolean> delete(String userId) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException;

    /**
     * Receives info for user we want to edit password from client side and sends it to service side
     * In case password is edited returns edited user if not returns bad request
     **/
    ResponseEntity<Boolean> changePassword(String userId, UserPasswordDto userPasswordDto) throws InvalidArgumentsException, PasswordMismatchException, ObjectDoesntExistException, AlreadyExistException;

    /** Sends user list from server to client side **/
    ResponseEntity<List<User>> list();

    /** Takes user id from params and looks for user if exist send user to client side if not sends bad req **/
    ResponseEntity<User> profile(String userId) throws ObjectDoesntExistException, InvalidArgumentsException;

    /** Takes all params from link and filters all users then sends that list to client side **/
    ResponseEntity<List<User>> filterList(Map<String, String> allParams) throws InvalidArgumentsException;
}
