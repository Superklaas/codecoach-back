package com.switchfully.spectangular.repository;

import com.switchfully.spectangular.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Integer> {

    @Query(value = "SELECT * FROM topic WHERE name IN (select topic_name from user_topics)", nativeQuery = true)
    List<Topic> findAllUsedTopics();

}
