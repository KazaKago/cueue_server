create table menurecipesrelations
(
    id         bigserial not null
        constraint menurecipesrelations_pkey
            primary key,
    menu_id    bigint    not null
        constraint fk_menurecipesrelations_menu_id_id
            references menus
            on update cascade on delete cascade,
    recipe_id  bigint    not null
        constraint fk_menurecipesrelations_recipe_id_id
            references recipes
            on update cascade on delete cascade,
    created_at timestamp not null,
    updated_at timestamp not null
);
