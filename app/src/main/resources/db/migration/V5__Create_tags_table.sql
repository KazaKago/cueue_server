create table tags
(
    id           bigserial not null
        constraint tags_pkey
            primary key,
    name         text      not null,
    workspace_id bigint    not null
        constraint fk_tags_workspace_id_id
            references workspaces
            on update cascade on delete cascade,
    created_at   timestamp not null,
    updated_at   timestamp not null
);
