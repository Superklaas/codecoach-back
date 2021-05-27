package com.switchfully.spectangular.mappers;

import com.switchfully.spectangular.domain.Topic;
import com.switchfully.spectangular.dtos.CreateTopicDto;
import com.switchfully.spectangular.dtos.TopicDto;
import com.switchfully.spectangular.dtos.UpdateTopicsDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TopicMapper {

    public Topic toEntity(CreateTopicDto dto) {
        return new Topic(dto.getName());
    }

    public Topic toEntity(UpdateTopicsDto dto) {
        return new Topic(dto.getName());
    }

    public List<Topic> toEntity(List<UpdateTopicsDto> topicdtos) {
        return topicdtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public TopicDto toDto(Topic topic) {
        return new TopicDto()
                .setName(topic.getName());
    }

    public List<TopicDto> toDto(List<Topic> topics) {
        if (topics == null) {
            return null;
        }
        return topics.stream().map(this::toDto).collect(Collectors.toList());
    }

}
