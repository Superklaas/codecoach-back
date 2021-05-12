package com.switchfully.spectangular.domain;

import com.switchfully.spectangular.exceptions.InvalidEmailException;
import com.switchfully.spectangular.exceptions.InvalidPasswordException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void WhenUserCreatedWithValidEmailThenAssignEmailToUser(){
        String validEmail = "testemail@Spectangular.com";
        User user = new User("testFirstName","testLastName","TestProfileName",validEmail,"testWachtw00rd", Role.COACHEE);
        assertThat(user.getEmail()).isEqualTo(validEmail);
    }


    @Test
    public void WhenUserCreatedWithUnvalidEmailThenThrowsInvalidMailException(){
        String invalidEmail = "testemailSpectangular.com";
        assertThatExceptionOfType(InvalidEmailException.class)
                .isThrownBy(() -> new User("testFirstName","testLastName","TestProfileName",invalidEmail,"testWachtw00rd", Role.COACHEE) );
    }

    @Test
    public void WhenUserCreatedWithUnvalidEmailThenThrowsInvalidMailException2(){
        String invalidEmail = "testemail@Spectangularcom";
        assertThatExceptionOfType(InvalidEmailException.class)
                .isThrownBy(() -> new User("testFirstName","testLastName","TestProfileName",invalidEmail,"testWachtw00rd", Role.COACHEE) );
    }

    @Test
    public void WhenUserCreatedWithUnvalidEmailThenThrowsInvalidMailException3(){
        String invalidEmail = "testemail@Spectangular.";
        assertThatExceptionOfType(InvalidEmailException.class)
                .isThrownBy(() -> new User("testFirstName","testLastName","TestProfileName",invalidEmail,"testWachtw00rd", Role.COACHEE) );
    }

    @Test
    public void WhenUserCreatedWithUnvalidEmailThenThrowsInvalidMailException4(){
        String invalidEmail = "testemail@.com";
        assertThatExceptionOfType(InvalidEmailException.class)
                .isThrownBy(() -> new User("testFirstName","testLastName","TestProfileName",invalidEmail,"testWachtw00rd", Role.COACHEE) );
    }

    @Test
    public void WhenUserCreatedWithUnvalidEmailThenThrowsInvalidMailException5(){
        String invalidEmail = "@Spectangular.com";
        assertThatExceptionOfType(InvalidEmailException.class)
                .isThrownBy(() -> new User("testFirstName","testLastName","TestProfileName",invalidEmail,"testWachtw00rd", Role.COACHEE) );
    }

    @Test
    public void WhenUserCreatedWithValidPasswordThenAssignPasswordToUser(){
        String validPassword = "Wachtwoord123";
        User user = new User("testFirstName","testLastName","TestProfileName","testemail@Spectangular.com",validPassword, Role.COACHEE);
        assertThat(user.getEncryptedPassword()).isEqualTo(validPassword);
    }

    @Test
    public void WhenUserCreatedWithInvalidPasswordThenThrowsInvalidPasswordException(){
        String invalidPassword = "Wachtwoord";

        assertThatExceptionOfType(InvalidPasswordException.class)
                .isThrownBy(() -> new User("testFirstName","testLastName","TestProfileName","testemail@Spectangular.com",invalidPassword, Role.COACHEE));
    }

    @Test
    public void WhenUserCreatedWithInvalidPasswordThenThrowsInvalidPasswordException2(){
        String invalidPassword = "wachtwoord";

        assertThatExceptionOfType(InvalidPasswordException.class)
                .isThrownBy(() -> new User("testFirstName","testLastName","TestProfileName","testemail@Spectangular.com",invalidPassword, Role.COACHEE));
    }

    @Test
    public void WhenUserCreatedWithInvalidPasswordThenThrowsInvalidPasswordException3(){
        String invalidPassword = "WACHTWOORD123";

        assertThatExceptionOfType(InvalidPasswordException.class)
                .isThrownBy(() -> new User("testFirstName","testLastName","TestProfileName","testemail@Spectangular.com",invalidPassword, Role.COACHEE));
    }

    @Test
    public void WhenUserCreatedWithInvalidPasswordThenThrowsInvalidPasswordException4(){
        String invalidPassword = "";

        assertThatExceptionOfType(InvalidPasswordException.class)
                .isThrownBy(() -> new User("testFirstName","testLastName","TestProfileName","testemail@Spectangular.com",invalidPassword, Role.COACHEE));
    }




}
