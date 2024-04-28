package com.maurer.library.controllers.implementation;

import com.maurer.library.controllers.interfaces.RentEntryController;
import com.maurer.library.dtos.RentBookDto;
import com.maurer.library.dtos.RentEntryResDto;
import com.maurer.library.exceptions.AlreadyExistException;
import com.maurer.library.exceptions.CurrentlyUnavailableException;
import com.maurer.library.exceptions.InvalidArgumentsException;
import com.maurer.library.exceptions.ObjectDoesntExistException;
import com.maurer.library.mapper.DataMapper;
import com.maurer.library.models.RentEntry;
import com.maurer.library.services.interfaces.RentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rent-entry")
@CrossOrigin
public class RentEntryControllerImpl implements RentEntryController {

    private final DataMapper dataMapper;
    private final RentService rentService;

    @Autowired
    public RentEntryControllerImpl(DataMapper dataMapper, RentService rentService) {
        this.dataMapper = dataMapper;
        this.rentService = rentService;
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<RentEntryResDto> create(@Valid @RequestBody RentBookDto rentBookDto) throws ObjectDoesntExistException, CurrentlyUnavailableException, AlreadyExistException, InvalidArgumentsException {
        System.out.println("\n\nBookId" + rentBookDto.getBookId() + " UserId" + rentBookDto.getUserId());
        RentEntry rentEntry = rentService.rentBook(rentBookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dataMapper.rentEntryToDto(rentEntry));
    }

    @Override
    @PutMapping("/return/{id}")
    public ResponseEntity<RentEntryResDto> returnBook(@PathVariable("id") String rentEntryId,@Valid @RequestParam String note) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        RentEntry returnedRentEntry = rentService.returnBook(rentEntryId, note);

        return ResponseEntity.ok().body(dataMapper.rentEntryToDto(returnedRentEntry));
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable("id") String rentEntryId) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        return null;
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<RentEntryResDto>> list(@RequestParam Map<String, String> allParams) {

        List<RentEntry> rentEntries = rentService.findAllRentEntries(allParams);

        return ResponseEntity.ok().body(dataMapper.listRentToListDto(rentEntries));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<RentEntryResDto> rentEntryPage(@PathVariable("id") String rentEntryId) throws ObjectDoesntExistException, InvalidArgumentsException {

        RentEntry rentEntry = rentService.findRentEntryById(rentEntryId);

        return ResponseEntity.ok().body(dataMapper.rentEntryToDto(rentEntry));
    }

    @Override
    @GetMapping("/query")
    public ResponseEntity<List<RentEntryResDto>> filterList(@RequestParam Map<String, String> allParams) throws InvalidArgumentsException, ObjectDoesntExistException {

        if(allParams.isEmpty()) throw new InvalidArgumentsException("Invalid amount of params sent!");

        List<RentEntry> filteredRentEntries = rentService.filterRentEntries(allParams);

        return ResponseEntity.ok().body(dataMapper.listRentToListDto(filteredRentEntries));
    }
}
