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
    id                   INT      NOT NULL,
    rev                  INT      NOT NULL,
    revtype              SMALLINT NOT NULL,
    name                 VARCHAR(255),
    address              VARCHAR(255),
    phone_number         VARCHAR(15),
    principal_name       VARCHAR(255),
    principal_contact_no VARCHAR(15),
    managing_trustee     VARCHAR(255),
    trustee_contact_info VARCHAR(15),
    website              VARCHAR(255),
    created_by           VARCHAR(255),
    created_at           TIMESTAMP,
    updated_by           VARCHAR(255),
    updated_at           TIMESTAMP,
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
    id         INT      NOT NULL,
    rev        INT      NOT NULL,
    revtype    SMALLINT NOT NULL,
    school_id  INT,
    name       VARCHAR(255),
    experience INT,
    created_by VARCHAR(255),
    created_at TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP,
    PRIMARY KEY (id, rev),
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
    id                 INTEGER  NOT NULL,
    rev                INTEGER  NOT NULL
        references revinfo,
    revtype            smallint NOT NULL,
    school_id          INTEGER NULL,      -- Allow NULL for deletion
    name               VARCHAR(255) NULL, -- Allow NULL for deletion
    dob                DATE NULL,         -- Allow NULL for deletion
    parent_id          INTEGER NULL,      -- Allow NULL for deletion
    address            VARCHAR(255) NULL, -- Allow NULL for deletion
    phone_number       VARCHAR(15) NULL,  -- Allow NULL for deletion
    alternative_number VARCHAR(15) NULL,
    email              VARCHAR(255) NULL, -- Allow NULL for deletion
    created_by         VARCHAR(255),
    created_at         TIMESTAMP,
    updated_by         VARCHAR(255),
    updated_at         TIMESTAMP,
    primary key (id, rev)
);

create table project
(
    id                serial
        primary key,
    name              varchar(255) not null,
    description       text,
    start_date        date,
    end_date          date,
    actual_start_date date,
    actual_end_date   date,
    status            varchar(50),
    created_by        varchar(255) not null,
    created_at        timestamp    not null,
    updated_by        varchar(255) not null,
    updated_at        timestamp    not null
);

CREATE TABLE project_aud
(
    id                BIGINT,
    rev               INT,
    revtype           SMALLINT,     -- Type of revision (0 = ADD, 1 = MODIFY, 2 = DELETE)
    name              VARCHAR(255), -- Audited field 'name'
    description       TEXT,         -- Audited field 'description'
    start_date        DATE,         -- Audited field 'startDate'
    end_date          DATE,         -- Audited field 'endDate'
    actual_start_date DATE,         -- Audited field 'actualStartDate'
    actual_end_date   DATE,         -- Audited field 'actualEndDate'
    status            VARCHAR(50),  -- Audited field 'status'
    created_by        VARCHAR(255) NOT NULL,
    created_at        TIMESTAMP    NOT NULL,
    updated_by        VARCHAR(255) NOT NULL,
    updated_at        TIMESTAMP    NOT NULL,
    PRIMARY KEY (id, rev)           -- Composite primary key
);

CREATE TABLE topic
(
    id          SERIAL PRIMARY KEY,
    project_id  INT REFERENCES Project (id) NOT NULL,
    topic_name  VARCHAR(255)                NOT NULL,
    description VARCHAR(255),
    created_by  VARCHAR(255)                NOT NULL,
    created_at  TIMESTAMP                   NOT NULL,
    updated_by  VARCHAR(255)                NOT NULL,
    updated_at  TIMESTAMP                   NOT NULL
);

CREATE TABLE topic_aud
(
    id          SERIAL,
    project_id  INT,
    topic_name  VARCHAR(255),
    description VARCHAR(255),
    created_by  VARCHAR(255),
    created_at  TIMESTAMP,
    updated_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    rev         INT,     -- Revision number
    revtype     SMALLINT -- Type of revision (0 = ADD, 1 = MODIFY, 2 = DELETE)
);


CREATE TABLE performance
(
    id                       SERIAL PRIMARY KEY,
    student_id               INT,
    topic_id                 INT,
    before_intervention_mark FLOAT,
    after_intervention_mark  FLOAT,
    created_by               VARCHAR(255) NOT NULL,
    created_at               TIMESTAMP    NOT NULL,
    updated_by               VARCHAR(255) NOT NULL,
    updated_at               TIMESTAMP    NOT NULL,
    CONSTRAINT fk_performance_student_id FOREIGN KEY (student_id) REFERENCES student (id),
    CONSTRAINT fk_performance_project_id FOREIGN KEY (topic_id) REFERENCES topic (id)
);

-- Indexes to speed up queries on foreign key columns
CREATE INDEX idx_performance_student_id ON performance (student_id);
create index idx_performance_project_id
    on performance (topic_id);



