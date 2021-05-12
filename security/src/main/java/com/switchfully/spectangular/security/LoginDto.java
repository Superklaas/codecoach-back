package com.switchfully.spectangular.security;

public class LoginDto {

    private String username;
    private String password;

    public LoginDto() {
    }

    public String getUsername() {
        return username;
    }

    public LoginDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginDto setPassword(String password) {
        this.password = password;
        return this;
    }
}
