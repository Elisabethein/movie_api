package com.api.movie_api.Controllers;

import com.api.movie_api.Entities.Movie;
import com.api.movie_api.Entities.Session;
import com.api.movie_api.Services.HistoryService;
import com.api.movie_api.Services.MovieService;
import com.api.movie_api.Services.SeatService;
import com.api.movie_api.Services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {
    @Autowired
    private MovieService movieService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private SeatService seatService;
    @Autowired
    private HistoryService historyService;

    @GetMapping("/allMovies")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/allSessions")
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/movie/{id}")
    public Movie getMovieById(
            @PathVariable("id") Long id
    ) {
        return movieService.getMovieById(id);
    }

    @GetMapping("/session/{id}")
    public Session getSessionById(
            @PathVariable("id") Long id
    ) {
        return sessionService.getSessionById(id);
    }

    @GetMapping("/seatPlan/{sessionId}")
    public char[][] getSeatPlan(
            @PathVariable("sessionId") Long sessionId
    ) {
        return seatService.getSeatPlan(sessionId);
    }

    @GetMapping("/suggestedSeat/{sessionId}/{tickets}")
    public String getSuggestedSeat(
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("tickets") int tickets
    ) {
        return seatService.getSuggestedSeat(sessionId, tickets);
    }

    @PostMapping("/bookSeat/{sessionId}/{rowNumber}/{seatNumber}")
    public String bookSeat(
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("rowNumber") int rowNumber,
            @PathVariable("seatNumber") int seatNumber
    ) {
        return seatService.bookSeat(sessionId, rowNumber, seatNumber);
    }

    @PostMapping("/addHistory/{sessionId}/{clientId}")
    public String addHistory(
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("clientId") Long clientId
    ) {
        return historyService.addHistory(sessionId, clientId);
    }

}
