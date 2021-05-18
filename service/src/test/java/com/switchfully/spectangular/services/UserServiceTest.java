package com.switchfully.spectangular.services;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.dtos.UserDto;
import com.switchfully.spectangular.exceptions.DuplicateEmailException;
import com.switchfully.spectangular.mappers.UserMapper;
import com.switchfully.spectangular.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;
    private CreateUserDto createUserDto;

    @BeforeEach
    private void setUp() {
        user = new User(
                "Test",
                "McTestFace",
                "TestFace",
                "test_mctestface@email.com",
                "Passw0rd",
                Role.COACHEE
        );
        userDto = new UserDto()
                .setId(1)
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setProfileName(user.getProfileName())
                .setEmail(user.getEmail())
                .setEncryptedPassword(user.getEncryptedPassword())
                .setRole(user.getRole().toString());

        createUserDto = new CreateUserDto()
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setProfileName(user.getProfileName())
                .setEmail(user.getEmail())
                .setPassword(user.getEncryptedPassword())
                .setRole(user.getRole().toString());

    }

    @Test
     void findUserByEmail_givenEmailAddress_thenReturnUser() {
        //GIVEN
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        //WHEN
        User actualUser = userService.findUserByEmail("test_mctestface@email.com");
        //THEN
        verify(userRepository).findByEmail(any());
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    void findUserByEmail_givenNonExistentEmailAddress_thenThrowIllegalArgumentException() {
        //GIVEN
        String email = "blabla@email.com";
        //WHEN & THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.findUserByEmail(email));
    }

    @Test
    void createUser_givenCreateUserDto_thenReturnUserDto() {
        //GIVEN
        when(userMapper.toDto(any())).thenReturn(userDto);
        //WHEN
        UserDto actualUserDto = userService.createUser(createUserDto);
        //THEN
        verify(userMapper).toEntity(any());
        verify(userRepository).save(any());
        verify(userMapper).toDto(any());
        assertThat(actualUserDto).isEqualTo(userDto);
    }

    @Test
    void createUser_givenDuplicateEmailAddress_thenThrowDuplicateEmailException() {
        //GIVEN
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        //WHEN & THEN
        assertThatExceptionOfType(DuplicateEmailException.class)
                .isThrownBy(() -> userService.createUser(createUserDto));
    }

    @Test
    void findUserById_givenId_thenReturnUserDto() {
        //GIVEN
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userMapper.toDto(any())).thenReturn(userDto);
        //WHEN
        UserDto actualUserDto = userService.findUserById(userDto.getId());
        //THEN
        verify(userRepository).findById(any());
        verify(userMapper).toDto(any());
        assertThat(actualUserDto).isEqualTo(userDto);
    }

    @Test
    void findUserbyId_givenNonExistentId_thenThrowIllegalArgumentException() {
        //GIVEN
        int id = 12;
        //WHEN & THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.findUserById(id));
    }
}