CREATE TABLE performance_aud
(
    id                       BIGINT,   -- ID of the Performance being audited
    rev                      INT,      -- Revision number
    revtype                  SMALLINT, -- Type of revision (0 = ADD, 1 = MODIFY, 2 = DELETE)
    student_id               BIGINT,   -- Audited field 'student_id' (foreign key reference)
    topic_id                 INT,
    before_intervention_mark FLOAT,
    after_intervention_mark  FLOAT,
    created_by               VARCHAR(255),
    created_at               TIMESTAMP,
    updated_by               VARCHAR(255),
    updated_at               TIMESTAMP,
    PRIMARY KEY (id, rev)              -- Composite primary key
);

CREATE TABLE student_project
(
    student_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    PRIMARY KEY (student_id, project_id),
    FOREIGN KEY (student_id) REFERENCES student (id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE
);

-- Create the audit table for student_project join table
CREATE TABLE student_project_aud
(
    id         BIGSERIAL,
    student_id BIGINT,
    project_id BIGINT,
    rev        INT,     -- Revision number
    revtype    SMALLINT -- Type of revision (0 = ADD, 1 = MODIFY, 2 = DELETE)
);

CREATE TABLE project_coordinator
(
    id                SERIAL PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    area_of_expertise VARCHAR(255),
    availability      VARCHAR(255),
    mobile_number     VARCHAR(15),
    address           VARCHAR(255),
    created_by        VARCHAR(255) NOT NULL,
    created_at        TIMESTAMP    NOT NULL,
    updated_by        VARCHAR(255) NOT NULL,
    updated_at        TIMESTAMP    NOT NULL
);


CREATE TABLE project_coordinator_aud
(
    id                SERIAL,
    name              VARCHAR(255),
    area_of_expertise VARCHAR(255),
    availability      VARCHAR(255),
    mobile_number     VARCHAR(15),
    address           VARCHAR(255),
    created_by        VARCHAR(255),
    created_at        TIMESTAMP,
    updated_by        VARCHAR(255),
    updated_at        TIMESTAMP,
    rev               INT,     -- Revision number
    revtype           SMALLINT -- Type of revision (0 = ADD, 1 = MODIFY, 2 = DELETE)
);

create table school_project_mapping
(
    id                serial
        primary key,
    project_id        integer not null
        references project,
    school_id         integer not null
        references school,
    teacher_id        integer not null
        references teacher,
    start_date        date,
    end_date          date,
    actual_start_date date,
    actual_end_date   date
);

create table school_project_mapping_aud
(
    id                serial,
    rev               integer,
    revtype           smallint,
    project_id        integer,
    school_id         integer,
    teacher_id        integer,
    start_date        date,
    end_date          date,
    actual_start_date date,
    actual_end_date   date
);

create table school_project_student_mapping
(
    school_project_id integer not null
        references school_project_mapping,
    student_id        integer not null
        references student,
    primary key (school_project_id, student_id)
);


create table school_project_student_mapping_aud
(
    school_project_id integer,
    student_id        integer,
    rev               integer,
    revtype           smallint
);



CREATE TABLE project_project_coordinator
(
    project_id     BIGINT NOT NULL,
    coordinator_id BIGINT NOT NULL,
    PRIMARY KEY (project_id, coordinator_id),
    CONSTRAINT fk_project FOREIGN KEY (project_id)
        REFERENCES project (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_project_coordinator FOREIGN KEY (coordinator_id)
        REFERENCES project_coordinator (id)
        ON DELETE CASCADE
);

CREATE TABLE project_project_coordinator_aud
(
    rev            INT,                           -- Envers revision number
    revtype        SMALLINT,                      -- Type of revision (add, mod, del)
    project_id     BIGINT,                        -- Reference to project_id in join table
    coordinator_id BIGINT,                        -- Reference to coordinator_id in join table
    PRIMARY KEY (rev, project_id, coordinator_id) -- Composite primary key
);

ALTER table school_project_mapping
    ADD COLUMN start_date DATE,
ADD COLUMN end_date DATE,
ADD COLUMN actual_start_date DATE,
ADD COLUMN actual_end_date DATE;

create table project_project_coordinator
(
    project_id     bigint not null
        constraint fk_project
            references project
            on delete cascade,
    coordinator_id bigint not null
        constraint fk_project_coordinator
            references project_coordinator
            on delete cascade,
    primary key (project_id, coordinator_id)
);


create table project_project_coordinator_aud
(
    rev            integer not null,
    revtype        smallint,
    project_id     bigint  not null,
    coordinator_id bigint  not null,
    primary key (rev, project_id, coordinator_id)
);

drop table school_project_mapping_aud;
create table school_project_mapping_aud
(
    id                serial,
    rev               integer,
    revtype           smallint,
    project_id        integer,
    school_id         integer,
    teacher_id        integer,
    start_date        date,
    end_date          date,
    actual_start_date date,
    actual_end_date   date
);