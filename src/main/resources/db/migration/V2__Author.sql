create table if not exists author
(
    id          serial primary key,
    full_name   varchar(100)       not null,
    create_date timestamp not null
);

alter table budget
    add column if not exists author_id int
        references author(id);