package com.switchfully.spectangular.services;


import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.Topic;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.dtos.*;
import com.switchfully.spectangular.exceptions.*;
import com.switchfully.spectangular.mappers.TopicMapper;
import com.switchfully.spectangular.mappers.UserMapper;
import com.switchfully.spectangular.repository.TopicRepository;
import com.switchfully.spectangular.repository.UserRepository;
import com.switchfully.spectangular.services.mailing.EmailService;
import com.switchfully.spectangular.services.timertasks.RemoveResetTokenTimerTask;
import com.switchfully.spectangular.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.*;


@Service
@Transactional
public class UserService {

    public static final int RESET_TOKEN_EXPIRATION_DELAY = 30 * 60 * 1000; // 30 minutes in milliseconds
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, EmailService emailService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto createUser(CreateUserDto dto) {
        UserValidator.assertValidPassword(dto.getPassword());
        assertEmailDoesNotExist(dto.getEmail());
        User user = userRepository.save(userMapper.toEntity(dto));
        emailService.mailForRegistering(user);
        return userMapper.toDto(user);
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

    public List<UserDto> getAllCoachees() {
        return userMapper.toListOfDtos(userRepository.findUsersByRole(Role.COACHEE));
    }

    public UserDto updateUser(UpdateUserProfileDto dto, int id, int principalId) {
        assertPrincipalCanUpdateProfile(id, principalId);
        if (dto.getRole() != null && !userIsAdmin(principalId)) {
            throw new UnauthorizedException("Only admin can change role.");
        }
        User user = findUserById(id);
        boolean sendMail = isUserBecomingCoach(user, dto);
        User result = userMapper.applyToEntity(dto, user);
        if (sendMail) emailService.mailForBecomingCoach(result);
        return userMapper.toDto(result);
    }

    public int updateXp(User user, int xp){
        user.setXp(user.getXp()+xp);
        return user.getXp();
    }

    public void requestToBecomeCoach(int id, CoachRequestDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found."));
        emailService.mailForCoachRequest(user, dto);
    }



    public UserDto updateCoach(UpdateCoachProfileDto dto, int id, int principalId) {
        assertPrincipalCanUpdateProfile(id, principalId);
        User user = findUserById(id);
        User result = userMapper.applyToEntity(dto, user);
        if(userIsAdmin(principalId)){
            result.setXp(dto.getXp());
        }
        return userMapper.toDto(result);
    }

    public void sendResetToken(String email, String requestUrl) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email address doesn't exist."));
        user.setResetToken(UUID.randomUUID().toString());
        emailService.mailToResetPassword(user, requestUrl);
        expireResetToken(user);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Your password has not been changed. Please try again."));
        user.setEncryptedPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
    }

    public void updatePassword(int uid, UpdatePasswordDto updatePasswordDto) {
        UserValidator.assertValidPassword(updatePasswordDto.getNewPassword());
        User user = this.findUserById(uid);

        if (user.getRole() == Role.ADMIN && updatePasswordDto.getOldPassword() == null){
            this.adminUpdatesPassword(updatePasswordDto);
        }else {
            this.userUpdatesPassword(uid, updatePasswordDto);
        }
    }


    private void userUpdatesPassword(int uid, UpdatePasswordDto updatePasswordDto) {
        User user = this.findUserById(updatePasswordDto.getId());
        assertSameUser(user.getId() ,uid);
        if (!passwordEncoder.matches( updatePasswordDto.getOldPassword(), user.getEncryptedPassword())){
            throw new UnauthorizedException("Old password does not match");
        }
        user.setEncryptedPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
    }

    private void adminUpdatesPassword(UpdatePasswordDto updatePasswordDto) {
        User user = this.findUserById(updatePasswordDto.getId());
        user.setEncryptedPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
    }

    private void expireResetToken(User user) {
        Timer timer = new Timer();
        TimerTask timerTask = new RemoveResetTokenTimerTask(user);
        timer.schedule(timerTask, RESET_TOKEN_EXPIRATION_DELAY);
    }


    private boolean userIsAdmin(int principalId) {
        return this.findUserById(principalId).getRole() == Role.ADMIN;
    }

    private boolean isSameUser(int userResourceId, int requestedBy){
        return requestedBy == userResourceId;
    }

    private boolean isUserBecomingCoach(User user, UpdateUserProfileDto dto) {
        return dto.getRole() != null && !user.getRole().equals(Role.COACH) && dto.getRole().equals(Role.COACH.name());
    }

    private boolean userHasAuthorityToUpdateProfile(int principalId, int id) {
        if (userIsAdmin(principalId)) {
            return true;
        }
        return principalId == id;
    }

    private void assertSameUser(int userResourceId, int requestedBy){
        if (!isSameUser(userResourceId, requestedBy)){
            throw new UnauthorizedException("You are not authorized to make this change");
        }
    }

    public void assertAdmin(int requestedBy) {
        if (!userIsAdmin(requestedBy)) {
            throw new UnauthorizedException("You are not authorized to make this change.");
        }
    }

    private void assertSameUserOrAdmin(int userResourceId, int requestedBy) {
        if (requestedBy != userResourceId) {
            User requester = userRepository.findById(requestedBy).orElseThrow();
            if (!requester.getRole().equals(Role.ADMIN)) {
                throw new UnauthorizedException("You are not authorized to make this change.");
            }
        }
    }

    private void assertEmailDoesNotExist(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException("user already exists with email address: " + email);
        }
    }

    private void assertPrincipalCanUpdateProfile(int id, int principalId) {
        if (!userHasAuthorityToUpdateProfile(principalId, id)) {
            throw new UnauthorizedException("You are not authorized to make changes to this profile.");
        }
    }

    public User findUserByProfileName(String profileName) {
        return userRepository.findDistinctFirstByProfileName(profileName)
                .orElseThrow(()-> new IllegalArgumentException("user not found"));
    }
}
