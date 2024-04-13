package com.maurer.library.controllers.interfaces;

import com.maurer.library.dtos.*;
import com.maurer.library.exceptions.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserController {

    /**
     * Receives new user from client side and sends it to service side
     * In case new object is created returns created object otherwise returns bad request
     **/
    ResponseEntity<UserResDto> register(UserDto userDto) throws EmailMismatchException, PasswordMismatchException, AlreadyExistException, InvalidArgumentsException;

    /**
     * Receives login info for user from client side and sends it to service side
     * In case user exists returns true if not returns bad request
     **/
    ResponseEntity<LoginResDto> login(UserLoginDto userLoginDto) throws InvalidArgumentsException, ObjectDoesntExistException;

    /**
     * Receives info for user we want to edit from client side and sends it to service side
     * In case user is edited returns edited user if not returns bad request
     **/
    ResponseEntity<UserResDto> edit(String userId, UserUpdateDto userDto) throws InvalidArgumentsException, ObjectDoesntExistException, AlreadyExistException;

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
    ResponseEntity<List<UserResDto>> list(Map<String, String> allParams);

    /** Takes user id from params and looks for user if exist send user to client side if not sends bad req **/
    ResponseEntity<UserResDto> profile(String userId) throws ObjectDoesntExistException, InvalidArgumentsException;

    /** Takes all params from link and filters all users then sends that list to client side **/
    ResponseEntity<List<UserResDto>> filterList(Map<String, String> allParams) throws InvalidArgumentsException;
}
