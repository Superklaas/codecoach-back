package com.switchfully.spectangular.mappers;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.dtos.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    private final UserMapper userMapper = new UserMapper(new PasswordEncoder() {
        @Override
        public String encode(CharSequence charSequence) {
            return charSequence.toString();
        }

        @Override
        public boolean matches(CharSequence charSequence, String s) {
            return charSequence.equals(s);
        }
    });

    private final User expectedUser = new User(
            "Test",
            "McTestFace",
            "Test0r",
            "test_mctestface@mail.com",
            "T3stP@ssw0rd",
            Role.COACHEE);
    private final CreateUserDto createUserDto = new CreateUserDto()
            .setFirstName(expectedUser.getFirstName())
            .setLastName(expectedUser.getLastName())
            .setProfileName(expectedUser.getProfileName())
            .setEmail(expectedUser.getEmail())
            .setPassword(expectedUser.getEncryptedPassword())
            .setRole(expectedUser.getRole().name());
    private final UserDto expectedUserDto = new UserDto()
            .setFirstName(expectedUser.getFirstName())
            .setLastName(expectedUser.getLastName())
            .setProfileName(expectedUser.getProfileName())
            .setEmail(expectedUser.getEmail())
            .setRole(expectedUser.getRole().name());

    @Test
    void toEntity_givenCreateUserDto_thenReturnUser() {
        //GIVEN
        //WHEN
        User actualUser = userMapper.toEntity(createUserDto);
        //THEN
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    void toDto_givenUser_thenReturnUserDto() {
        //GIVEN
        //WHEN
        UserDto actualUserDto = userMapper.toDto(expectedUser);
        //THEN
        assertThat(actualUserDto).isEqualTo(expectedUserDto);
    }

}
