package com.maurer.library.services.interfaces;

import com.maurer.library.dtos.RentBookDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.models.RentEntry;
import com.maurer.library.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 *  Functionalities of rent entry service
 **/
public interface RentService {

    /** When user wants to rent a book creates rent entry **/
    public RentEntry rentBook(RentBookDto rentBookDto) throws ObjectDoesntExistException, InvalidArgumentsException, CurrentlyUnavailableException, AlreadyExistException;

    /** When user wants to return book updates rent entry with new values **/
    public RentEntry returnBook(String rentEntryId, String note) throws ObjectDoesntExistException, InvalidArgumentsException, AlreadyExistException;

    RentEntry findRentEntryById(String rentEntry) throws InvalidArgumentsException, ObjectDoesntExistException;

    /** Looks up for all rents some user have **/
    public List<RentEntry> findRentsByUserId(String userId) throws InvalidArgumentsException, ObjectDoesntExistException;

    /** Looks up for all rents some book have **/
    List<RentEntry> findRentsByBookId(String bookId) throws InvalidArgumentsException, ObjectDoesntExistException;

    /** Returns list of all rent entries **/
    List<RentEntry> findAllRentEntries(Map<String, String> allParams);

    /** Filtrates all rent entries by status **/
    List<RentEntry> findAllRentEntriesByStatus(String status) throws InvalidArgumentsException, InvalidEnumException;

    /** Looks up all current rent entries and if some rent is older than 30 days, status of that rent entry changes to expired **/
    public List<RentEntry> setExpiredRentEntries(List<RentEntry> rentEntryList);

    /** Checks for expired entries and then filters list of objects with params we send **/
    List<RentEntry> filterRentEntries(Map<String, String> allParams) throws InvalidArgumentsException, ObjectDoesntExistException;
}
