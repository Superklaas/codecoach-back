package com.switchfully.spectangular.controllers;

import com.switchfully.spectangular.dtos.TopicDto;
import com.switchfully.spectangular.dtos.UpdateTopicsDto;
import com.switchfully.spectangular.dtos.UserDto;
import com.switchfully.spectangular.services.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "topics")
public class TopicController {

    private final TopicService topicService;
    private final Logger logger = LoggerFactory.getLogger(TopicController.class);

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PreAuthorize(value = "hasAuthority('GET_ALL_TOPICS')")
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<TopicDto> getAllTopics() {
        logger.info("Received GET request to get all topics.");
        return topicService.getAllTopics();
    }

    @PreAuthorize(value = "hasAuthority('GET_ALL_TOPICS')")
    @GetMapping(path="/used" , produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<TopicDto> getAllUsedTopics() {
        logger.info("Received GET request to get all topics.");
        return topicService.getAllUsedTopics();
    }

}
