package com.switchfully.spectangular.mappers;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(CreateUserDto dto) {
        return new User(dto.getFirstName(), dto.getLastName(), dto.getProfileName(), dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()), Role.COACHEE);
    }

    public UserDto toDto(User user) {
        return new UserDto()
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setId(user.getId())
                .setProfileName(user.getProfileName())
                .setRole(user.getRole().toString());
    }
}
