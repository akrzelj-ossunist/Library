package com.maurer.library.controllers.implementation;

import com.maurer.library.controllers.interfaces.UserController;
import com.maurer.library.dtos.UserDto;
import com.maurer.library.dtos.UserLoginDto;
import com.maurer.library.dtos.UserPasswordDto;
import com.maurer.library.dtos.UserUpdateDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.models.User;
import com.maurer.library.services.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserControllerImplJpa implements UserController {


    private final UserService userService;

    @Autowired
    public UserControllerImplJpa(UserService userService) {
        this.userService = userService;
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserDto userDto) throws EmailMismatchException, PasswordMismatchException, AlreadyExistException, InvalidArgumentsException {

        User newUser = userService.addUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@Valid @RequestBody UserLoginDto userLoginDto) throws ObjectDoesntExistException, InvalidArgumentsException {

        boolean userExists = userService.validateLogin(userLoginDto);

        if(!userExists) throw new ObjectDoesntExistException("User doesn't exists!");

        return ResponseEntity.status(HttpStatus.FOUND).body(true);
    }

    @Override
    @PutMapping("/edit/{id}")
    public ResponseEntity<User> edit(@PathVariable("id") String userId, @Valid @RequestBody UserUpdateDto userDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        User updatedUser = userService.updateUser(userId, userDto);

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String userId) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        boolean deleted = userService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }


    @Override
    @PutMapping("/change-password/{id}")
    public ResponseEntity<Boolean> changePassword(@PathVariable("id") String userId, @Valid @RequestBody UserPasswordDto userPasswordDto) throws PasswordMismatchException, ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

            userService.userChangePassword(userId, userPasswordDto);

            return ResponseEntity.status(HttpStatus.OK).body(true);

    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<User>> list() {

        List<User> userList = userService.findAllUsers();

        if (userList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok().body(userList);
    }

    @Override
    @GetMapping("/profile/{id}")
    public ResponseEntity<User> profile(@PathVariable("id") String userId) throws ObjectDoesntExistException, InvalidArgumentsException {

            User userProfile = userService.findUserById(userId);

            return ResponseEntity.ok().body(userProfile);

    }

    @Override
    @GetMapping("/query")
    public ResponseEntity<List<User>> filterList(@RequestParam Map<String, String> allParams) throws InvalidArgumentsException {

        if(allParams.isEmpty()) throw new InvalidArgumentsException("Invalid amount of params sent!");

        List<User> filteredList = userService.filterUsers(allParams);

        return ResponseEntity.ok().body(filteredList);
    }
}
