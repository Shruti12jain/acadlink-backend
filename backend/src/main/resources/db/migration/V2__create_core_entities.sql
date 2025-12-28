--  EXTENSIONS (UUID)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


-- UNIVERSITY TABLE
CREATE TABLE universities (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'UNIVERSITY',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);


-- PROFESSOR TABLE
CREATE TABLE professors (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50) NOT NULL DEFAULT 'PROFESSOR',
    university_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_professor_university
        FOREIGN KEY (university_id)
            REFERENCES universities (id)
            ON DELETE SET NULL
);


-- STUDENT TABLE
CREATE TABLE students (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50) NOT NULL DEFAULT 'STUDENT',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);


-- MANY-TO-MANY: UNIVERSITY ↔ STUDENT
CREATE TABLE university_students (
    university_id UUID NOT NULL,
    student_id UUID NOT NULL,

    PRIMARY KEY (university_id, student_id),

    CONSTRAINT fk_universitystudents_university
        FOREIGN KEY (university_id)
            REFERENCES universities (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_universitystudents_student
        FOREIGN KEY (student_id)
            REFERENCES students (id)
            ON DELETE CASCADE
);


-- MANY-TO-MANY: PROFESSOR ↔ STUDENT
CREATE TABLE professor_students (
    professor_id UUID NOT NULL,
    student_id UUID NOT NULL,

    PRIMARY KEY (professor_id, student_id),

    CONSTRAINT fk_professorstudents_professor
        FOREIGN KEY (professor_id)
            REFERENCES professors (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_professorstudents_student
        FOREIGN KEY (student_id)
            REFERENCES students (id)
            ON DELETE CASCADE
);


-- UPDATE TRIGGERS FOR UPDATED_AT
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_university_timestamp
BEFORE UPDATE ON universities
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_professor_timestamp
BEFORE UPDATE ON professors
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_student_timestamp
BEFORE UPDATE ON students
FOR EACH ROW EXECUTE FUNCTION update_timestamp();
