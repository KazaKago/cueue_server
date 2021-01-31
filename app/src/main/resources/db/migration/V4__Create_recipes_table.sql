create table recipes
(
    id           bigserial not null
        constraint recipes_pkey
            primary key,
    title        text      not null,
    description  text      not null,
    workspace_id bigint    not null
        constraint fk_recipes_workspace_id_id
            references workspaces
            on update cascade on delete cascade,
    created_at   timestamp not null,
    updated_at   timestamp not null
);
