package com.switchfully.spectangular.domain;

import javax.persistence.*;

@Entity
public class Session {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Integer id;
}
