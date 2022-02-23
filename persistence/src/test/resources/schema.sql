create table tag
(
    id   bigserial
        constraint tag_pk
            primary key,
    name varchar(100) not null
);

create unique index tag_name_uindex
    on tag (name);

create unique index tag_id_uindex
    on tag (id);

create table app_user
(
    id    bigserial
        constraint app_user_pk
            primary key,
    email varchar(254) not null
);

create unique index app_user_email_uindex
    on app_user (email);

create unique index app_user_id_uindex
    on app_user (id);

create table user_order
(
    id          bigserial
        constraint user_order_pk
            primary key,
    user_id     bigint         not null
        constraint user_order_app_user_id_fk
            references app_user,
    price       numeric(20, 2) not null,
    create_date timestamp      not null
);

create unique index user_order_id_uindex
    on user_order (id);

create table actuality_state
(
    id   bigserial
        constraint actuality_state_pk
            primary key,
    name varchar(50) not null
);

create table gift_certificate
(
    id               bigserial
        constraint gift_certificate_pk
            primary key,
    name             varchar(500)   not null,
    description      text           not null,
    price            numeric(20, 2) not null,
    duration         integer        not null,
    create_date      timestamp      not null,
    last_update_date timestamp      not null,
    count            integer        not null,
    successor_id     bigint
        constraint gift_certificate_gift_certificate_id_fk
            references gift_certificate,
    state_id         bigint         not null
        constraint gift_certificate_actuality_state_id_fk
            references actuality_state
);

create unique index gift_certificate_id_uindex
    on gift_certificate (id);

create unique index gift_certificate_successor_id_uindex
    on gift_certificate (successor_id);

create table gift_certificate_tag
(
    gift_certificate_id bigint not null
        constraint gift_certificate_tag_gift_certificate_id_fk
            references gift_certificate
            on delete cascade,
    tag_id              bigint not null
        constraint gift_certificate_tag_tag_id_fk
            references tag
            on delete cascade,
    constraint gift_certificate_tag_pk
        primary key (gift_certificate_id, tag_id)
);

create table user_order_position
(
    user_order_id       bigint  not null
        constraint user_order_gift_certificate_user_order_id_fk
            references user_order,
    gift_certificate_id bigint  not null
        constraint user_order_gift_certificate_gift_certificate_id_fk
            references gift_certificate,
    count               integer not null
);

create unique index actuality_state_id_uindex
    on actuality_state (id);

create unique index actuality_state_name_uindex
    on actuality_state (name);