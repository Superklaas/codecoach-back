package com.switchfully.spectangular.exceptions;

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

    private static final String EMAILREGEX = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

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
        if (/*!emailAddress.matches(EMAILREGEX)*/ !EmailValidator.getInstance().isValid(emailAddress)) {
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

    public boolean isPasswordCorrect(String password){
        return encryptedPassword.equals(password);
    }
}