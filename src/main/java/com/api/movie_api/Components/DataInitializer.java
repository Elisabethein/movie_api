package com.api.movie_api.Components;

import com.api.movie_api.Entities.Movie;
import com.api.movie_api.Entities.Room;
import com.api.movie_api.Entities.Seat;
import com.api.movie_api.Entities.Session;
import com.api.movie_api.Repositories.MovieRepository;
import com.api.movie_api.Repositories.RoomRepository;
import com.api.movie_api.Repositories.SeatRepository;
import com.api.movie_api.Repositories.SessionRepository;
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

    @Autowired
    public DataInitializer(MovieRepository movieRepository, RoomRepository roomRepository, SessionRepository sessionRepository, SeatRepository seatRepository) {
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.sessionRepository = sessionRepository;
        this.seatRepository = seatRepository;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception{
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
//        movieRepository.saveAll(movieList);
        List<Room> roomList = List.of(
                new Room(),
                new Room(),
                new Room()
        );
//        roomRepository.saveAll(roomList);


//        movieList = movieRepository.findAll();
//        roomList = roomRepository.findAll();
//        List<Session> sessions = new ArrayList<>();
//        LocalDate currentDate = LocalDate.now();
//        Random random = new Random();
//        for (int i = 0; i < 7; i++) {
//            LocalDateTime sessionTimestamp = LocalDateTime.of(currentDate.plusDays(i), LocalTime.of(random.nextInt(10) + 14, 0));
//            Session session = new Session(movieList.get(random.nextInt(movieList.size())).getId(), roomList.get(random.nextInt(roomList.size())).getId(), sessionTimestamp.toLocalDate(), sessionTimestamp.toLocalTime());
//            sessions.add(session);
//            sessionRepository.save(session);
//        }
//
//        for (Session session : sessions) {
//            for (int i = 1; i < 10; i++) {
//                for (int j = 1; j < 10; j++) {
//                    // There is a 30% chance that the seat is taken, assuming usually more seats are vacant than taken
//                    boolean isSeatTaken = Math.random() < 0.3;
//                    seatRepository.save(new Seat(i, j, session.getId(), isSeatTaken));
//                }
//            }
//        }

    }
}
