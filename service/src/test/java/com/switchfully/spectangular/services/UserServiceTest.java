package com.switchfully.spectangular.services;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.dtos.UserDto;
import com.switchfully.spectangular.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    private CreateUserDto createUserDto;

    @BeforeEach
    private void setUp() {
        User user = new User(
                "Test",
                "McTestFace",
                "TestFace",
                "test_mctestface@email.com",
                "Passw0rd",
                Role.COACHEE
        );
        UserDto userDto = new UserDto()
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setProfileName(user.getProfileName())
                .setEmail(user.getEmail())
                .setEncryptedPassword(user.getEncryptedPassword())
                .setRole(user.getRole().toString());


    }

    @Test
     void findUserByEmail_givenEmailAddress_thenReturnUser() {

    }
}
