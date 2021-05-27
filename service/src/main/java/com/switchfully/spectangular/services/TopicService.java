package com.switchfully.spectangular.services;

import com.switchfully.spectangular.dtos.TopicDto;
import com.switchfully.spectangular.mappers.TopicMapper;
import com.switchfully.spectangular.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Autowired
    public TopicService(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    public List<TopicDto> getAllTopics() {
        return topicMapper.toDto(topicRepository.findAll());
    }
}
