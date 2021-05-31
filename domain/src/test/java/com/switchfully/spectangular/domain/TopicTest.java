package com.switchfully.spectangular.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class TopicTest {

    @Test
    void createTopicWithValidNameSetsName(){

        String validTopicName = "TestTopic";

        Topic topic = new Topic(validTopicName);

        assertThat(topic.getName()).isEqualTo(validTopicName);
    }

    @Test
    void createTopicWithEmptyNameThrowsIllegalArgumentException(){

        String invalidTopicName = "";

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( () -> new Topic(invalidTopicName));
    }

    @Test
    void createTopicWithEmptyNameThrowsIllegalArgumentException2(){

        String invalidTopicName = "        ";

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( () -> new Topic(invalidTopicName));
    }

    @Test
    void createTopicWithEmptyNameThrowsIllegalArgumentException3(){

        String invalidTopicName = null;

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( () -> new Topic(invalidTopicName));
    }

    @Test
    void createTopicWithNameThatIsTooLong(){

        String TopicNameWithLEngth51 = "testotestotestotestotestotestotestotestotestotestoT";

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( () -> new Topic(TopicNameWithLEngth51));
    }
}
