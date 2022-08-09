alter table users add photo_id bigint
    references contents
    on update cascade on delete set null;
