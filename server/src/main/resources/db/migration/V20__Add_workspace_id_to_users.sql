alter table users add workspace_id bigint
    references workspaces
    on update cascade on delete set null;
