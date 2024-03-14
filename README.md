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
There is also a web interface for the API.
The web interface is written in Vue.js and can be found in the very same project - in folder movie_application

This project uses PostgreSQL as a database. On the first run, the database is initialized with the following data:
* 12 movies, which use movie database api to get its genre, language, age restriction and rating.
  The api is from https://www.omdbapi.com/
* 3 rooms
* 1 client (username: test, password: Proov_123) 
* 1 session for every following day for the next 7 days. The movie and room are chosen randomly.
* seating plan for every added session. The seat status is chosen randomly with 30% probability for a seat to be taken. It is assumed that there should be more vacant seats than taken seats.

## Running the project

To run the project manually on your machine, you need to have Java 21 and Gradle installed on your machine.
The project can be run with and without Docker.

With Docker:
* Run the compose.yml file which will start the project in a docker container, with database, back-end server and front-end server. You will need Docker Desktop installed on your machine.

Without Docker:
* Running the back-end server and front-end server separately.
   This requires the following steps:
   * Create a database in PostgreSQL with the name moviedatabase
   * Change the username and password in the application.properties file to match your database credentials
   * Switch the database url line in the application.properties file to the commented line and comment the current line
   * Run the back-end server with starting the MovieApiApplication class
   * Run the front-end server in the movie_application folder with the following commands:
     * npm install
     * npm run serve


* The back-end will start on port 8080
* The front-end will start on port 8081
* The web interface can be accessed at http://localhost:8081
* The API can be accessed at http://localhost:8080/api

## Description of the web interface

The front-end part does have its own README.md file, which can be found in the movie_application folder. But here is a short description of the web interface:
The web interface consists of the following views:
* Home / Sessions Page - the main page of the application. The user can see the list of all sessions and filter them by starting time, age restrictions, genre and language. By default the user can see all sessions. Every filter shows only the sessions that match the filter, except for the starting time filter, which shows the sessions that start at the chosen time or later. The user doesn't have to be logged in to use this pages functionalities, they are greeted as Guest. But the "suggest by history" button is only functional for logged in users. The user can click on the session and be directed to the session page.
* A Session Page - shows the session's details. The user can see the session's title, starting time, age restriction, genre, language and IMDB rating. The page displays the session's seating plan, where vacant seats are colored white and taken seats are colored dark. The user can select how many tickets they would like to buy and the program suggest the best seats for them, coloring them green. The user can click on seats to select them and ten make a purchase. Purchase is only possible for logged-in users.
* Login Page - the user can log in to the application. The user can log in with their username and password.
* Sign Up Page - the user can sign up to the application. The user can sign up with their username and password.

## Important logic

The project uses some more in-depth logic, which is worth mentioning.
* The project uses a third-party API to get the movie's genre, language, age restriction and rating. The API is from https://www.omdbapi.com/
* It is possible to add movies of your liking to the database, but this must be done by adding the titles to the titles list ind DataInitializer class before first run, because the movies in the db stay constant.
* On every run the program checks how many upcoming sessions there are. If there are less than 20, we add on for every following day, but else the sessions remain the same to prevent overloading the user's homepage.
* The seating plan is created with a 30% probability for a seat to be taken. It is assumed that there should be more vacant seats than taken seats.
* The seating plan is first created as a 2D char array, where 'X' represents a taken seat and 'O' represents a vacant seat. The front end uses this array to display the seating plan and colors the seats accordingly.
* The user can select up to 9 tickets at once. The program uses a 2D integer array to calculate the best seat combination. I have assumed the best seats are in the middle rows and in the middle seats. The program calculates the best possible score and returns the first combination with that score. The front-end uses this combination to color the seats green.
* The user can select seats they want to buy. The program colors them pink and remembers all for the user to be able to purchase. The user can deselect the seats if they want to. The purchase button is functional only for logged-in users and it is possible to buy as many seats as possible and in any places. The program adds the session to user's history after purchasing tickets.
* The user can filter sessions by starting time, age restrictions, genre and language. The program returns only the sessions that match the filter. The user can also see all starting times, which are filterable and return the sessions that start at the chosen time or later.
* Logged in users can also order the sessions based on their history. Without history the order remains the same. The algorithm used in this part first calculates the counts of every watched genre and sorts the sessions by genres. Then it calculates the most visited time (median if there are more than one) and sorts the sessions by starting time relative to the most visited time. Then it sorts by the rating if many sessions have same starting time.
* By default the sessions are sorted by time and then by rating.
* I assumed the web interface should be accessible for everyone, so the user doesn't have to be logged in to use the functionalities. The user can see all sessions and filter them by starting time, age restrictions, genre and language. The user can also see the session's details and seating plan. The user can also see the starting times and filter the sessions by starting time. The user can also see the movies and their details.


## Personal Notes

This project took me about 5 days to complete. I am familiar with Spring boot, so I didn't face many difficulties with this project. 
The hardest part was to set up Docker for every part of the project. I hadn't used Docker for my projects before, so it took me a while to figure out how to set it up. But i got help from many different web pages.
I can proudly say, that I implemented as much as I could, and I'm happy with how it turned out. I hope running this project on your machine will be as easy as possible.

