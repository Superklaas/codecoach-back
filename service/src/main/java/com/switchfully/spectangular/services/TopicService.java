package com.switchfully.spectangular.services;

import com.switchfully.spectangular.domain.Topic;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.dtos.TopicDto;
import com.switchfully.spectangular.dtos.UpdateTopicsDto;
import com.switchfully.spectangular.dtos.UserDto;
import com.switchfully.spectangular.mappers.TopicMapper;
import com.switchfully.spectangular.mappers.UserMapper;
import com.switchfully.spectangular.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserService userService;
    private final TopicMapper topicMapper;
    private final UserMapper userMapper;

    @Autowired
    public TopicService(TopicRepository topicRepository, UserService userService, TopicMapper topicMapper, UserMapper userMapper) {
        this.topicRepository = topicRepository;
        this.userService = userService;
        this.topicMapper = topicMapper;
        this.userMapper = userMapper;
    }

    public List<TopicDto> getAllTopics() {
        return topicMapper.toDto(topicRepository.findAll());
    }

    public List<TopicDto> getAllUsedTopics() {
        return topicMapper.toDto(topicRepository.findAllUsedTopics());
    }

    public UserDto updateTopicsofCoach(int coachId, List<UpdateTopicsDto> topicDtos, int requestedBy) {
        if (topicDtos.size() > User.MAX_COACH_TOPICS) {
            throw new IllegalStateException("Too many topics");
        }
        userService.assertAdmin(requestedBy);


        User coach = userService.findUserById(coachId);
        List<Topic> topics = topicMapper.toEntity(topicDtos);

        topics.forEach(topicRepository::save);

        List<Topic> oldTopicList =coach.getTopicList();
        coach.setTopicList(topics);
        deleteTopicsIfUnused(oldTopicList);

        return userMapper.toDto(coach);
    }

    private void deleteTopicsIfUnused(List<Topic> topicList){
        List<Topic> usedTopic = topicRepository.findAllUsedTopics();
        topicList.stream().filter(topic -> !usedTopic.contains(topic)).forEach(topicRepository::delete);
    }


}
