[Restaurant Voting Application](https://github.com/vlubchen/gradproject)
===============================
-------------------------------------------------------------
Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
  - If it is before 11:00 we assume that he changed his mind.
  - If it is after 11:00 then it is too late, vote can't be changed
* Each restaurant provides a new menu each day.
-------------------------------------------------------------
* Additionally: Admin may not fill in the price of the lunch, after saving the price of the lunch item will be equal to the price of the dish.
-------------------------------------------------------------
Description API:

There are two roles: Admin and User, which configured for specific paths:
* /api/profile/** - User's profile
* /api/** - for Users (except path: /api/admin/**)
* /api/admin/** - for Admins

Paths:
* /api/profile - get / delete / register / update User's profile
* /api/restaurants/ - get all restaurants with lunch items on today (for User)
* /api/restaurants/{id} - get restaurant with lunch items on today (for User)
* /api/restaurants/{restaurantId}/lunch-items - get lunch items on today for restaurant (for User)
* /api/restaurants/{restaurantId}/lunch-items/by-date - get lunch items by date for restaurant (for User)
* /api/votes - get all votes for logged user (for User)
* /api/votes/today - get vote for logged user on today / create vote /update vote(for User)
* /api/votes/by-date - get vote for logged user by date (for User)
* /api/admin/users - get all / get by e-mail / create / update / delete / disable / enable Users (for Admin)
* /api/admin/restaurants - get all / get by id / create / update / delete restaurants (for Admin)
* /api/admin/restaurants/{restaurantId}/lunch-items - get all / create lunch items for restaurant (for Admin)
* /api/admin/restaurants/{restaurantId}/lunch-items/{id} - get by id / update / delete lunch items for restaurant (for Admin)
* /api/admin/restaurants/{restaurantId}/dishes - get all / create dishes for restaurant (for Admin)
* /api/admin/restaurants/{restaurantId}/dishes/{id} - get by id / update / delete dishes for restaurant (for Admin)
-------------------------------------------------------------
- Stack: [JDK 17](http://jdk.java.net/17/), Spring Boot 3.x, Lombok, H2, Caffeine Cache, SpringDoc OpenApi 2.x, Mapstruct, Liquibase 
- Run: `mvn spring-boot:run` in root directory.
-----------------------------------------------------
[REST API documentation](http://localhost:8080/)  
Credentials:
```
Admin: admin@gmail.com / admin
User:  user@yandex.ru / password
Guest: guest@gmail.com / guest
```