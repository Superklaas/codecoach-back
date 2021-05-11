package com.switchfully.spectangular.mappers;

import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.dtos.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserDto dto){
        return new User(dto.getFirstName(), dto.getLastName(), dto.getProfileName(), dto.getEmail(),
                dto.getEncryptedPassword(), dto.getRole());
    }

    public UserDto toDto(User user){
        return new UserDto()
                .setEmail(user.getEmail())
                .setEncryptedPassword(user.getEncryptedPassword())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setId(user.getId())
                .setProfileName(user.getProfileName())
                .setRole(user.getRole());
    }
}
