INSERT INTO members (username, password) VALUES ('kevin1', 'p4ssword');
INSERT INTO members (username, password) VALUES ('kevin2', 'p4ssword');
INSERT INTO members (username, password) VALUES ('kevin3', 'p4ssword');

INSERT INTO staff (username, password, type) VALUES ('chris1', 'password', 'admin');
INSERT INTO staff (username, password, type) VALUES ('chris2', 'password', 'trainer');
INSERT INTO staff (username, password, type) VALUES ('chris3', 'password', 'trainer');

INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (3, NULL, 9, FALSE);
INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (3, NULL, 10, FALSE);
INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (3, NULL, 11, TRUE);
INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (3, NULL, 12, FALSE);
INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (3, NULL, 14, FALSE);
INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (3, 1, 15, TRUE);
INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (3, NULL, 16, FALSE);
INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (2, NULL, 8, TRUE);
INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (2, NULL, 9, FALSE);
INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (2, 1, 11, TRUE);
INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (2, NULL, 12, FALSE);
INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (2, NULL, 17, FALSE);
INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (2, NULL, 18, FALSE);
INSERT INTO trainer_schedule (staff_id, member_id, time_slot, is_booked) VALUES (2, NULL, 19, FALSE);

INSERT INTO personal_information (first_name, last_name, year_of_birth, email) VALUES ('kevin1', 'bob1', 1970, 'kevin1@email.com');
INSERT INTO personal_information (first_name, last_name, year_of_birth, email) VALUES ('kevin2', 'bob2', 1971, 'kevin2@email.com');
INSERT INTO personal_information (first_name, last_name, year_of_birth, email) VALUES ('kevin3', 'bob3', 1972, 'kevin3@email.com');

INSERT INTO exercise_routines (member_id, routine_id, exercise, total_time) VALUES (1, 1, 'bench press', 10);
INSERT INTO exercise_routines (member_id, routine_id, exercise, total_time) VALUES (1, 1, 'running', 15);
INSERT INTO exercise_routines (member_id, routine_id, exercise, total_time) VALUES (2, 1, 'squat', 10);
INSERT INTO exercise_routines (member_id, routine_id, exercise, total_time) VALUES (2, 1, 'pull ups', 12);
INSERT INTO exercise_routines (member_id, routine_id, exercise, total_time) VALUES (2, 2, 'leg press', 20);
INSERT INTO exercise_routines (member_id, routine_id, exercise, total_time) VALUES (2, 2, 'dumbbell curls', 10);
INSERT INTO exercise_routines (member_id, routine_id, exercise, total_time) VALUES (3, 1, 'biking', 25);
INSERT INTO exercise_routines (member_id, routine_id, exercise, total_time) VALUES (3, 1, 'skipping', 10);

INSERT INTO health_metrics (body_weight, body_fat_percentage, heart_rate) VALUES (155, 12, 70);
INSERT INTO health_metrics (body_weight, body_fat_percentage, heart_rate) VALUES (175, 15, 67);
INSERT INTO health_metrics (body_weight, body_fat_percentage, heart_rate) VALUES (190, 13, 60);

INSERT INTO fitness_goals (member_id, goal) VALUES (1, 'Bench 185 lbs');
INSERT INTO fitness_goals (member_id, goal) VALUES (1, 'Gain 5 lbs muscle');
INSERT INTO fitness_goals (member_id, goal) VALUES (2, '10% Body fat percentage');
INSERT INTO fitness_goals (member_id, goal) VALUES (2, 'Run 10km in under an hour');
INSERT INTO fitness_goals (member_id, goal) VALUES (3, 'Lose 10 pounds');

INSERT INTO fitness_achievements (member_id, achievement) VALUES (1, 'Drank 1 L water for a month');
INSERT INTO fitness_achievements (member_id, achievement) VALUES (1, 'Benched 175 lbs');
INSERT INTO fitness_achievements (member_id, achievement) VALUES (2, 'Lost 10 pounds');
INSERT INTO fitness_achievements (member_id, achievement) VALUES (2, 'Ran 5 km in 25 min');
INSERT INTO fitness_achievements (member_id, achievement) VALUES (3, 'Lost 30 pounds');

INSERT INTO billings (member_id, amount_to_pay, is_paid) VALUES (1, 70, FALSE);
INSERT INTO billings (member_id, amount_to_pay, is_paid) VALUES (2, 70, FALSE);
INSERT INTO billings (member_id, amount_to_pay, is_paid) VALUES (3, 70, FALSE);

INSERT INTO group_classes (staff_id, start_time) VALUES (2, 11);
INSERT INTO group_classes (staff_id, start_time) VALUES (3, 15);
INSERT INTO group_classes (staff_id, start_time) VALUES (2, 8);

INSERT INTO class_members (class_id, member_id) VALUES (1, 1);
INSERT INTO class_members (class_id, member_id) VALUES (1, 2);
INSERT INTO class_members (class_id, member_id) VALUES (1, 3);

INSERT INTO room_bookings (staff_id, room_number, start_time) VALUES (2, 101, 8);
INSERT INTO room_bookings (staff_id, room_number, start_time) VALUES (3, 69, 13);

INSERT INTO equipments (equipment_type, last_maintenance_month) VALUES ('bench press machine', 2);
INSERT INTO equipments (equipment_type, last_maintenance_month) VALUES ('treadmill', 5);