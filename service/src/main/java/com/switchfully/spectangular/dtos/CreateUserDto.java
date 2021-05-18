package com.switchfully.spectangular.dtos;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateUserDto that = (CreateUserDto) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(profileName, that.profileName) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, profileName, email, password, role);
    }
}
