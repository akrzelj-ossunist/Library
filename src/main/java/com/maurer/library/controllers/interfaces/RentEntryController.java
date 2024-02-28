package com.maurer.library.controllers.interfaces;

import com.maurer.library.exceptions.AlreadyExistException;
import com.maurer.library.exceptions.CurrentlyUnavailableException;
import com.maurer.library.exceptions.InvalidArgumentsException;
import com.maurer.library.exceptions.ObjectDoesntExistException;
import com.maurer.library.models.RentEntry;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface RentEntryController {

    /**
     * Receives new rent entry from client side and sends it to service side
     * In case new object is created returns created object otherwise returns bad request
     **/
    ResponseEntity<RentEntry> create(Map<String, String> allParams) throws ObjectDoesntExistException, CurrentlyUnavailableException, AlreadyExistException, InvalidArgumentsException;

    /**
     * Receives info for rent entry when we want to return book from client side and sends it to service side
     * In case book is returned then we return edited rent entry if not returns bad request
     **/
    ResponseEntity<RentEntry> returnBook(String rentEntryId, String note) throws InvalidArgumentsException, ObjectDoesntExistException, AlreadyExistException;

    /**
     * Receives rent entry id of rent entry we want to delete from client side and sends it to service side
     * In case rent entry is deleted returns true user if not returns bad request
     **/
    Boolean delete(String rentEntryId) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException;

    /** Sends rent entry list from server to client side **/
    ResponseEntity<List<RentEntry>> list();

    /** Takes rent entry id from params and looks for rent entry, if exist send rent entry to client side if not sends bad req **/
    ResponseEntity<RentEntry> rentEntryPage(String rentEntryId) throws ObjectDoesntExistException, InvalidArgumentsException;

    /** Takes all params from link and filters all rent entries then sends that list to client side **/
    ResponseEntity<List<RentEntry>> filterList(Map<String, String> allParams) throws InvalidArgumentsException;
}
