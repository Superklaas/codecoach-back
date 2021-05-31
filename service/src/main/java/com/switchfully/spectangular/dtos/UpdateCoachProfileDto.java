package com.switchfully.spectangular.dtos;

public class UpdateCoachProfileDto {

    private String availability;
    private String introduction;

    public String getAvailability() {
        return availability;
    }

    public String getIntroduction() {
        return introduction;
    }

    @Override
    public String toString() {
        return "UpdateCoachProfileDto{" + "availability='" + availability + '\'' + ", introduction='" + introduction + '\'' + '}';
    }
}
