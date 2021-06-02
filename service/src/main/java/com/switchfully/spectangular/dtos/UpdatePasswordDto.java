package com.switchfully.spectangular.dtos;

import java.util.Objects;

public class UpdatePasswordDto {

    private int id;
    private String oldPassword;
    private String newPassword;

    public UpdatePasswordDto() {
    }

    public int getId() {
        return id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public UpdatePasswordDto setId(int id) {
        this.id = id;
        return this;
    }

    public UpdatePasswordDto setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public UpdatePasswordDto setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }
}
