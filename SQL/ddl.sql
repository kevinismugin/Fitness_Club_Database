CREATE TABLE members (
    member_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE staff (
    staff_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    type VARCHAR(10) CHECK (LOWER(type) IN ('trainer', 'admin'))
);

CREATE TABLE personal_information (
    member_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    year_of_birth INTEGER,
    email VARCHAR(50),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

CREATE TABLE exercise_routines (
    member_id INTEGER NOT NULL,
    routine_id INTEGER NOT NULL,
    exercise VARCHAR(50) NOT NULL,
    total_time INTEGER NOT NULL,
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

CREATE TABLE trainer_schedule (
    schedule_id SERIAL PRIMARY KEY,
    staff_id INTEGER NOT NULL,
    member_id INTEGER,
    time_slot INTEGER NOT NULL,
    is_booked BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

CREATE TABLE group_classes (
    class_id SERIAL PRIMARY KEY,
    staff_id INTEGER NOT NULL,
    start_time INTEGER NOT NULL,
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);

CREATE TABLE class_members (
    class_id INTEGER NOT NULL,
    member_id INTEGER NOT NULL,
    PRIMARY KEY (class_id, member_id),
    FOREIGN KEY (class_id) REFERENCES group_classes(class_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

CREATE TABLE health_metrics (
    member_id SERIAL PRIMARY KEY,
    body_weight INTEGER,
    body_fat_percentage INTEGER,
    heart_rate INTEGER,
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

CREATE TABLE fitness_goals (
    member_id INTEGER NOT NULL,
    goal VARCHAR(255) NOT NULL,
    PRIMARY KEY (member_id, goal),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

CREATE TABLE fitness_achievements (
    member_id INTEGER NOT NULL,
    achievement VARCHAR(50) NOT NULL,
    PRIMARY KEY (member_id, achievement),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

CREATE TABLE room_bookings (
    booking_id SERIAL PRIMARY KEY,
    staff_id INTEGER NOT NULL,
    room_number INTEGER NOT NULL,
    start_time INTEGER NOT NULL,
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);

CREATE TABLE equipments (
    equipment_id SERIAL PRIMARY KEY,
    equipment_type VARCHAR(50) NOT NULL,
    last_maintenance_month INTEGER NOT NULL
);

CREATE TABLE billings (
    member_id SERIAL PRIMARY KEY,
    amount_to_pay INTEGER,
    is_paid BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);