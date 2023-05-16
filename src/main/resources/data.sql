INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (name, phone, address, email)
VALUES ('Sholly', '+7(8512)51-60-57', 'ул. Урицкого, д.3', 'upravl_sholi@am-house.ru'),
       ('Izba', '+7(8512)51-81-91', 'ул. Красная Набережная, д.8', 'izba2012@inbox.ru'),
       ('BeerHouse', '+7(8512)54-72-72', 'ул. Савушкина, д.38', 'pivdom@am-house.ru');

INSERT INTO DISH (name, restaurant_id, price)
VALUES ('Грибной суп', '1', 25000),
       ('Стейк', '1', 80000),
       ('Винегрет', '1', 15000),
       ('Компот', '1', 5000),
       ('Новое блюдо', '1', 40000),
       ('Борщ', '2', 40000),
       ('Плов', '2', 50000),
       ('Оливье', '2', 15000),
       ('Чай', '2', 5000),
       ('Грибной суп', '3', 40000),
       ('Удон', '3', 60000),
       ('Греческий', '3', 20000),
       ('Чай', '3', 5000);

INSERT INTO LUNCH_ITEM(created_date, restaurant_id, dish_id, price)
VALUES (CURRENT_DATE - 2, 1, 1, 10000),
       (CURRENT_DATE - 2, 1, 2, 40000),
       (CURRENT_DATE - 2, 1, 3, 70000),
       (CURRENT_DATE - 2, 1, 4, 2000),
       (CURRENT_DATE - 2, 2, 6, 10000),
       (CURRENT_DATE - 2, 2, 7, 25000),
       (CURRENT_DATE - 2, 2, 8, 70000),
       (CURRENT_DATE - 2, 2, 9, 2500),
       (CURRENT_DATE - 2, 3, 10, 10000),
       (CURRENT_DATE - 2, 3, 11, 20000),
       (CURRENT_DATE - 2, 3, 12, 60000),
       (CURRENT_DATE - 2, 3, 13, 2500),

       (CURRENT_DATE - 1, 1, 1, 10000),
       (CURRENT_DATE - 1, 1, 2, 40000),
       (CURRENT_DATE - 1, 1, 3, 70000),
       (CURRENT_DATE - 1, 1, 4, 2000),
       (CURRENT_DATE - 1, 2, 6, 10000),
       (CURRENT_DATE - 1, 2, 7, 25000),
       (CURRENT_DATE - 1, 2, 8, 70000),
       (CURRENT_DATE - 1, 2, 9, 2500),
       (CURRENT_DATE - 1, 3, 10, 10000),
       (CURRENT_DATE - 1, 3, 11, 20000),
       (CURRENT_DATE - 1, 3, 12, 60000),
       (CURRENT_DATE - 1, 3, 13, 2500),

       (CURRENT_DATE, 1, 1, 10000),
       (CURRENT_DATE, 1, 2, 40000),
       (CURRENT_DATE, 1, 3, 70000),
       (CURRENT_DATE, 1, 4, 2000),
       (CURRENT_DATE, 2, 6, 10000),
       (CURRENT_DATE, 2, 7, 25000),
       (CURRENT_DATE, 2, 8, 70000),
       (CURRENT_DATE, 2, 9, 2500),
       (CURRENT_DATE, 3, 10, 10000),
       (CURRENT_DATE, 3, 11, 20000),
       (CURRENT_DATE, 3, 12, 60000),
       (CURRENT_DATE, 3, 13, 2500);

INSERT INTO VOTE(created_date, created_time, user_id, restaurant_id)
VALUES (CURRENT_DATE - 2, '09:00', 1, 2),
       (CURRENT_DATE - 2, '09:30', 2, 3),

       (CURRENT_DATE - 1, '09:00', 1, 1),
       (CURRENT_DATE - 1, '10:59', 2, 3),

       (CURRENT_DATE, '10:00', 1, 2),
       (CURRENT_DATE, '09:00', 2, 1);