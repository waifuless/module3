INSERT INTO tag(name)
VALUES ('spa'),
       ('relax'),
       ('gaming'),
       ('LGBT');

INSERT INTO actuality_state(name)
VALUES ('ACTUAL'),
       ('ARCHIVED');

INSERT INTO gift_certificate(name, description, price, duration,
                             create_date, last_update_date, state_id, count, successor_id)
VALUES ('Summer super chill', 'good adventure', 220.2, 120,
        '2022-01-02 14:00:22.123', '2022-01-02 14:00:22.123', 1, 12, null),
       ('Shopping', 'buy anything you want for 40br', 40, 90,
        '2022-01-03 22:22:21.789', '2022-02-06 14:00:22.123', 1, 22, null),
       ('AbilityBox game design', 'interesting courses', 999.99, 30,
        '2022-02-03 22:22:21.999', '2022-02-06 11:00:22.213', 1, 32, null);

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (1, 4),
       (2, 2),
       (3, 1),
       (3, 3),
       (3, 4);

INSERT INTO app_user(email)
VALUES ('vova@gmail.com'),
       ('tanya@gmail.com');

INSERT INTO user_order(user_id, price, create_date)
VALUES (1, 100200, '2022-02-04 22:22:21.999'),
       (2, 220.2, '2022-01-03 14:00:22.123');

INSERT INTO user_order_position(user_order_id, gift_certificate_id, count)
VALUES (1, 3, 12),
       (1, 1, 1),
       (2, 1, 22);
