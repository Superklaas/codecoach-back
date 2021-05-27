-- MIGRATION OF USER TOPICS SO THAT IT USES A NAME INSTEAD OF AN ID AS PRIMARY KEY

-- First create a copy of the user_topics to use topic_name instead of topic_id
create table user_topics_2
(
    user_id int
        constraint user_topics_2_user_id_fk
            references app_user
            on delete cascade,
    topic_name varchar(255),
    constraint user_topics_2_pk
        primary key (user_id, topic_name)
);

-- Insert the data of user_topics into user_topics_2 but use the topic name isntead of topic id
INSERT INTO user_topics_2 (user_id, topic_name)
    SELECT user_topics.user_id, topic.name
    FROM user_topics JOIN topic ON topic.topic_id = user_topics.topic_id;

-- Drop the original user_topics table
DROP TABLE user_topics;

-- Remove the primary key of topic_id from topicrr
ALTER TABLE topic
    DROP CONSTRAINT topics_pk;

-- Make the name of the topic it's primary key
ALTER TABLE topic
    ADD CONSTRAINT topics_pk
        PRIMARY KEY (name);

-- Add new foreign key in the user topics table
ALTER TABLE user_topics_2
    ADD CONSTRAINT user_topics_topic_name_fk
        FOREIGN KEY(topic_name)
            references topic (name)
            on delete cascade;

-- Drop the topic_id table as we don't need it anymore
ALTER TABLE topic
    DROP COLUMN topic_id;

-- Rename the user_topics_2 to user_topics
ALTER TABLE user_topics_2 RENAME TO user_topics;
