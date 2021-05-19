INSERT INTO codecoach.topic (topic_id, name)
VALUES (1, 'Java');

INSERT INTO codecoach.topic (topic_id, name)
VALUES (2, 'Angular2+');

insert into user_topics (user_id, topic_id)
values (3, 1), (3, 2), (4, 1) ;
