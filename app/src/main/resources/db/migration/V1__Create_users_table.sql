create table users
(
    id         bigserial not null
        constraint users_pkey
            primary key,
    uid        text      not null
        constraint users_uid_unique
            unique,
    created_at timestamp not null,
    updated_at timestamp not null
);

