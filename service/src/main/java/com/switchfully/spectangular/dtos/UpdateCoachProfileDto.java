package com.switchfully.spectangular.dtos;

public class UpdateCoachProfileDto {

    private String availability;
    private String introduction;
    private int xp;

    public String getAvailability() {
        return availability;
    }

    public String getIntroduction() {
        return introduction;
    }

    public int getXp() {
        return xp;
    }

    @Override
    public String toString() {
        return "UpdateCoachProfileDto{" + "availability='" + availability + '\'' + ", introduction='" + introduction + '\'' + ", xp='" + xp + '\'' + '}';
    }
}
