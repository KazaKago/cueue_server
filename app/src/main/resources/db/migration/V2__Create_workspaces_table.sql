create table workspaces
(
    id         bigserial not null
        constraint workspaces_pkey
            primary key,
    name       text      not null,
    type       text      not null,
    created_at timestamp not null,
    updated_at timestamp not null
);

