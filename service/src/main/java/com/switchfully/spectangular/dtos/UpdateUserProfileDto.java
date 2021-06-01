package com.switchfully.spectangular.dtos;

public class UpdateUserProfileDto {

    String email;
    String firstName;
    String lastName;
    String profileName;
    String imageUrl;
    String role;


    public String getEmail() {
        return email;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "UpdateUserProfileDto{" + "email='" + email + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", profileName='" + profileName + '\'' + ", imageUrl='" + imageUrl + '\'' + ", role='" + role + '\'' + '}';
    }
}
