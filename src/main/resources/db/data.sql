delete from user_roles;
delete from users;
delete from restaurants;
delete from menus;

insert into users (name, email, password)
values ('Admin', 'admin@yandex.com', 'AdminQwerty1'),
       ('User1', 'user1@yandex.ru', 'Qwerty123'),
       ('User2', 'user2@yandex.ru', 'Qwerty234'),
       ('User3', 'user3@yandex.ru', 'Qwerty345'),
       ('User4', 'user4@yandex.ru', 'Qwerty456'),
       ('User5', 'user5@yandex.ru', 'Qwerty567');


insert into user_roles (role, user_id)
values ('ADMIN', 1000),
       ('USER', 1001),
       ('USER', 1002),
       ('USER', 1003),
       ('USER', 1004),
       ('USER', 1005);


insert into restaurants (name)
values ('Lerua-Merlo'),
       ('Potato Club'),
       ('Banana restaurant');

insert into menus (menu_date, restaurant_id)
values (NOW(), 1000),
        ('2020-01-01', 1000),
       (NOW(), 1001),
       ('2020-01-01', 1001);

insert into dishes (name, price, menu_id)
values ('Potato', '10.00', 1000),
       ('Сrab salad', '60.45', 1000),
       ('Burger', '120.00', 1000),
       ('Soup kharcho', '55.65', 1001),
       ('Shawarma', '80.00', 1001),
       ('Borscht', '89.90', 1002),
       ('Burger', '120.00', 1002),
       ('Сrab salad', '60.45', 1003),
       ('Soup kharcho', '55.65', 1003),
       ('Potato', '10.00', 1003);


insert into votes (vote_date, user_id, restaurant_id)
values (NOW(), 1001, 1000),
       (NOW(), 1002, 1000),
       (NOW(), 1003, 1001);
