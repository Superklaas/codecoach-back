package com.switchfully.spectangular.mappers;

import com.switchfully.spectangular.domain.Topic;
import com.switchfully.spectangular.dtos.CreateTopicDto;
import com.switchfully.spectangular.dtos.TopicDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void toDto_givenTopic_thenReturnTopicDto() {
        //GIVEN
        Topic mockTopic = mock(Topic.class);
        when(mockTopic.getId()).thenReturn(1);
        when(mockTopic.getName()).thenReturn("Spring");
        //WHEN
        TopicDto actualTopicDto = topicMapper.toDto(mockTopic);
        //THEN
        assertThat(actualTopicDto).isEqualTo(topicDto);
    }
}
