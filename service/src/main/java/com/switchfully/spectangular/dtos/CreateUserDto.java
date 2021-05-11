package com.switchfully.spectangular.dtos;

public class CreateUserDto {

    private String firstName;
    private String lastName;
    private String profileName;
    private String email;
    private String password;
    private String role;

    public CreateUserDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public CreateUserDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public CreateUserDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getProfileName() {
        return profileName;
    }

    public CreateUserDto setProfileName(String profileName) {
        this.profileName = profileName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CreateUserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CreateUserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getRole() {
        return role;
    }

    public CreateUserDto setRole(String role) {
        this.role = role;
        return this;
    }
}
