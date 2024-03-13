package com.api.movie_api.Components;

import com.api.movie_api.Entities.*;
import com.api.movie_api.Repositories.*;
import com.api.movie_api.Services.ClientService;
import com.api.movie_api.Services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements ApplicationRunner {
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final SessionRepository sessionRepository;
    private final SeatRepository seatRepository;
    private final SessionService sessionService;
    private final ClientRepository clientRepository;
    private final ClientService clientService;

    @Autowired
    public DataInitializer(MovieRepository movieRepository, RoomRepository roomRepository, SessionRepository sessionRepository, SeatRepository seatRepository, SessionService sessionService, ClientRepository clientRepository, ClientService clientService) {
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.sessionRepository = sessionRepository;
        this.seatRepository = seatRepository;
        this.sessionService = sessionService;
        this.clientRepository = clientRepository;
        this.clientService = clientService;
    }

    /**
     * This method fills the database with initial data
     * @param args incoming application arguments
     * @throws Exception if there is an error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // If there are no movies in the database, add some
        if (movieRepository.findAll().isEmpty()) {
            List<Movie> movieList = List.of(
                    new Movie("The Shawshank Redemption", "Drama", "", "English"),
                    new Movie("The Godfather", "Crime", "", "English"),
                    new Movie("The Dark Knight", "Action", "", "English"),
                    new Movie("The Lord of the Rings: The Return of the King", "Adventure", "", "English"),
                    new Movie("Pulp Fiction", "Crime", "", "English"),
                    new Movie("Forrest Gump", "Drama", "", "English"),
                    new Movie("Inception", "Action", "", "English"),
                    new Movie("The Matrix", "Action", "", "English"),
                    new Movie("The Lord of the Rings: The Fellowship of the Ring", "Adventure", "", "English"),
                    new Movie("The Lord of the Rings: The Two Towers", "Adventure", "", "English"),
                    new Movie("The Godfather: Part II", "Crime", "", "English")
            );
            movieRepository.saveAll(movieList);
        }
        // If there are no rooms in the database, add some
        if (roomRepository.findAll().isEmpty()) {
            List<Room> roomList = List.of(
                    new Room(),
                    new Room(),
                    new Room()
            );
            roomRepository.saveAll(roomList);
        }
        // If there are no users in the database, add one
        if (clientRepository.findAll().isEmpty()){
            clientService.login("test", "Proov_123");
        }

        // If there are too many sessions for the following week, don't add more, else add one session for each day with random movie and room
        if (sessionService.getAllSessions().size() > 20) {
            return;
        } else {
            List<Movie> movieList = movieRepository.findAll();
            List<Room> roomList = roomRepository.findAll();
            List<Session> sessions = new ArrayList<>();
            LocalDate currentDate = LocalDate.now();
            Random random = new Random();
            for (int i = 0; i < 7; i++) {
                LocalDateTime sessionTimestamp = LocalDateTime.of(currentDate.plusDays(i), LocalTime.of(random.nextInt(10) + 14, 0));
                Session session = new Session(movieList.get(random.nextInt(movieList.size())).getId(), roomList.get(random.nextInt(roomList.size())).getId(), sessionTimestamp.toLocalDate(), sessionTimestamp.toLocalTime());
                sessions.add(session);
                sessionRepository.save(session);
            }

            // Add 9x9 seats for each added session, with 30% chance that the seat is taken
            for (Session session : sessions) {
                for (int i = 1; i < 10; i++) {
                    for (int j = 1; j < 10; j++) {
                        // There is a 30% chance that the seat is taken, assuming usually more seats are vacant than taken
                        boolean isSeatTaken = Math.random() < 0.3;
                        seatRepository.save(new Seat(i, j, session.getId(), isSeatTaken));
                    }
                }
            }
        }
    }
}
