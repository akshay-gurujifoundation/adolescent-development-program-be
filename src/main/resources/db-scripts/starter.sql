-- Connect to PostgreSQL as a superuser or admin (e.g., postgres user)

-- Step 1: Create the Database
CREATE
DATABASE adolescent_dev_program;

-- Step 2: Create the User
CREATE
USER adolescent_user WITH PASSWORD 'Adolescent@123';

-- Step 3: Grant All Privileges on the Database to the User
GRANT ALL PRIVILEGES ON DATABASE
adolescent_dev_program TO adolescent_user;

-- Grant usage on the 'public' schema
GRANT USAGE ON SCHEMA
public TO adolescent_user;

-- Grant privileges to create objects in the 'public' schema
GRANT CREATE
ON SCHEMA public TO adolescent_user;

-- Optionally, grant all privileges on the schema
GRANT ALL
ON SCHEMA public TO adolescent_user;


-- Grant all privileges on all tables in the 'public' schema
GRANT ALL PRIVILEGES ON ALL
TABLES IN SCHEMA public TO adolescent_user;

-- Grant all privileges on all sequences in the 'public' schema
GRANT ALL PRIVILEGES ON ALL
SEQUENCES IN SCHEMA public TO adolescent_user;


ALTER
DATABASE adolescent_dev_program OWNER TO adolescent_user;


-- Rev Info
CREATE TABLE revinfo
(
    rev      SERIAL PRIMARY KEY,
    revtstmp BIGINT NOT NULL
);


CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1;


-- School Table
CREATE TABLE school
(
    id                   SERIAL PRIMARY KEY,
    name                 VARCHAR(255) NOT NULL,
    address              VARCHAR(255) NOT NULL,
    phone_number         VARCHAR(15)  NOT NULL,
    principal_name       VARCHAR(255) NOT NULL,
    principal_contact_no VARCHAR(15)  NOT NULL,
    managing_trustee     VARCHAR(255),
    trustee_contact_info VARCHAR(15),
    website              VARCHAR(255),
    created_by           VARCHAR(255) NOT NULL,
    created_at           TIMESTAMP    NOT NULL,
    updated_by           VARCHAR(255) NOT NULL,
    updated_at           TIMESTAMP    NOT NULL
);

CREATE INDEX idx_school_id ON school (id);



CREATE TABLE school_aud
(
    id                   INT          NOT NULL,
    rev                  INT          NOT NULL,
    revtype              SMALLINT     NOT NULL,
    name                 VARCHAR(255) NOT NULL,
    address              VARCHAR(255) NOT NULL,
    phone_number         VARCHAR(15)  NOT NULL,
    principal_name       VARCHAR(255) NOT NULL,
    principal_contact_no VARCHAR(15)  NOT NULL,
    managing_trustee     VARCHAR(255),
    trustee_contact_info VARCHAR(15),
    website              VARCHAR(255),
    created_by           VARCHAR(255) NOT NULL,
    created_at           TIMESTAMP    NOT NULL,
    updated_by           VARCHAR(255) NOT NULL,
    updated_at           TIMESTAMP    NOT NULL,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo (rev)
);


-- Teacher Table
CREATE TABLE teacher
(
    id         SERIAL PRIMARY KEY,
    school_id  INT REFERENCES school (id) NOT NULL,
    name       VARCHAR(255)               NOT NULL,
    experience INT                        NOT NULL,
    created_by VARCHAR(255)               NOT NULL,
    created_at TIMESTAMP                  NOT NULL,
    updated_by VARCHAR(255)               NOT NULL,
    updated_at TIMESTAMP                  NOT NULL
);


CREATE INDEX idx_teacher_school_id ON teacher (school_id);


CREATE TABLE teacher_aud
(
    id         INT          NOT NULL,
    rev        INT          NOT NULL,
    revtype    SMALLINT     NOT NULL,
    school_id  INT          NOT NULL,
    name       VARCHAR(255) NOT NULL,
    experience INT          NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP    NOT NULL,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (school_id) REFERENCES school (id),
    FOREIGN KEY (rev) REFERENCES revinfo (rev)
);


--Parent Table
CREATE TABLE parent
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    occupation   VARCHAR(255),
    phone_number VARCHAR(15)  NOT NULL,
    created_by   VARCHAR(255) NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    updated_by   VARCHAR(255) NOT NULL,
    updated_at   TIMESTAMP    NOT NULL
);


create index idx_parent_id
    on parent (id);



create table parent_aud
(
    id           INTEGER      NOT NULL,
    rev          INTEGER      NOT NULL
        references revinfo,
    revtype      smallint     not null,
    name         VARCHAR(255) NOT NULL,
    occupation   VARCHAR(255),
    phone_number VARCHAR(15)  NOT NULL,
    created_by   VARCHAR(255) NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    updated_by   VARCHAR(255) NOT NULL,
    updated_at   TIMESTAMP    NOT NULL,
    primary key (id, rev)
);


--Student Table
CREATE TABLE student
(
    id                 SERIAL PRIMARY KEY,
    school_id          INT REFERENCES school (id) NOT NULL,
    name               VARCHAR(255)               NOT NULL,
    dob                DATE                       NOT NULL,
    parent_id          INT REFERENCES parent (id) NOT NULL,
    address            VARCHAR(255)               NOT NULL,
    phone_number       VARCHAR(15)                NOT NULL,
    alternative_number VARCHAR(15),
    email              VARCHAR(255)               NOT NULL,
    created_by         VARCHAR(255)               NOT NULL,
    created_at         TIMESTAMP                  NOT NULL,
    updated_by         VARCHAR(255)               NOT NULL,
    updated_at         TIMESTAMP                  NOT NULL
);

create index idx_student_id
    on student (id);

create index idx_student_school_id
    on student (id, school_id);



create table student_aud
(
    id                 INTEGER                    NOT NULL,
    rev                INTEGER                    NOT NULL
        references revinfo,
    revtype            smallint                   not null,
    school_id          INT REFERENCES school (id) NOT NULL,
    name               VARCHAR(255)               NOT NULL,
    dob                DATE                       NOT NULL,
    parent_id          INT REFERENCES parent (id) NOT NULL,
    address            VARCHAR(255)               NOT NULL,
    phone_number       VARCHAR(15)                NOT NULL,
    alternative_number VARCHAR(15),
    email              VARCHAR(255)               NOT NULL,
    created_by         VARCHAR(255)               NOT NULL,
    created_at         TIMESTAMP                  NOT NULL,
    updated_by         VARCHAR(255)               NOT NULL,
    updated_at         TIMESTAMP                  NOT NULL,
    primary key (id, rev)
);



