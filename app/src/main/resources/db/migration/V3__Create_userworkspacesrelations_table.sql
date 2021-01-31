create table userworkspacesrelations
(
    id           bigserial not null
        constraint userworkspacesrelations_pkey
            primary key,
    user_id      bigint    not null
        constraint fk_userworkspacesrelations_user_id_id
            references users
            on update cascade on delete cascade,
    workspace_id bigint    not null
        constraint fk_userworkspacesrelations_workspace_id_id
            references workspaces
            on update cascade on delete cascade,
    created_at   timestamp not null,
    updated_at   timestamp not null
);

