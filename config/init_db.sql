create table resume
(
    uuid      char(36) not null
        constraint resume_pk
            primary key,
    full_name text     not null
);

alter table resume
    owner to postgres;

create table contact
(
    id          serial
        constraint contact_pk
            primary key,
    type        text not null,
    value       text not null,
    resume_uuid char(36) references resume on delete cascade
        constraint contact_resume_uuid_fk

);

alter table contact
    owner to postgres;

create unique index contact_uuid_type_index
    on contact (resume_uuid, type);

create table section
(
    id           serial
        constraint section_pk
            primary key,
    section_type text,
    content      text not null,
    resume_uuid  char(36)
        constraint section_resume_uuid_fk
            references resume
            on delete cascade
);

alter table section
    owner to postgres;

create unique index section_uuid_type_index
    on section (resume_uuid, section_type);