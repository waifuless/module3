INSERT INTO tag(name)
VALUES ('spa'),
       ('relax'),
       ('gaming'),
       ('LGBT');

INSERT INTO gift_certificate(name, description, price, duration, create_date, last_update_date)
VALUES ('Summer super chill', 'good adventure', 220.2, 120, '2022-01-02 14:00:22.123', '2022-01-02 14:00:22.123'),
       ('Shopping', 'buy anything you want for 40br', 40, 90, '2022-01-03 22:22:21.789', '2022-02-06 14:00:22.123'),
       ('AbilityBox game design', 'interesting courses', 999.99, 30,
        '2022-02-03 22:22:21.999', '2022-02-06 11:00:22.213');

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (1, 4),
       (2, 2),
       (3, 3),
       (3, 4);