package com.switchfully.spectangular.controllers;

import com.switchfully.spectangular.dtos.*;
import com.switchfully.spectangular.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "sessions")
public class SessionController {

    private final SessionService sessionService;
    private final Logger logger = LoggerFactory.getLogger(SessionController.class);

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PreAuthorize(value = "hasAuthority('CREATE_SESSION')")
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public SessionDto createSession(Principal principal, @RequestBody CreateSessionDto createSessionDto){
        int uid = Integer.parseInt(principal.getName());

        logger.info("Received POST request to create a new session: " + createSessionDto.toString() + "by user: " + uid);
        return sessionService.createSession(createSessionDto, uid);
    }

    @PreAuthorize(value = "hasAuthority('GET_ALL_SESSIONS')")
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<SessionDto> getAllSessions(){
        return sessionService.getAll();
    }

    @PreAuthorize(value = "hasAuthority('GET_ALL_COACHING_SESSION')")
    @GetMapping(path = "/coach", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<SessionDto> getAllCoachSessions(Principal principal){
        int uid = Integer.parseInt(principal.getName());

        return sessionService.getAllSessionByCoach(uid);
    }

    @PreAuthorize(value = "hasAuthority('GET_ALL_COACHEE_SESSIONS')")
    @GetMapping(path = "/coachee", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<SessionDto> getAllCoacheeSessions(Principal principal){

        int uid = Integer.parseInt(principal.getName());

        return sessionService.getAllSessionByCoachee(uid);
    }

    @PreAuthorize(value = "hasAuthority('GET_SESSION_BY_ID')")
    @GetMapping(path = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public SessionDto getSessionById(@PathVariable int id) {
        return sessionService.getSessionById(id);
    }

    @PreAuthorize(value = "hasAuthority('UPDATE_SESSION')")
    @PostMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public SessionDto updateSession(@PathVariable int id, @RequestBody UpdateSessionDto updateSessionDto){
        logger.info("Received POST request to update session: " + id);
        return sessionService.updateSession(id, updateSessionDto);
    }


    @PreAuthorize(value = "hasAuthority('UPDATE_SESSION_STATUS')")
    @PostMapping(path = "/{id}/status", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SessionDto updateStatus(Principal principal, @PathVariable int id, @RequestBody SessionStatusDto status){
        int uid = Integer.parseInt(principal.getName());

        logger.info("Received POST request to update status of session: " + id + "by user: " + uid + "to status: " + status.toString());
        return sessionService.updateSessionStatus(uid, id, status.getStatus());
    }

    @PostMapping(path="/{id}/feedback-for-coach", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public SessionDto createFeedbackForCoach(Principal principal, @PathVariable int id, @RequestBody AddFeedbackForCoachDto addFeedbackDto) {
        logger.info("Received POST request to create feedback for coach for session: " +
                id + " by user: " + principal.getName() + "to feedback: " + addFeedbackDto.toString());

        int uid = Integer.parseInt(principal.getName());
        return sessionService.addFeedbackForCoach(id, uid, addFeedbackDto);
    }

    @PostMapping(path="/{id}/feedback-for-coachee", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public SessionDto createFeedbackForCoachee(Principal principal, @PathVariable int id, @RequestBody AddFeedbackForCoacheeDto addFeedbackDto) {
        logger.info("Received POST request to create feedback for coachee for session: " +
                id + " by user: " + principal.getName() + "to feedback: " + addFeedbackDto.toString());

        int uid = Integer.parseInt(principal.getName());
        return sessionService.addFeedbackForCoachee(id, uid, addFeedbackDto);
    }

}
