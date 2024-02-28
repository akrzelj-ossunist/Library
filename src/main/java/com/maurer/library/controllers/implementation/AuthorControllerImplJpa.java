package com.maurer.library.controllers.implementation;

import com.maurer.library.controllers.interfaces.AuthorController;
import com.maurer.library.dtos.AuthorDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.models.Author;
import com.maurer.library.services.interfaces.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/author")
public class AuthorControllerImplJpa implements AuthorController {

    @Autowired
    private AuthorService authorService;

    @Override
    @PostMapping("/create")
    public ResponseEntity<Author> create(@Valid @RequestBody AuthorDto authorDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException, InvalidAuthorException {

        Author createdAuthor = authorService.addAuthor(authorDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);

    }

    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<Author> edit(@PathVariable("id") String authorId, @Valid @RequestBody AuthorDto authorDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException, InvalidAuthorException {

        Author updatedAuthor = authorService.updateAuthor(authorId, authorDto);

        return ResponseEntity.ok().body(updatedAuthor);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String authorId) throws CannotDeleteException, ObjectDoesntExistException, InvalidArgumentsException, InvalidAuthorException {

        boolean deleted = authorService.deleteAuthor(authorId);

        return ResponseEntity.ok().body(deleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Author> authorProfile(@PathVariable("id") String authorId) throws ObjectDoesntExistException, InvalidArgumentsException {

        Author author = authorService.findByAuthorId(authorId);

        return ResponseEntity.ok().body(author);
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<Author>> list() {

        List<Author> authors = authorService.findAllAuthors();

        return ResponseEntity.ok().body(authors);
    }

    @Override
    @GetMapping("/query")
    public ResponseEntity<List<Author>> filterList(@RequestParam Map<String, String> allParams) throws InvalidArgumentsException {

        if (allParams.isEmpty()) throw new InvalidArgumentsException("Sent arguments cannot be null!");

        List<Author> filteredAuthors = authorService.filterAuthors(allParams);

        return ResponseEntity.ok().body(filteredAuthors);
    }
}
