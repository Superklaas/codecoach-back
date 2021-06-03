package com.switchfully.spectangular.repository;

import com.switchfully.spectangular.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Integer> {

    @Query(value = "SELECT * FROM topic WHERE name IN (select topic_name from user_topics right join app_user on user_topics.user_id = app_user.user_id where app_user.role = 'COACH')",
            nativeQuery = true)
    List<Topic> findAllUsedTopics();

}
