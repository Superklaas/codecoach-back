package com.switchfully.spectangular.dtos;

public class CreateTopicDto {

    private String name;

    public String getName() {
        return name;
    }

    public CreateTopicDto setName(String name) {
        this.name = name;
        return this;
    }
}
