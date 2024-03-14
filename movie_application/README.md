# movie_application

This is a movie application, which is a simple web interface for the API. The web interface is written in Vue.js.
This project is the front-end part of the internship trial work for CGI Estonia.
The back-end part of the project can be found in the following repository: [Delivery API](https://github.com/Elisabethein/movie_api).

The web interface consists of the following views:
* Home / Sessions Page - the main page of the application. The user can see the list of all sessions and filter them by starting time, age restrictions, genre and language. By default the user can see all sessions. Every filter shows only the sessions that match the filter, except for the starting time filter, which shows the sessions that start at the chosen time or later. The user doesn't have to be logged in to use this pages functionalities, they are greeted as Guest. But the "suggest by history" button is only functional for logged in users. The user can click on the session and be directed to the session page.
* A Session Page - shows the session's details. The user can see the session's title, starting time, age restriction, genre, language. The page displays the session's seating plan, where vacant seats are colored white and taken seats are colored dark. The user can select how many tickets they would like to buy and the program suggest the best seats for them, coloring them green. The user can click on seats to select them and ten make a purchase. Purchase is only possible for logged-in users.
* Login Page - the user can log in to the application. The user can log in with their username and password.
* Sign Up Page - the user can sign up to the application. The user can sign up with their username and password.


Credits to photo:
Photo by <a href="https://unsplash.com/@timesnewroman14?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Roman Skrypnyk</a> on <a href="https://unsplash.com/photos/a-black-and-white-movie-clapper-on-a-gray-background-gjA24divsqw?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Unsplash</a>
  
## Project details

The program runs on port 8080 by default, which has been granted access by the back-end part of the project. The user might need to change the port in the back-end part of the project, if they want to run the program on a different port.

## Project setup
```
npm install
```
## install dependencies:
```
npm install vuex

npm install vue-router
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
