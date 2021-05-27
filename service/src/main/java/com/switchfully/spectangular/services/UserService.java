package com.switchfully.spectangular.services;


import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.dtos.*;
import com.switchfully.spectangular.exceptions.*;
import com.switchfully.spectangular.mappers.UserMapper;
import com.switchfully.spectangular.repository.UserRepository;
import com.switchfully.spectangular.services.timertasks.RemoveResetTokenTimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.stream.Collectors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
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


    public UserDto updateUser(UpdateUserProfileDto dto, int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("The user you are trying to update does not exist"));
        user.setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setProfileName(dto.getProfileName())
                .setEmail(dto.getEmail())
                .setImageUrl(dto.getImageUrl());
        User result = userRepository.save(user);
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
}
