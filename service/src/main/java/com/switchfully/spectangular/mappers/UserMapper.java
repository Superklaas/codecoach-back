package com.switchfully.spectangular.mappers;

import com.switchfully.spectangular.exceptions.InvalidEmailException;
import com.switchfully.spectangular.exceptions.Role;
import com.switchfully.spectangular.exceptions.User;
import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.dtos.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserDto dto){

        try {
            return new User(dto.getFirstName(), dto.getLastName(), dto.getProfileName(), dto.getEmail(),
                    dto.getEncryptedPassword(), Role.valueOf(dto.getRole().toUpperCase()) );
        } catch (Exception ex) {
            throw new InvalidEmailException("A user with this email address already exists.");
        }

    }

    public UserDto toDto(User user){
        return new UserDto()
                .setEmail(user.getEmail())
                .setEncryptedPassword(user.getEncryptedPassword())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setId(user.getId())
                .setProfileName(user.getProfileName())
                .setRole(user.getRole().toString());
    }
}
