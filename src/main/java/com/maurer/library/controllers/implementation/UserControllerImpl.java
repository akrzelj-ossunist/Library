package com.maurer.library.controllers.implementation;

import com.maurer.library.aspect.LoggerUtil;
import com.maurer.library.controllers.interfaces.UserController;
import com.maurer.library.dtos.*;
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

import static com.maurer.library.utils.ConvertModel.convertUser;
import static com.maurer.library.utils.ConvertModel.convertUserList;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserControllerImpl implements UserController {


    private final UserService userService;

    @Autowired
    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserResDto> register(@Valid @RequestBody UserDto userDto) throws EmailMismatchException, PasswordMismatchException, AlreadyExistException, InvalidArgumentsException {

        User newUser = userService.addUser(userDto);

        LoggerUtil.logInfo("New User created: " + newUser.getFullName());

        return ResponseEntity.status(HttpStatus.CREATED).body(convertUser(newUser));
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@Valid @RequestBody UserLoginDto userLoginDto) throws ObjectDoesntExistException, InvalidArgumentsException {

        boolean userExists = userService.validateLogin(userLoginDto);

        if(!userExists) throw new ObjectDoesntExistException("User doesn't exists!");

        LoggerUtil.logInfo("User successfully logged in: " + userLoginDto.getEmail());

        return ResponseEntity.status(HttpStatus.FOUND).body(true);
    }

    @Override
    @PutMapping("/edit/{id}")
    public ResponseEntity<UserResDto> edit(@PathVariable("id") String userId, @Valid @RequestBody UserUpdateDto userDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        User updatedUser = userService.updateUser(userId, userDto);

        LoggerUtil.logInfo("Existing User updated: " + updatedUser.getFullName());

        return ResponseEntity.status(HttpStatus.OK).body(convertUser(updatedUser));
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String userId) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        boolean deleted = userService.deleteUser(userId);

        LoggerUtil.logInfo("User successfully deleted!");

        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }


    @Override
    @PutMapping("/change-password/{id}")
    public ResponseEntity<Boolean> changePassword(@PathVariable("id") String userId, @Valid @RequestBody UserPasswordDto userPasswordDto) throws PasswordMismatchException, ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        userService.userChangePassword(userId, userPasswordDto);

        LoggerUtil.logInfo("User successfully changed password!");

        return ResponseEntity.status(HttpStatus.OK).body(true);

    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<UserResDto>> list() {

        List<User> userList = userService.findAllUsers();

        if (userList.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        LoggerUtil.logInfo("List of Users successfully fetched!");

        return ResponseEntity.ok().body(convertUserList(userList));
    }

    @Override
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserResDto> profile(@PathVariable("id") String userId) throws ObjectDoesntExistException, InvalidArgumentsException {

        User userProfile = userService.findUserById(userId);

        LoggerUtil.logInfo("User profile successfully fetched!");

        return ResponseEntity.ok().body(convertUser(userProfile));

    }

    @Override
    @GetMapping("/query")
    public ResponseEntity<List<UserResDto>> filterList(@RequestParam Map<String, String> allParams) throws InvalidArgumentsException {

        if(allParams.isEmpty()) throw new InvalidArgumentsException("Invalid amount of params sent!");

        List<User> filteredList = userService.filterUsers(allParams);

        LoggerUtil.logInfo("List of filtered Users successfully fetched!");

        return ResponseEntity.ok().body(convertUserList(filteredList));
    }
}
