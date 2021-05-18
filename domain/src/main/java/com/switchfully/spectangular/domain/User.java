package com.switchfully.spectangular.domain;

import com.switchfully.spectangular.exceptions.InvalidEmailException;
import com.switchfully.spectangular.exceptions.InvalidPasswordException;
import org.apache.commons.validator.routines.EmailValidator;

import javax.persistence.*;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "profile_name")
    private String profileName;

    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String encryptedPassword;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public User() {
    }

    public User(String firstName, String lastName, String profileName, String email, String encryptedPassword, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileName = profileName;
        this.email = validEmail(email);
        this.encryptedPassword = validPassword(encryptedPassword);
        this.role = role;
    }

    public String validEmail(String emailAddress){
        if (!EmailValidator.getInstance().isValid(emailAddress)) {
            throw new InvalidEmailException("user has invalid email address");
        }
        return emailAddress;
    }

    public String validPassword(String password){
       if (password.length() < 8 ){
           throw new InvalidPasswordException("password is invalid");
       }
       if (!password.matches(".*[0-9]+.*")){
           throw new InvalidPasswordException("password is invalid");
       }
        if (!password.matches(".*[A-Z]+.*")){
            throw new InvalidPasswordException("password is invalid");
        }
        if (!password.matches(".*[a-z]+.*")){
            throw new InvalidPasswordException("password is invalid");
        }
        return password;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getEmail() {
        return email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public Role getRole() {
        return role;
    }

}
