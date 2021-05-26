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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    @Mock
    private EmailService emailService;
    @Mock
    private PasswordEncoder passwordEncoder;
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
        assertThatExceptionOfType(EmailNotFoundException.class)
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
        createUserDto.setPassword("123456");
        //WHEN & THEN
        assertThatExceptionOfType(InvalidPasswordException.class)
                .isThrownBy(() -> userService.createUser(createUserDto));
    }

    @Test
    void createUser_givenDuplicateEmailAddress_thenThrowDuplicateEmailException2() {
        //GIVEN
        createUserDto.setPassword("wwww123456");
        //WHEN & THEN
        assertThatExceptionOfType(InvalidPasswordException.class)
                .isThrownBy(() -> userService.createUser(createUserDto));
    }

    @Test
    void createUser_givenDuplicateEmailAddress_thenThrowDuplicateEmailException3() {
        //GIVEN
        createUserDto.setPassword("WWWW123456");
        //WHEN & THEN
        assertThatExceptionOfType(InvalidPasswordException.class)
                .isThrownBy(() -> userService.createUser(createUserDto));
    }

    @Test
    void createUser_givenInvalidPassword_thenThrowInvalidPasswordException() {
        //GIVEN
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        //WHEN & THEN
        assertThatExceptionOfType(DuplicateEmailException.class)
                .isThrownBy(() -> userService.createUser(createUserDto));
    }

    @Test
    void findUserById_givenId_thenReturnUser() {
        //GIVEN
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        //WHEN
        User actualUser = userService.findUserById(userDto.getId());
        //THEN
        verify(userRepository).findById(any());
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    void findUserById_givenNonExistentId_thenThrowIllegalArgumentException() {
        //GIVEN
        int id = 12;
        //WHEN & THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.findUserById(id));
    }

    @Test
    void getUserById_givenId_thenReturnUserDto() {
        //GIVEN
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userMapper.toDto(any())).thenReturn(userDto);
        //WHEN
        UserDto actualUserDto = userService.getUserById(userDto.getId());
        //THEN
        verify(userRepository).findById(any());
        verify(userMapper).toDto(any());
        assertThat(actualUserDto).isEqualTo(userDto);
    }

    @Test
    void updateToCoach_givenId_thenReturnUserWithRoleCoach() {
        //GIVEN
        UserDto expectedUserDto = userDto.setRole(Role.COACH.name());
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userMapper.toDto(any())).thenReturn(expectedUserDto);
        //WHEN
        UserDto actualUserDto = userService.updateToCoach(userDto.getId());
        //THEN
        verify(userRepository).findById(any());
        verify(userMapper).toDto(any());
        assertThat(actualUserDto).isEqualTo(expectedUserDto);
    }

    @Test
    void updateToCoach_givenNonExistentId_thenThrowIllegalArgumentException() {
        //GIVEN
        int id = 12;
        //WHEN & THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.updateToCoach(id));
    }

    @Test
    void getAllCoaches_givenCoachesInDb_thenReturnListOfCoaches() {
        //GIVEN
        user.becomeCoach();
        userDto.setRole(Role.COACH.name());
        when(userRepository.findUsersByRole(any())).thenReturn(List.of(user));
        when(userMapper.toListOfDtos(any())).thenReturn(List.of(userDto));
        //WHEN
        List<UserDto> actualUserDtos = userService.getAllCoaches();
        //THEN
        verify(userRepository).findUsersByRole(any());
        verify(userMapper).toListOfDtos(any());
        assertThat(actualUserDtos).isEqualTo(List.of(userDto));
    }

    @Test
    void sendResetToken_givenEmailAndUrl_thenEmailGetsSend() {
        //GIVEN
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        //WHEN
        userService.sendResetToken(user.getEmail(), "https://www.non-existent-website.org");
        //THEN
        verify(userRepository).findByEmail(any());
        verify(userRepository).save(any());
    }

    @Test
    void sendResetToken_givenNonExistentEmail_thenThrowIllegalArgumentException() {
        //GIVEN
        //WHEN
        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.sendResetToken("email@email.com", "https://www.non-existent-website.org"));
    }

    @Test
    void resetPassword_givenResetTokenAndNewPassword_thenPasswordIsChanged() {
        //GIVEN
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        when(userRepository.findByResetToken(any())).thenReturn(Optional.of(user));
        //WHEN
        userService.resetPassword(token, "newYouC0ach");
        //THEN
        verify(userRepository).findByResetToken(any());
        verify(userRepository).save(any());
    }

    @Test
    void resetPassword_givenNonExistentEmail_thenThrowIllegalArgumentException() {
        //GIVEN
        //WHEN
        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.resetPassword(UUID.randomUUID().toString(), "newYouC0ach"));
    }
}
