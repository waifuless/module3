create table gift_certificate
(
    id               bigserial
        constraint gift_certificate_pk
            primary key,
    name             varchar(500)   not null,
    description      text           not null,
    price            numeric(20, 2) not null,
    duration         integer        not null,
    create_date      date           not null,
    last_update_date date           not null
);

create unique index gift_certificate_id_uindex
    on gift_certificate (id);

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