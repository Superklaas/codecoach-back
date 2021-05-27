package com.switchfully.spectangular.dtos;

import java.util.Objects;

public class CreateTopicDto {

    private String name;

    public String getName() {
        return name;
    }

    public CreateTopicDto setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateTopicDto that = (CreateTopicDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
