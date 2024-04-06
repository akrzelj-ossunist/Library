package com.maurer.library.controllers.implementation;

import com.maurer.library.controllers.interfaces.UserController;
import com.maurer.library.dtos.*;
import com.maurer.library.exceptions.*;
import com.maurer.library.mapper.DataMapper;
import com.maurer.library.models.User;
import com.maurer.library.services.interfaces.UserService;
import com.maurer.library.utils.TokenGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
public class UserControllerImpl implements UserController {

    private final DataMapper dataMapper;
    private final UserService userService;
    private final TokenGenerator tokenGenerator;

    @Autowired
    public UserControllerImpl(DataMapper dataMapper, UserService userService, TokenGenerator tokenGenerator) {
        this.dataMapper = dataMapper;
        this.userService = userService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserResDto> register(@Valid @RequestBody UserDto userDto) throws EmailMismatchException, PasswordMismatchException, AlreadyExistException, InvalidArgumentsException {

        User newUser = userService.addUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dataMapper.userToDto(newUser));
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@Valid @RequestBody UserLoginDto userLoginDto) throws ObjectDoesntExistException, InvalidArgumentsException {

        boolean userExists = userService.validateLogin(userLoginDto);

        if(!userExists) throw new ObjectDoesntExistException("User doesn't exists!");

        User user = userService.findUserByEmail(userLoginDto.getEmail());

        String jwtToken = tokenGenerator.generateToken(String.valueOf(user.getRole()), user.getId());

        LoginResDto loginResDto = LoginResDto.builder()
                .success(true)
                .token(jwtToken)
                .userResDto(dataMapper.userToDto(user))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(loginResDto);
    }

    @Override
    @PutMapping("/edit/{id}")
    public ResponseEntity<UserResDto> edit(@PathVariable("id") String userId, @Valid @RequestBody UserUpdateDto userDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException {

        User updatedUser = userService.updateUser(userId, userDto);

        return ResponseEntity.status(HttpStatus.OK).body(dataMapper.userToDto(updatedUser));
    }

    @Override
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
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
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResDto>> list(@RequestParam Map<String, String> allParams) {

        List<User> userList = userService.findAllUsers(allParams);

        if (userList.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        return ResponseEntity.ok().body(dataMapper.listUserToListDto(userList));
    }

    @Override
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserResDto> profile(@PathVariable("id") String userId) throws ObjectDoesntExistException, InvalidArgumentsException {

            User userProfile = userService.findUserById(userId);

            return ResponseEntity.ok().body(dataMapper.userToDto(userProfile));

    }

    @Override
    @GetMapping("/query")
    //@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UserResDto>> filterList(@RequestParam Map<String, String> allParams) throws InvalidArgumentsException {

        List<User> filteredList = userService.filterUsers(allParams);

        return ResponseEntity.ok().body(dataMapper.listUserToListDto(filteredList));
    }

}

