package com.switchfully.spectangular.mappers;

import com.switchfully.spectangular.domain.Topic;
import com.switchfully.spectangular.dtos.CreateTopicDto;
import com.switchfully.spectangular.dtos.TopicDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TopicMapperTest {

    private final TopicMapper topicMapper = new TopicMapper();
    private Topic topic;
    private CreateTopicDto createTopicDto;
    private TopicDto topicDto;

    @BeforeEach
    void setUp() {
        topic = new Topic("Spring");
        createTopicDto = new CreateTopicDto()
                .setName(topic.getName());
        topicDto = new TopicDto()
                .setName(topic.getName());
    }

    @Test
    void toEntity_givenCreateTopicDto_thenReturnTopic() {
        //GIVEN
        //WHEN
        Topic actualTopic = topicMapper.toEntity(createTopicDto);
        //THEN
        assertThat(actualTopic).isEqualTo(topic);
    }

    @Test
    void toDto() {
    }

    @Test
    void testToDto() {
    }
}
