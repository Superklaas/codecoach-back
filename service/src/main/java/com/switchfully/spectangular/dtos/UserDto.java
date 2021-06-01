package com.switchfully.spectangular.dtos;

import com.switchfully.spectangular.domain.Topic;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.Objects;

public class UserDto {


    private Integer id;
    private String firstName;
    private String lastName;
    private String profileName;
    private String email;
    private String role;
    private String availability;
    private String introduction;
    private String imageUrl;
    private List<TopicDto> topicList;
    private int xp;

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

    public String getRole() {
        return role;
    }

    public UserDto setRole(String role) {
        this.role = role;
        return this;
    }

    public String getAvailability() {
        return availability;
    }

    public UserDto setAvailability(String availability) {
        this.availability = availability;
        return this;
    }

    public String getIntroduction() {
        return introduction;
    }

    public UserDto setIntroduction(String introduction) {
        this.introduction = introduction;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public UserDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public List<TopicDto> getTopicList() {
        return topicList;
    }

    public UserDto setTopicList(List<TopicDto> topicList) {
        this.topicList = topicList;
        return this;
    }

    public int getXp() {
        return xp;
    }

    public UserDto setXp(int xp) {
        this.xp = xp;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) && Objects.equals(firstName, userDto.firstName) && Objects.equals(lastName, userDto.lastName) && Objects.equals(profileName, userDto.profileName) && Objects.equals(email, userDto.email) && Objects.equals(role, userDto.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, profileName, email, role);
    }
}
