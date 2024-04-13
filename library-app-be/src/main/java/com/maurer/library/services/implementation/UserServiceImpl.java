package com.maurer.library.services.implementation;

import com.maurer.library.dtos.UserDto;
import com.maurer.library.dtos.UserLoginDto;
import com.maurer.library.dtos.UserPasswordDto;
import com.maurer.library.dtos.UserUpdateDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.mapper.DataMapper;
import com.maurer.library.models.RentEntry;
import com.maurer.library.models.User;
import com.maurer.library.models.enums.UserRole;
import com.maurer.library.repositories.UserRepository;
import com.maurer.library.services.interfaces.RentService;
import com.maurer.library.services.interfaces.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *  Implementation of user service which stores data into java collection
 **/
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RentService rentService;

    private final DataMapper dataMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, @Lazy RentService rentService, DataMapper dataMapper) {
        this.userRepository = userRepository;
        this.rentService = rentService;
        this.dataMapper = dataMapper;
    }

    @Override
    public User addUser(UserDto userDto) throws InvalidArgumentsException, AlreadyExistException, PasswordMismatchException, EmailMismatchException {

        if(userDto == null) throw new InvalidArgumentsException("Entered arguments are invalid!");

        if(!Objects.equals(userDto.getPassword(), userDto.getPasswordRepeat())) throw new PasswordMismatchException("Entered passwords doesn't match!");

        if(!Objects.equals(userDto.getEmail(), userDto.getEmailRepeat())) throw new EmailMismatchException("Entered emails doesn't match!");

        //Check is already in library
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if(existingUser.isPresent()) throw new AlreadyExistException("User already exist in library!");

        return userRepository.save(dataMapper.dtoToUser(userDto));
    }

    @Override
    public Boolean validateLogin(UserLoginDto userLoginDto) throws ObjectDoesntExistException, InvalidArgumentsException {

        if(userLoginDto == null) throw new InvalidArgumentsException("Sent arguments cannot be null!");

        User user = findUserByEmail(userLoginDto.getEmail());

        return user != null && BCrypt.checkpw(userLoginDto.getPassword(), user.getPassword());
    }

    @Override
    public Boolean deleteUser(String userId) throws InvalidArgumentsException, AlreadyExistException, ObjectDoesntExistException {

        if(userId == null) throw new InvalidArgumentsException("Entered user id cannot be null");

        List<RentEntry> rentEntryList = rentService.findRentsByUserId(userId);
        if(!rentEntryList.isEmpty()) throw new AlreadyExistException("User is currently renting a book so he cannot be deleted!");

        userRepository.delete(findUserById(userId));

        return true;
    }

    @Override
    public User updateUser(String userId, UserUpdateDto userDto) throws InvalidArgumentsException, ObjectDoesntExistException, AlreadyExistException {

        if(userId == null || userDto == null) throw new InvalidArgumentsException("Entered user id or user info cannot be null!");

        User user = findUserById(userId);

        return userRepository.save(updatedUser(user, userDto));
    }

    public User updatedUser(User user, UserUpdateDto userDto) {

        user.setFullName(userDto.getFullName());
        user.setBirthday(userDto.getBirthday());
        user.setAddress(userDto.getAddress());

        return user;
    }

    @Override
    public User findUserById(String userId) throws InvalidArgumentsException, ObjectDoesntExistException {

        if(userId == null) throw new InvalidArgumentsException("Entered user id cannot be null");

        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) throw new ObjectDoesntExistException("User you are looking for doesn't exist!");

        return user.get();
    }

    @Override
    public User findUserByEmail(String email) throws InvalidArgumentsException, ObjectDoesntExistException {

        if(email == null) throw new InvalidArgumentsException("Entered user id cannot be null");

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()) throw new ObjectDoesntExistException("User you are looking for doesn't exist!");

        return user.get();
    }

    @Override
    public List<User> findAllUsers(Map<String, String> allParams) {

        int page = allParams.get("page") != null ? Integer.parseInt(allParams.get("page")) - 1 : 0;
        int size = allParams.get("size") != null ? Integer.parseInt(allParams.get("size")) : 10;

        Pageable pageable = (Pageable) PageRequest.of(page, size);

        return userRepository.findAll(pageable).getContent();
    }

    @Override
    public Boolean userChangePassword(String userId, UserPasswordDto userPasswordDto) throws PasswordMismatchException, InvalidArgumentsException, ObjectDoesntExistException, AlreadyExistException {

        if(userId == null || userPasswordDto == null) throw new InvalidArgumentsException("Entered user id or user info cannot be null!");

        if(!Objects.equals(userPasswordDto.getPassword(), userPasswordDto.getPasswordRepeat())) throw new PasswordMismatchException("Entered passwords doesn't match!");

        User user = findUserById(userId);
        if(user == null) throw new ObjectDoesntExistException("User doesn't exist in library!");

        user.setPassword(BCrypt.hashpw(userPasswordDto.getPassword(), BCrypt.gensalt(10)));

        userRepository.save(user);

        return true;
    }

    @Override
    public List<User> filterUsers(Map<String, String> allParams) throws InvalidArgumentsException {

        int page = allParams.get("page") != null ? Integer.parseInt(allParams.get("page")) - 1 : 0;
        int size = allParams.get("size") != null ? Integer.parseInt(allParams.get("size")) : 10;

        Pageable pageable = (Pageable) PageRequest.of(page, size);

        return userRepository.findByFullNameAndAddressAndEmail(
                allParams.get("fullName"),
                allParams.get("address"),
                allParams.get("email"),
                pageable).getContent();
    }
}
