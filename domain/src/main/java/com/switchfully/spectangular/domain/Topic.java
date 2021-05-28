package com.switchfully.spectangular.domain;

import javax.persistence.*;
import java.awt.*;
import java.util.Objects;

@Entity
@Table(name = "topic")
public class Topic {

    public static final int MAX_TOPIC_LENGTH = 50;

    @Id
    @Column(name = "name")
    private String name;

    public Topic(String name) {
        this.setName(name);
    }

    public Topic() {
    }

    public String getName() {
        return name;
    }

    public Topic setName(String name) {
        if (name.length() > MAX_TOPIC_LENGTH) {
            throw new IllegalArgumentException("Cannot set a topic name longer than " + MAX_TOPIC_LENGTH + " characters");
        }
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(name, topic.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
