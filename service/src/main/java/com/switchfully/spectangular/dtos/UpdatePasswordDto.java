package com.switchfully.spectangular.dtos;

import java.util.Objects;

public class UpdatePasswordDto {

    private String email;
    private String oldPassword;
    private String newPassword;

    public UpdatePasswordDto() {
    }

    public String getEmail() {
        return email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public UpdatePasswordDto setEmail(String email) {
        this.email = email;
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
