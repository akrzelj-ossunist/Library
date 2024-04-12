package com.maurer.library.controllers.implementation;

import com.maurer.library.controllers.interfaces.AuthorController;
import com.maurer.library.dtos.AuthorDto;
import com.maurer.library.dtos.AuthorResDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.mapper.DataMapper;
import com.maurer.library.models.Author;
import com.maurer.library.services.interfaces.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/author")
@CrossOrigin
public class AuthorControllerImpl implements AuthorController {

    private final AuthorService authorService;
    private final DataMapper dataMapper;

    @Autowired
    public AuthorControllerImpl(AuthorService authorService, DataMapper dataMapper) {
        this.authorService = authorService;
        this.dataMapper = dataMapper;
    }

    @Override
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AuthorResDto> create(@Valid @RequestBody AuthorDto authorDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException, InvalidAuthorException {
        System.out.println(authorDto.getFullName() + " " + authorDto.getBirthday());
        Author createdAuthor = authorService.addAuthor(authorDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(dataMapper.authorToDto(createdAuthor));

    }

    @Override
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AuthorResDto> edit(@PathVariable("id") String authorId, @Valid @RequestBody AuthorDto authorDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException, InvalidAuthorException {

        Author updatedAuthor = authorService.updateAuthor(authorId, authorDto);

        return ResponseEntity.ok().body(dataMapper.authorToDto(updatedAuthor));
    }

    @Override
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String authorId) throws CannotDeleteException, ObjectDoesntExistException, InvalidArgumentsException, InvalidAuthorException {

        boolean deleted = authorService.deleteAuthor(authorId);

        return ResponseEntity.ok().body(deleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResDto> authorProfile(@PathVariable("id") String authorId) throws ObjectDoesntExistException, InvalidArgumentsException {

        Author author = authorService.findByAuthorId(authorId);

        return ResponseEntity.ok().body(dataMapper.authorToDto(author));
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<AuthorResDto>> list(@RequestParam Map<String, String> allParams) {

        List<Author> authors = authorService.findAllAuthors(allParams);

        return ResponseEntity.ok().body(dataMapper.listAuthorToListDto(authors));
    }

    @Override
    @GetMapping("/query")
    public ResponseEntity<List<AuthorResDto>> filterList(@RequestParam Map<String, String> allParams) throws InvalidArgumentsException {

        if (allParams.isEmpty()) throw new InvalidArgumentsException("Sent arguments cannot be null!");

        List<Author> filteredAuthors = authorService.filterAuthors(allParams);

        return ResponseEntity.ok().body(dataMapper.listAuthorToListDto(filteredAuthors));
    }
}
