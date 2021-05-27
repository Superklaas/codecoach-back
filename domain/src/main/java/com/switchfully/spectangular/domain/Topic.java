package com.switchfully.spectangular.domain;

import javax.persistence.*;
import java.awt.*;
import java.util.Objects;

@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @Column(name = "name")
    private String name;

//    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "topicList")
//    private List<User> userList;

    public Topic(String name) {
        this.name = name;
    }

    public Topic() {
    }

    public String getName() {
        return name;
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
