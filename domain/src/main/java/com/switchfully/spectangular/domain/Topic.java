package com.switchfully.spectangular.domain;

import javax.persistence.*;

@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public Topic(String name) {
        this.name = name;
    }

    public Topic() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
