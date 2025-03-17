
CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1;

create table revinfo
(
    rev      serial
        primary key,
    revtstmp bigint not null
);

alter table revinfo
    owner to adolescent_user;


create table school
(
    id                   serial
        primary key,
    name                 varchar(255) not null,
    address              varchar(255) not null,
    phone_number         varchar(15),
    principal_name       varchar(255) not null,
    principal_contact_no varchar(15)  not null,
    managing_trustee     varchar(255),
    trustee_contact_info varchar(100),
    website              varchar(255),
    created_by           varchar(255) not null,
    created_at           timestamp    not null,
    updated_by           varchar(255) not null,
    updated_at           timestamp    not null
);

alter table school
    owner to adolescent_user;

create index idx_school_id
    on school (id);

create table school_aud
(
    id                   integer  not null,
    rev                  integer  not null
        references revinfo,
    revtype              smallint not null,
    name                 varchar(255),
    address              varchar(255),
    phone_number         varchar(15),
    principal_name       varchar(255),
    principal_contact_no varchar(15),
    managing_trustee     varchar(255),
    trustee_contact_info varchar(100),
    website              varchar(255),
    created_by           varchar(255),
    created_at           timestamp,
    updated_by           varchar(255),
    updated_at           timestamp,
    primary key (id, rev)
);

alter table school_aud
    owner to adolescent_user;

create table parent
(
    id           serial
        primary key,
    name         varchar(255) not null,
    occupation   varchar(255),
    phone_number varchar(15)  not null,
    created_by   varchar(255) not null,
    created_at   timestamp    not null,
    updated_by   varchar(255) not null,
    updated_at   timestamp    not null
);

alter table parent
    owner to adolescent_user;

create index idx_parent_id
    on parent (id);


create table parent_aud
(
    id           integer      not null,
    rev          integer      not null
        references revinfo,
    revtype      smallint     not null,
    name         varchar(255) not null,
    occupation   varchar(255),
    phone_number varchar(15)  not null,
    created_by   varchar(255) not null,
    created_at   timestamp    not null,
    updated_by   varchar(255) not null,
    updated_at   timestamp    not null,
    primary key (id, rev)
);

alter table parent_aud
    owner to adolescent_user;

create table student
(
    id                 serial
        primary key,
    school_id          integer      not null
        references school,
    name               varchar(255) not null,
    dob                date         not null,
    parent_id          integer      not null
        references parent,
    address            varchar(255) not null,
    phone_number       varchar(15),
    alternative_number varchar(15),
    email              varchar(255),
    created_by         varchar(255) not null,
    created_at         timestamp    not null,
    updated_by         varchar(255) not null,
    updated_at         timestamp    not null
);

alter table student
    owner to adolescent_user;

create index idx_student_id
    on student (id);

create index idx_student_school_id
    on student (id, school_id);

create table student_aud
(
    id                 integer  not null,
    rev                integer  not null
        references revinfo,
    revtype            smallint not null,
    school_id          integer,
    name               varchar(255),
    dob                date,
    parent_id          integer,
    address            varchar(255),
    phone_number       varchar(15),
    alternative_number varchar(15),
    email              varchar(255),
    created_by         varchar(255),
    created_at         timestamp,
    updated_by         varchar(255),
    updated_at         timestamp,
    primary key (id, rev)
);

alter table student_aud
    owner to adolescent_user;


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

alter table project
    owner to adolescent_user;


create table project_aud
(
    id                bigint  not null,
    rev               integer not null,
    revtype           smallint,
    name              varchar(255),
    description       text,
    start_date        date,
    end_date          date,
    actual_start_date date,
    status            varchar(50),
    school_id         bigint,
    created_by        varchar(255),
    created_at        timestamp,
    updated_by        varchar(255),
    updated_at        timestamp,
    primary key (id, rev)
);

alter table project_aud
    owner to adolescent_user;



create table topic
(
    id          serial
        primary key,
    project_id  integer      not null
        references project,
    topic_name  varchar(255) not null,
    created_by  varchar(255) not null,
    created_at  timestamp    not null,
    updated_by  varchar(255) not null,
    updated_at  timestamp    not null,
    description varchar(255)
);

alter table topic
    owner to adolescent_user;

