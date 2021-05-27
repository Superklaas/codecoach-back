package com.switchfully.spectangular.dtos;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicDto topicDto = (TopicDto) o;
        return Objects.equals(name, topicDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
