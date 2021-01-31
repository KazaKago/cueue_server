create table recipetagsrelations
(
    id         bigserial not null
        constraint recipetagsrelations_pkey
            primary key,
    recipe_id  bigint    not null
        constraint fk_recipetagsrelations_recipe_id_id
            references recipes
            on update cascade on delete cascade,
    tag_id     bigint    not null
        constraint fk_recipetagsrelations_tag_id_id
            references tags
            on update cascade on delete cascade,
    created_at timestamp not null,
    updated_at timestamp not null
);
