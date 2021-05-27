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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.stream.Collectors;

import java.util.*;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TopicMapper topicMapper;
    private final TopicRepository topicRepository;


    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, EmailService emailService, PasswordEncoder passwordEncoder, TopicMapper topicMapper, TopicRepository topicRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.topicMapper = topicMapper;
        this.topicRepository = topicRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("Email not found in system: " + email));
    }

    public UserDto createUser(CreateUserDto dto) {

        assertValidPassword(dto.getPassword());
        throwsExceptionWhenEmailNotUnique(dto.getEmail());

        User user = userRepository.save(userMapper.toEntity(dto));
        return userMapper.toDto(user);
    }

    public void throwsExceptionWhenEmailNotUnique(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException("user already exists with email address: " + email);
        }
    }

    public UserDto getUserById(int id) {
        return userMapper.toDto(findUserById(id));
    }

    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));
    }

    public UserDto updateToCoach(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("User not found.");
        User user = optionalUser.get();
        user.becomeCoach();
        return userMapper.toDto(user);
    }

    public List<UserDto> getAllCoaches() {
        return userMapper.toListOfDtos(userRepository.findUsersByRole(Role.COACH));
    }


    public UserDto updateUser(UpdateUserProfileDto dto, int id, String token) {
        if(!userHasAuthorityToUpdateProfile(token, id)){
            throw new UnauthorizedException("You are not authorized to make changes to this profile.");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("The user you are trying to update does not exist"));
        user.setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setProfileName(dto.getProfileName())
                .setEmail(dto.getEmail())
                .setImageUrl(dto.getImageUrl());
        User result = userRepository.save(user);
        return userMapper.toDto(result);
    }
    public UserDto updateCoach(UpdateCoachProfileDto dto, int id, String token) {
        if(!userHasAuthorityToUpdateProfile(token, id)){
            throw new UnauthorizedException("You are not authorized to make changes to this profile.");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("The user you are trying to update does not exist"));
        user.setAvailability(dto.getAvailability())
                .setIntroduction(dto.getIntroduction());
        User result = userRepository.save(user);
        System.out.println(user.getAvailability());
        System.out.println(user.getIntroduction());
        return userMapper.toDto(result);
    }

    public void sendResetToken(String email, String requestUrl) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("Email address doesn't exist.");
        User user = optionalUser.get();
        user.setResetToken(UUID.randomUUID().toString());
        sendEmailToResetPassword(user, requestUrl);
        expireResetToken(user);
    }

    public void resetPassword(String token, String newPassword) {
        Optional<User> optionalUser = userRepository.findByResetToken(token);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("Your password has not been changed. Please try again.");
        User user = optionalUser.get();
        user.setEncryptedPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
    }

    public List<UserDto> getAll(){
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    private void sendEmailToResetPassword(User user, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("spectangular.codecoach@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n"
                + url + "/reset-password?token=" + user.getResetToken()
                + "\nThis link will expire in 30 minutes.");
        emailService.sendEmail(message);
    }

    private void expireResetToken(User user) {
        Timer timer = new Timer();
        TimerTask timerTask = new RemoveResetTokenTimerTask(user);
        timer.schedule(timerTask, 1800000); //1800000 = 30 minutes in milliseconds
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

    public boolean userHasAuthorityToUpdateProfile(String token, int id  ) {
        JSONObject tokenObject = JSONObjectParser.JwtTokenToJSONObject(token);
        if(tokenObject.get("role").equals("ADMIN")){
            return true;
        }
        if(getIdFromJwtToken(token)==id){
            return true;
        }
        return false;
    }

    private int getIdFromJwtToken(String token) {
        JSONObject tokenObject = JSONObjectParser.JwtTokenToJSONObject(token);
        return Integer.parseInt(tokenObject.get("sub").toString());
    }

    public UserDto updateTopics(int userId, List<UpdateTopicsDto> topicDtos) {
        List<Topic> topics = topicMapper.toEntity(topicDtos);
        User user = userRepository.findById(userId).orElseThrow();

        topics.forEach(topicRepository::save);
        user.setTopicList(topics);

        return userMapper.toDto(user);
    }
}
