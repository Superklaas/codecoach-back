package com.switchfully.spectangular.dtos;

public class TopicDto {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public TopicDto setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TopicDto setName(String name) {
        this.name = name;
        return this;
    }
}