create table topic_aud
(
    id          serial,
    project_id  integer,
    topic_name  varchar(255),
    description varchar(255),
    created_by  varchar(255),
    created_at  timestamp,
    updated_by  varchar(255),
    updated_at  timestamp,
    rev         integer,
    revtype     smallint
);

alter table topic_aud
    owner to adolescent_user;



create table performance
(
    id                       serial
        primary key,
    student_id               integer
        constraint fk_performance_student_id
            references student,
    topic_id                 integer
        constraint fk_performance_project_id
            references topic,
    before_intervention_mark double precision,
    after_intervention_mark  double precision,
    created_by               varchar(255) not null,
    created_at               timestamp    not null,
    updated_by               varchar(255) not null,
    updated_at               timestamp    not null
);

alter table performance
    owner to adolescent_user;

create index idx_performance_student_id
    on performance (student_id);

create index idx_performance_project_id
    on performance (topic_id);

create table performance_aud
(
    id                       bigint  not null,
    rev                      integer not null,
    revtype                  smallint,
    student_id               bigint,
    topic_id                 integer,
    before_intervention_mark double precision,
    after_intervention_mark  double precision,
    created_by               varchar(255),
    created_at               timestamp,
    updated_by               varchar(255),
    updated_at               timestamp,
    primary key (id, rev)
);

alter table performance_aud
    owner to adolescent_user;

create table project_coordinator
(
    id                serial
        primary key,
    name              varchar(255) not null,
    area_of_expertise varchar(255),
    availability      varchar(255),
    mobile_number     varchar(15),
    address           varchar(255),
    created_by        varchar(255) not null,
    created_at        timestamp    not null,
    updated_by        varchar(255) not null,
    updated_at        timestamp    not null
);

alter table project_coordinator
    owner to adolescent_user;


create table project_coordinator_aud
(
    id                serial,
    name              varchar(255),
    area_of_expertise varchar(255),
    availability      varchar(255),
    mobile_number     varchar(15),
    address           varchar(255),
    created_by        varchar(255),
    created_at        timestamp,
    updated_by        varchar(255),
    updated_at        timestamp,
    rev               integer,
    revtype           smallint
);

alter table project_coordinator_aud
    owner to adolescent_user;

create table teacher
(
    id         serial
        primary key,
    school_id  integer      not null
        references school,
    name       varchar(255) not null,
    experience integer      not null,
    created_by varchar(255) not null,
    created_at timestamp    not null,
    updated_by varchar(255) not null,
    updated_at timestamp    not null
);

alter table teacher
    owner to adolescent_user;

create index idx_teacher_school_id
    on teacher (school_id);


create table teacher_aud
(
    id         integer  not null,
    rev        integer  not null
        references revinfo,
    revtype    smallint not null,
    school_id  integer,
    name       varchar(255),
    experience integer,
    created_by varchar(255),
    created_at timestamp,
    updated_by varchar(255),
    updated_at timestamp,
    primary key (id, rev)
);

alter table teacher_aud
    owner to adolescent_user;

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

alter table school_project_mapping
    owner to adolescent_user;

create table school_project_mapping_aud
(
    id                serial,
    rev               integer,
    revtype           smallint,
    project_id        integer
        references project,
    school_id         integer
        references school,
    teacher_id        integer
        references teacher,
    start_date        date,
    end_date          date,
    actual_start_date date,
    actual_end_date   date
);

alter table school_project_mapping_aud
    owner to adolescent_user;


create table school_project_student_mapping
(
    school_project_id integer not null
        references school_project_mapping,
    student_id        integer not null
        references student,
    primary key (school_project_id, student_id)
);

alter table school_project_student_mapping
    owner to adolescent_user;

create table school_project_student_mapping_aud
(
    school_project_id integer,
    student_id        integer,
    rev               integer,
    revtype           smallint
);

alter table school_project_student_mapping_aud
    owner to adolescent_user;

create table student_project
(
    student_id bigint not null
        references student
            on delete cascade,
    project_id bigint not null
        references project
            on delete cascade,
    primary key (student_id, project_id)
);

alter table student_project
    owner to adolescent_user;

create table student_project_aud
(
    id         bigserial,
    student_id bigint,
    project_id bigint,
    rev        integer,
    revtype    smallint
);

alter table student_project_aud
    owner to adolescent_user;



