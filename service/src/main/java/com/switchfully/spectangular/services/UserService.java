package com.switchfully.spectangular.services;


import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.dtos.UserDto;
import com.switchfully.spectangular.exceptions.DuplicateEmailException;
import com.switchfully.spectangular.exceptions.EmailNotFoundException;
import com.switchfully.spectangular.exceptions.InvalidPasswordException;
import com.switchfully.spectangular.mappers.UserMapper;
import com.switchfully.spectangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new IllegalArgumentException("User not found.");
        user.get().becomeCoach();
        return userMapper.toDto(user.get());
    }

    public List<UserDto> getAllCoaches() {
        return userMapper.toListOfDtos(userRepository.findUsersByRole(Role.COACH));
    }

    public void sendResetToken(String email, String requestUrl) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("Email address doesn't exist.");
        User user = optionalUser.get();
        user.setResetToken(UUID.randomUUID().toString());
        userRepository.save(user);
        sendEmailToResetPassword(user, requestUrl);
    }

    public void resetPassword(String token, String newPassword) {
        Optional<User> optionalUser = userRepository.findByResetToken(token);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("Invalid reset code.");
        User user = optionalUser.get();
        user.setEncryptedPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
    }

    private void sendEmailToResetPassword(User user, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("spectangular.codecoach@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + url
        + "/reset-password\n and enter the following code: " + user.getResetToken());
        emailService.sendEmail(message);
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
