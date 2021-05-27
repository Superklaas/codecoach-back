package com.switchfully.spectangular.controllers;

import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.dtos.UpdateTopicsDto;
import com.switchfully.spectangular.dtos.UpdateUserProfileDto;
import com.switchfully.spectangular.dtos.UserDto;
import com.switchfully.spectangular.services.TopicService;
import com.switchfully.spectangular.dtos.*;
import com.switchfully.spectangular.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping(path = "users")
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final TopicService topicService;

    @Autowired
    public UserController(UserService userService, TopicService topicService) {
        this.userService = userService;
        this.topicService = topicService;
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
    public UserDto updateProfile(@PathVariable int id, @RequestBody UpdateUserProfileDto updateDto, @RequestHeader (name="Authorization") String token) {
        logger.info("Received PUT request to update a user");
        return userService.updateUser(updateDto, id, token);
    }

    @PostMapping(path = "/forgot-password", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void sendResetToken(@RequestHeader String email, @RequestBody String url) {
        logger.info("Received POST request to set and retrieve a reset token.");
        userService.sendResetToken(email, url);
    }

    @PostMapping(path = "/reset-password", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@RequestParam String token, @RequestBody String newPassword) {
        logger.info("Received POST request to reset the User's password.");
        userService.resetPassword(token, newPassword);
    }

    @PreAuthorize("hasAuthority('GET_ALL_USERS')")
    @GetMapping( produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers() {
        logger.info("Received GET request to get an overview of all the users.");
        return userService.getAll();
    }

    @PreAuthorize(value = "hasAuthority('UPDATE_TOPICS')")
    @PostMapping(path = "/{id}/topics", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserDto updateTopics(Principal principal, @PathVariable int id, @RequestBody List<UpdateTopicsDto> dtoList) {
        logger.info("Received POST request to update topics.");
        return userService.updateTopics(id, dtoList, Integer.parseInt(principal.getName()));
    }

    @PreAuthorize("hasAuthority('UPDATE_PROFILE')")
    @PutMapping(path = "/{id}/coach" , produces = "application/json", consumes = "application/json" )
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateCoach(@PathVariable int id, @RequestBody UpdateCoachProfileDto updateDto,
                                  @RequestHeader (name="Authorization") String token) {
        logger.info("Received PUT request to update a coach");
        return userService.updateCoach(updateDto, id, token);
    }

}

