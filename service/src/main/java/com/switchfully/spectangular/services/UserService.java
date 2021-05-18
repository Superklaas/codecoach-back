package com.switchfully.spectangular.services;


import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.dtos.UserDto;
import com.switchfully.spectangular.exceptions.DuplicateEmailException;
import com.switchfully.spectangular.exceptions.EmailNotFoundException;
import com.switchfully.spectangular.mappers.UserMapper;
import com.switchfully.spectangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }
        throw new EmailNotFoundException("Email not found in system: " + email);
    }

    public UserDto createUser(CreateUserDto dto) {
        throwsExceptionWhenEmailNotUnique(dto.getEmail());

        User user = userRepository.save(userMapper.toEntity(dto));
        return userMapper.toDto(user);
    }

    public void throwsExceptionWhenEmailNotUnique(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException("user already exists with email address: " + email);
        }
    }

    public UserDto findUserById(int id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not " +
                "found")));
    }

    public UserDto updateToCoach(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new IllegalArgumentException("User not found.");
        user.get().becomeCoach();
        return userMapper.toDto(user.get());
    }
}
