package com.switchfully.spectangular.services;

import com.switchfully.spectangular.exceptions.User;
import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.dtos.UserDto;
import com.switchfully.spectangular.mappers.UserMapper;
import com.switchfully.spectangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDto createUser(CreateUserDto dto) {
        User user = userRepository.save(userMapper.toEntity(dto));
        return userMapper.toDto(user);
    }

}
