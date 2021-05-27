package com.switchfully.spectangular.domain;

import com.switchfully.spectangular.exceptions.InvalidEmailException;
import com.switchfully.spectangular.exceptions.InvalidPasswordException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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


    @ParameterizedTest
    @ValueSource(strings = {"testemailSpectangular.com", "testemail@Spectangularcom", "testemail@Spectangular.", "testemail@.com", "@Spectangular.com"})
    public void WhenUserCreatedWithInvalidEmailThenThrowsInvalidMailException(String input){
        assertThatExceptionOfType(InvalidEmailException.class)
                .isThrownBy(() -> new User(
                        "testFirstName",
                        "testLastName",
                        "TestProfileName",
                        input,
                        "testWachtw00rd",
                        Role.COACHEE) );
    }
}
