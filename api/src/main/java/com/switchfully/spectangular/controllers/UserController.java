package com.switchfully.spectangular.controllers;

import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.dtos.UserDto;
import com.switchfully.spectangular.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "users")
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody CreateUserDto createUserDto) {
        logger.info("Received POST request to create a new User.");
        return userService.createUser(createUserDto);
    }

    @PreAuthorize(value = "hasAuthority('GET_USER_INFORMATION')")
    @GetMapping(path = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable int id) {
        logger.info("Received GET request to find a User by it's id.");
        return userService.getUserById(id);
    }

    @PreAuthorize(value = "hasAuthority('BECOME_COACH')")
    @PostMapping(path = "/{id}/coachify", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateToCoach(@PathVariable int id) {
        logger.info("Received POST request to update a User to having a Coach role.");
        return userService.updateToCoach(id);
    }

    @PreAuthorize("hasAuthority('GET_ALL_COACHES')")
    @GetMapping(path = "/coaches", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllCoaches() {
        logger.info("Received GET request to get an overview of all the coaches.");
        return userService.getAllCoaches();
    }

    @PreAuthorize("hasAuthority('UPDATE_PROFILE')")
    @PutMapping(path = "/{id}" , produces = "application/json", consumes = "application/json" )
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateProfile(@PathVariable int id, @RequestBody UserDto userDto ){
        logger.info("Received PUT request to update a user");
        return userService.updateUser(userDto,id);
    }

}

