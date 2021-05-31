package com.switchfully.spectangular.dtos;

public class UpdateTopicsDto {
    private String name;

    public String getName() {
        return name;
    }

    public UpdateTopicsDto setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "UpdateTopicsDto{" + "name='" + name + '\'' + '}';
    }
}
