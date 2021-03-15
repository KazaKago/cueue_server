create table contents
(
    id           bigserial not null
        constraint contents_pkey
            primary key,
    key        text      not null,
    recipe_id bigint
        constraint fk_contents_recipe_id_id
            references recipes
            on update cascade on delete set null,
    created_at   timestamp not null,
    updated_at   timestamp not null
);
