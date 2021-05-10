package com.switchfully.spectangular.account;

import javax.persistence.*;


@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name="account_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name= "email", unique=true)
    private String email;
    @Column(name="password")
    private String encryptedPassword;
    @Enumerated(value=EnumType.STRING)
    @Column(name="role")
    private Role role;

    public Account(String email, String encryptedPassword, Role role) {
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.role = role;
    }

    public Account() {
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public Role getRole() {
        return role;
    }
}
