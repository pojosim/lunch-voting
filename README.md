
Voting system for deciding where to have lunch.
==

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
  - If it is before 11:00 we assume that he changed his mind.
  - If it is after 11:00 then it is too late, vote can't be changed
    
Each restaurant provides a new menu each day.

## Technologies

* Java 15
* Spring Boot 2
* Spring Security
* Spring Data REST
* Spring Data JPA, Hibernate
* JUnit 5
* Tomcat
* Maven
* HSQLDB

## Requirements

Set the JAVA_HOME environment variable to the JDK version 15 root folder

## Run application

- #### Test application
  `mvn test`
  
- #### Run application (default port 8080)
  `mvn spring-boot:run`

# API
## Credentional

Authorization is done using username

* User1, user1@google.com, 12345678, {USER}
* User2, user2@google.com, 87654321, {USER}
* User3, user3@google.com, 27654322, {USER}
* User4, user4@google.com, 2777894322, {USER}
* Admin, admin@google.com, admin2899999, {ADMIN, USER}

## CURL Command
### ROLE Admin

- ####  Get all restaurants with all menus and with dishes
  `curl -X GET http://localhost:8080/api/admin/restaurant -H 'Authorization: Basic QWRtaW46YWRtaW4='`
  
- ####  Create new restaurant
  `curl -X POST http://localhost:8080/api/admin/restaurant -H 'Authorization: Basic QWRtaW46YWRtaW4=' -H 'Content-Type: application/json' -d '{"name": "Mamasita"}'`

- ####  Delete restaurant by ID
  `curl -X DELETE http://localhost:8080/api/admin/restaurant/1000 -H 'Authorization: Basic QWRtaW46YWRtaW4='`

- ####  Get all menus by restaurant ID
  `curl -X GET http://localhost:8080/api/admin/restaurant/1000/menu -H 'Authorization: Basic QWRtaW46YWRtaW4='`

- ####  Create menu by date with dishes 
  `curl -X POST http://localhost:8080/api/admin/restaurant/1000/menu?date=2021-01-10 -H 'Authorization: Basic QWRtaW46YWRtaW4=' -H 'Content-Type: application/json' -d '[{"name": "Chiсken soup","price": 80 }, {"name": "Bun with poppy seeds","price": 45 }]'`

- ####  Update all dishes menu by date and restaurant ID 
  `curl -X PUT http://localhost:8080/api/admin/restaurant/1000/menu?date=2021-01-10 -H 'Authorization: Basic QWRtaW46YWRtaW4=' -H 'Content-Type: application/json' -d '[{"name": "Apple cream soup","price": 125 }, {"name": "Mashed potatoes","price": 55 }]'`

- ####  Delete menu with dishes by date and restaurant ID
  `curl -X DELETE http://localhost:8080/api/admin/restaurant/1000/menu?date=2021-01-10 -H 'Authorization: Basic QWRtaW46YWRtaW4='`


### ROLE User

- ####  Get today restaurants with menu
  `curl -X GET http://localhost:8080/api/restaurant -H 'Authorization: Basic VXNlcjE6dXNlcjE='`

- ####  Vote to the restaurant by ID
  `curl -X POST http://localhost:8080/api/restaurant/1000/vote -H 'Authorization: Basic VXNlcjE6dXNlcjE='`

- ####  Vote result
  `curl -X GET http://localhost:8080/api/restaurant/vote-result -H 'Authorization: Basic VXNlcjE6dXNlcjE='`