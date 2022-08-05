create table invitations
(
    id           bigserial not null
        primary key,
    code         text    not null
        unique,
    created_by   bigint    not null
        references users
        on update cascade on delete cascade,
    workspace_id bigint    not null
        references workspaces
        on update cascade on delete cascade,
    created_at   timestamp not null,
    updated_at   timestamp not null
);
