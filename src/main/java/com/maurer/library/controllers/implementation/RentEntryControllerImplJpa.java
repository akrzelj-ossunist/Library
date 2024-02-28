package com.maurer.library.controllers.implementation;

import com.maurer.library.controllers.interfaces.RentEntryController;
import com.maurer.library.dtos.RentBookDto;
import com.maurer.library.exceptions.AlreadyExistException;
import com.maurer.library.exceptions.CurrentlyUnavailableException;
import com.maurer.library.exceptions.InvalidArgumentsException;
import com.maurer.library.exceptions.ObjectDoesntExistException;
import com.maurer.library.models.RentEntry;
import com.maurer.library.services.interfaces.RentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jpa/rent-entry")
@CrossOrigin
public class RentEntryControllerImplJpa implements RentEntryController {

    @Autowired
    private RentService rentService;

    public RentEntryControllerImplJpa(RentService rentService) {
        this.rentService = rentService;
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<RentEntry> create(@RequestParam Map<String, String> allParams) throws ObjectDoesntExistException, CurrentlyUnavailableException, AlreadyExistException, InvalidArgumentsException {

        RentBookDto rentBookDto = RentBookDto.builder()
                    .bookId(allParams.get("bookId"))
                    .userId(allParams.get("userId"))
                    .build();

        RentEntry rentEntry = rentService.rentBook(rentBookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(rentEntry);
    }

    @Override
    @PutMapping("/return/{id}")
    public ResponseEntity<RentEntry> returnBook(@PathVariable("id") String rentEntryId,@Valid @RequestParam String note) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        RentEntry returnedRentEntry = rentService.returnBook(rentEntryId, note);

        return ResponseEntity.ok().body(returnedRentEntry);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable("id") String rentEntryId) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        return null;
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<RentEntry>> list() {

        List<RentEntry> rentEntries = rentService.findAllRentEntries();

        return ResponseEntity.ok().body(rentEntries);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<RentEntry> rentEntryPage(@PathVariable("id") String rentEntryId) throws ObjectDoesntExistException, InvalidArgumentsException {

        RentEntry rentEntry = rentService.findRentEntryById(rentEntryId);

        return ResponseEntity.ok().body(rentEntry);
    }

    @Override
    @GetMapping("/query")
    public ResponseEntity<List<RentEntry>> filterList(@RequestParam Map<String, String> allParams) throws InvalidArgumentsException {

        if(allParams.isEmpty()) throw new InvalidArgumentsException("Invalid amount of params sent!");

        List<RentEntry> filteredRentEntries = rentService.filterRentEntries(allParams);

        return ResponseEntity.ok().body(filteredRentEntries);
    }
}
