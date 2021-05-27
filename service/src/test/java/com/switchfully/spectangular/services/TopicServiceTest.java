package com.switchfully.spectangular.services;

import com.switchfully.spectangular.domain.Topic;
import com.switchfully.spectangular.dtos.TopicDto;
import com.switchfully.spectangular.mappers.TopicMapper;
import com.switchfully.spectangular.repository.TopicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private TopicMapper topicMapper;
    @InjectMocks
    private TopicService topicService;

    Topic topic = new Topic("Angular");
    TopicDto topicDto = new TopicDto()
            .setId(1)
            .setName(topic.getName());

    @Test
    void getAllTopics_givenTopicsInDb_thenReturnListOfTopicDtos() {
        //GIVEN
        List<Topic> topics = List.of(topic);
        List<TopicDto> topicDtos = List.of(topicDto);
        when(topicRepository.findAll()).thenReturn(topics);
        when(topicMapper.toDto(topics)).thenReturn(topicDtos);
        //WHEN
        List<TopicDto> actualDtos = topicService.getAllTopics();
        //THEN
        verify(topicRepository).findAll();
        verify(topicMapper).toDto(topics);
        assertThat(actualDtos)
                .hasSize(1)
                .isEqualTo(topicDtos);
    }

}
