package com.switchfully.spectangular.repository;

import com.switchfully.spectangular.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TopicRepository extends JpaRepository<Topic, Integer> {

}
