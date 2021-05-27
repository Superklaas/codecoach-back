package com.switchfully.spectangular.dtos;

import java.util.Objects;

public class TopicDto {

    private String name;

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
