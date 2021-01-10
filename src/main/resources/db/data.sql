delete from user_roles;
delete from users;
delete from restaurants;
delete from menus;

insert into users (name, email, password)
values ('User1', 'user1@yandex.ru', 'user1'),
       ('User2', 'user2@yandex.ru', 'user2'),
       ('User3', 'user3@yandex.ru', 'user3'),
       ('Admin', 'admin@gmail.com', 'admin');

insert into user_roles (role, user_id)
values ('USER', 1000),
       ('USER', 1001),
       ('USER', 1002),
       ('ADMIN', 1003);

insert into restaurants (name)
values ('Lerua-Merlo'),
       ('Potato Club');

insert into menus (date, restaurant_id)
values ('2021-01-04', 1000),
       ('2021-01-04', 1001),
       ('2021-01-05', 1000),
       ('2021-01-05', 1001),
       ('2021-01-06', 1000),
       ('2021-01-06', 1001);

insert into dishes (name, price, menu_id)
values ('Potato', '50.50', 1000),
       ('Сrab salad', '180.00', 1000),
       ('Soup kharcho', '90.00', 1001),
       ('Shawarma', '120.00', 1001),
       ('Borscht', '89.90', 1001),
       ('French fries', '130.00', 1002),
       ('Solyanka', '80.00', 1002),
       ('Greek salad', '85.00', 1003),
       ('French fries', '90.00', 1003),
       ('Borscht', '89.90', 1004),
       ('Soup kharcho', '90.00', 1005),
       ('Сrab salad', '180.00', 1005),
       ('Shawarma', '120.00', 1005);

