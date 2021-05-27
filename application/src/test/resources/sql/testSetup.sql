INSERT INTO app_user (user_id, first_name, last_name, email, profile_name, password, role, reset_token)
VALUES (1000, 'Test', 'McTestFace', 'test@spectangular.com', 'T3sty', '$2a$10$IZCN0xQsxP0VKgxm5MItu.Rl2dpD3PMI3j0UNcm89WyrShQX6iAUe', 'COACHEE', 'cba90cb7-4ef5-4fcb-a4a1-6d7177ce75e8');
INSERT INTO app_user (user_id, first_name, last_name, email, profile_name, password, role)
VALUES (1001, 'Coach', 'McCoachFace', 'coach@spectangular.com', 'C0achy', '$2a$10$IZCN0xQsxP0VKgxm5MItu.Rl2dpD3PMI3j0UNcm89WyrShQX6iAUe', 'COACH');

INSERT INTO sessions (session_id, subject, date, start_time, location, remarks, status, coach_id, coachee_id)
VALUES (1000000, 'basic Java', '2030-10-10', '12:00:00', 'F2F', '', 'REQUESTED', 1001, 1000);

INSERT INTO topic (name)
VALUES('Angular')
