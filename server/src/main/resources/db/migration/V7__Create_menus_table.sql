create table menus
(
    id           bigserial not null
        constraint menus_pkey
            primary key,
    memo         text      not null,
    date         date      not null,
    time_frame   text      not null,
    workspace_id bigint    not null
        constraint fk_menus_workspace_id_id
            references workspaces
            on update cascade on delete cascade,
    created_at   timestamp not null,
    updated_at   timestamp not null
);
