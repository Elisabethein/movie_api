# Movie Application

This is a Spring Boot project written in Java. The project uses Gradle as a build tool. The project is for internship trial work at CGI Estonia.

## Project Description

The goal of this project is to create a RESTful API.

The project consists of the following packages:
* controllers - contains the RESTful API endpoints
* repositories - contains the JPA repositories
* services - contains the business logic
* entities - contains the JPA entities
* configurations - contains the configuration classes
* components - contains the data initialization classes

The API takes requests from path /api and consists of the following endpoints:
1) Session Controller:
   * /allSessions - get all sessions
   * /session/{id} - get session by id
   * /sessionByGenre/{genre} - get sessions by genre
      * parameters: genre
      * returns: sessions with the chosen genre
    * /sessionByLanguage/{language} - get sessions by language
      * parameters: language
      * returns: sessions with the chosen language
    * /sessionByAgeRestriction/{ageRestriction} - get sessions by age restriction
        * parameters: ageRestriction
        * returns: sessions with the chosen age restriction
    * /sessionByTime/{time} - get sessions by starting time
        * parameters: time
        * returns: sessions with the chosen starting time or later
    * /sessionByHistory/{clientId} - get sessions by user's history
         * parameters: clientId
         * returns: sessions based on user's history
    * /allStartTimes - get all starting times
    * /filteredSessions - get filtered sessions
        * parameters: genre, language, ageRestriction, time
        * returns: sessions that match the filter
2) Movie Controller:
    * /allMovies - get all movies
    * /movie/{id} - get movie by id
    * /allGenres - get all genres
    * /allLanguages - get all languages
    * /allAgeRestrictions - get all age restrictions
3) Client Controller:
    * /login - log in to the application
    * /signUp - sign up to the application
    * /client/{id} - get client by id
4) History Controller:
    * /addHistory/{sessionId}/{clientId} - add session to user's history
        * parameters: sessionId, clientId
5) Seat Controller:
    * /suggestedSeats/{sessionId}/{tickets} - get suggested seats based on the amount of tickets
        * parameters: sessionId, tickets
        * returns: suggested seats as list
    * /bookSeat/{sessionId}/{rowNumber}/{seatNumber} - reserve a seat
        * parameters: sessionId, rowNumber, seatNumber
    * /seat/{sessionId}/{rowNumber}/{seatNumber} - get seat by session id, row number and seat number
        * parameters: sessionId, rowNumber, seatNumber
        * returns: seat

The project also has a test class, which tests the business logic and error handling.
There is also a web interface for the API. The web interface is written in Vue.js and can be found in the following repository: [Movie Application Web Interface](https://github.com/Elisabethein/movie_application)

This project uses PostgreSQL as a database. On the first run, the database is initialized with the following data:
* movies
* 3 rooms
* 1 client
* 1 session for every following day for the next 7 days. The movie and room are chosen randomly.
* seating plan for every added session. The seat status is chosen randomly with 30% probability for a seat to be taken. It is assumed that there should be more vacant seats than taken seats.

## Running the project

To run the project manually on your machine, you need to have Java 21 and Gradle installed on your machine.
Application can be started by running the MovieApplication class or by running the following command in the terminal:
```./gradlew bootRun```

* The application will start on port 8081, which can be changed in the application.properties file.


## Personal Notes

This project took me about 3 days to complete. I am familiar with Spring boot, so I didn't face many difficulties with this project. The hardest part was to come up with the logic for the suggested seats and session order for clients.
Another hard part was handling the denied interaction between server and front-end, but that problem was solved with configurations.

