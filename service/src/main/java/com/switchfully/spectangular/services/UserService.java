package com.switchfully.spectangular.services;


import com.switchfully.spectangular.JSONObjectParser;
import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.Topic;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.dtos.*;
import com.switchfully.spectangular.exceptions.*;
import com.switchfully.spectangular.mappers.TopicMapper;
import com.switchfully.spectangular.mappers.UserMapper;
import com.switchfully.spectangular.repository.TopicRepository;
import com.switchfully.spectangular.repository.UserRepository;
import com.switchfully.spectangular.services.timertasks.RemoveResetTokenTimerTask;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.*;


@Service
@Transactional
public class UserService {

    public static final int RESET_TOKEN_EXPIRATION_DELAY = 30*60*1000; // 30 minutes in milliseconds
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TopicMapper topicMapper;
    private final TopicRepository topicRepository;


    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, EmailService emailService,
                       PasswordEncoder passwordEncoder, TopicMapper topicMapper, TopicRepository topicRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.topicMapper = topicMapper;
        this.topicRepository = topicRepository;
    }

    public UserDto createUser(CreateUserDto dto) {
        assertValidPassword(dto.getPassword());
        assertEmailDoesNotExist(dto.getEmail());
        return userMapper.toDto(userRepository.save(userMapper.toEntity(dto)));
    }

    public UserDto getUserById(int id) {
        return userMapper.toDto(findUserById(id));
    }

    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("Email not found in " +
                "system: " + email));
    }

    public List<UserDto> getAll() {
        return userMapper.toListOfDtos(userRepository.findAll());
    }

    public List<UserDto> getAllCoaches() {
        return userMapper.toListOfDtos(userRepository.findUsersByRole(Role.COACH));
    }

    public UserDto updateUser(UpdateUserProfileDto dto, int id, String token) {
        assertPrincipalCanUpdateProfile(id, token);
        if (dto.getRole() != null && !userIsAdmin(token)) {
            throw new UnauthorizedException("Only admin can change role");
        }
        User user = findUserById(id);
        userMapper.applyToEntity(dto, user);
        User result = userRepository.save(user);
        return userMapper.toDto(result);
    }

    public UserDto updateToCoach(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found."));
        user.becomeCoach();
        return userMapper.toDto(user);
    }

    public UserDto updateCoach(UpdateCoachProfileDto dto, int id, String token) {
        assertPrincipalCanUpdateProfile(id, token);
        User user = findUserById(id);
        userMapper.applyToEntity(dto, user);
        return userMapper.toDto(userRepository.save(user));
    }

    public UserDto updateTopics(int coachId, List<UpdateTopicsDto> topicDtos, int requestedBy) {
        if (topicDtos.size() > User.MAX_COACH_TOPICS) {
            throw new IllegalStateException("Too many topics");
        }
        List<Topic> topics = topicMapper.toEntity(topicDtos);
        assertSameUserOrAdmin(coachId, requestedBy);
        User coach = userRepository.findById(coachId).orElseThrow();
        if (!coach.getRole().equals(Role.COACH) && !findUserById(requestedBy).getRole().equals(Role.ADMIN)) {
            throw new IllegalStateException("Cannot set topics for a non-coach user");
        }
        topics.forEach(topicRepository::save);
        coach.setTopicList(topics);
        return userMapper.toDto(coach);
    }

    public void sendResetToken(String email, String requestUrl) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email address doesn't exist."));
        user.setResetToken(UUID.randomUUID().toString());
        emailService.sendEmailToResetPassword(user, requestUrl);
        expireResetToken(user);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Your password has not been changed. Please try again."));
        user.setEncryptedPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
    }

    private void assertSameUserOrAdmin(int userResourceId, int requestedBy) {
        if (requestedBy != userResourceId) {
            User requester = userRepository.findById(requestedBy).orElseThrow();
            if (!requester.getRole().equals(Role.ADMIN)) {
                throw new UnauthorizedException("You are not authorized to set topics of this coach");
            }
        }
    }

    private void assertEmailDoesNotExist(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException("user already exists with email address: " + email);
        }
    }

    private void assertPrincipalCanUpdateProfile(int id, String token) {
        if (!userHasAuthorityToUpdateProfile(token, id)) {
            throw new UnauthorizedException("You are not authorized to make changes to this profile.");
        }
    }

    private boolean userIsAdmin(String token) {
        JSONObject tokenObject = JSONObjectParser.JwtTokenToJSONObject(token);
        if (tokenObject.get("role").equals("ADMIN")) {
            return true;
        }
        return false;
    }

    private void expireResetToken(User user) {
        Timer timer = new Timer();
        TimerTask timerTask = new RemoveResetTokenTimerTask(user);
        timer.schedule(timerTask, RESET_TOKEN_EXPIRATION_DELAY);
    }

    private void assertValidPassword(String unencryptedPassword) {
        if (unencryptedPassword.length() < 8) {
            throw new InvalidPasswordException("password is invalid");
        }
        if (!unencryptedPassword.matches(".*[0-9]+.*")) {
            throw new InvalidPasswordException("password is invalid");
        }
        if (!unencryptedPassword.matches(".*[A-Z]+.*")) {
            throw new InvalidPasswordException("password is invalid");
        }
        if (!unencryptedPassword.matches(".*[a-z]+.*")) {
            throw new InvalidPasswordException("password is invalid");
        }
    }

    private boolean userHasAuthorityToUpdateProfile(String token, int id) {
        if (userIsAdmin(token)) {
            return true;
        }
        if (getIdFromJwtToken(token) == id) {
            return true;
        }
        return false;
    }

    private int getIdFromJwtToken(String token) {
        JSONObject tokenObject = JSONObjectParser.JwtTokenToJSONObject(token);
        return Integer.parseInt(tokenObject.get("sub").toString());
    }

}
