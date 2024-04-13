package com.maurer.library.controllers.interfaces;

import com.maurer.library.dtos.AuthorDto;
import com.maurer.library.dtos.AuthorResDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.models.Author;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface AuthorController {

    /**
     * Receives new author from client side and sends it to service side
     * In case new object is created returns created object otherwise returns bad request
     **/
    ResponseEntity<AuthorResDto> create(AuthorDto authorDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException, InvalidAuthorException;

    /**
     * Receives info for author we want to edit from client side and sends it to service side
     * In case author is edited returns edited author if not returns bad request
     **/
    ResponseEntity<AuthorResDto> edit(String authorId, AuthorDto authorDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException, InvalidAuthorException;

    /**
     * Receives author id of author we want to delete from client side and sends it to service side
     * In case author is deleted returns true if not returns bad request
     **/
    ResponseEntity<Boolean> delete(String authorId) throws CannotDeleteException, ObjectDoesntExistException, InvalidArgumentsException, InvalidAuthorException;

    /** Takes author id from params and looks for author if exist send author to client side if not sends bad req **/
    ResponseEntity<AuthorResDto> authorProfile(String authorId) throws ObjectDoesntExistException, InvalidArgumentsException;

    /** Sends list of authors from server to client side **/
    ResponseEntity<List<AuthorResDto>> list(Map<String, String> allParams);

    /** Takes all params from link and filters all authors then sends that list to client side **/
    ResponseEntity<List<AuthorResDto>> filterList(Map<String, String> allParams) throws InvalidArgumentsException;
}
