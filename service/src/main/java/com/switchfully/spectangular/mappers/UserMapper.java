package com.switchfully.spectangular.mappers;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.dtos.UpdateCoachProfileDto;
import com.switchfully.spectangular.dtos.UpdateUserProfileDto;
import com.switchfully.spectangular.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final TopicMapper topicMapper;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder, TopicMapper topicMapper) {
        this.passwordEncoder = passwordEncoder;
        this.topicMapper = topicMapper;
    }

    public User toEntity(CreateUserDto dto) {
        return new User(dto.getFirstName(), dto.getLastName(), dto.getProfileName(), dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()), Role.COACHEE);
    }

    public UserDto toDto(User user) {
        return new UserDto()
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setId(user.getId())
                .setProfileName(user.getProfileName())
                .setRole(user.getRole().toString())
                .setAvailability(user.getAvailability())
                .setIntroduction(user.getIntroduction())
                .setImageUrl(user.getImageUrl())
                .setTopicList(topicMapper.toDto(user.getTopicList()))
                .setXp(user.getXp());

    }

    public List<UserDto> toListOfDtos(List<User> users) {
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }


    public User applyToEntity(UpdateUserProfileDto dto, User user) {
        if(dto.getRole()!=null) {
            user.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        }
        return user.setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setProfileName(dto.getProfileName())
                .setEmail(dto.getEmail())
                .setImageUrl(dto.getImageUrl());
    }

    public User applyToEntity(UpdateCoachProfileDto dto, User user) {
        return user.setAvailability(dto.getAvailability()).setIntroduction(dto.getIntroduction());
    }
}
