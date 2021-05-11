package com.switchfully.spectangular.dtos;

import com.switchfully.spectangular.domain.Role;

import javax.persistence.*;

public class UserDto {


    private Integer id;
    private String firstName;
    private String lastName;
    private String profileName;
    private String email;
    private String encryptedPassword;
    private Role role;

    public UserDto() {
    }

    public Integer getId() {
        return id;
    }

    public UserDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getProfileName() {
        return profileName;
    }

    public UserDto setProfileName(String profileName) {
        this.profileName = profileName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public UserDto setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public UserDto setRole(Role role) {
        this.role = role;
        return this;
    }
}
