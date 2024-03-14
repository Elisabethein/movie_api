package com.api.movie_api.Controllers;

import com.api.movie_api.Entities.Movie;
import com.api.movie_api.Entities.Session;
import com.api.movie_api.Services.HistoryService;
import com.api.movie_api.Services.MovieService;
import com.api.movie_api.Services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @GetMapping("/allSessions")
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/session/{id}")
    public Session getSessionById(
            @PathVariable("id") Long id
    ) {
        return sessionService.getSessionById(id);
    }

    /**
     * Get all sessions with a specific genre
     * @param genre - genre of the movie
     * @return a list of sessions
     */
    @GetMapping("/sessionByGenre/{genre}")
    public List<Session> getSessionByGenre(
            @PathVariable("genre") String genre
    ) {
        return sessionService.getSessionByGenre(genre);
    }

    /**
     * Get all sessions with a specific language
     * @param language - language of the movie
     * @return a list of sessions
     */
    @GetMapping("/sessionByLanguage/{language}")
    public List<Session> getSessionByLanguage(
            @PathVariable("language") String language
    ) {
        return sessionService.getSessionByLanguage(language);
    }

    /**
     * Get all sessions with a specific age restriction
     * @param ageRestriction - age restriction of the movie
     * @return a list of sessions
     */
    @GetMapping("/sessionByAgeRestriction/{ageRestriction}")
    public List<Session> getSessionByAgeRestriction(
            @PathVariable("ageRestriction") String ageRestriction
    ) {
        return sessionService.getSessionByAgeRestriction(ageRestriction);
    }

    /**
     * Get all sessions that have starting time equal or later than the given time
     * @param time - time
     * @return a list of sessions
     */
    @GetMapping("/sessionByTime/{time}")
    public List<Session> getSessionByTime(
            @PathVariable("time") String time
    ) {
        return sessionService.getSessionByTime(time);
    }

    /**
     * Get all the sessions sorted by the user's history preferences
     * @param clientId - id of the client
     * @return a list of all playing sessions sorted by the user's history preferences
     */
    @GetMapping("/sessionByHistory/{clientId}")
    public List<Session> getSessionByHistory(
            @PathVariable("clientId") Long clientId
    ) {
        return sessionService.getSessionByHistory(clientId);
    }

    /**
     * Get all the start times of the sessions to be used in the filter
     */
    @GetMapping("/allStartTimes")
    public List<LocalTime> getAllStartTimes() {
        return sessionService.getAllStartTimes();
    }

    /**
     * Get all the sessions that match the filter
     * @param genre - genre of the movie (optional)
     * @param language - language of the movie (optional)
     * @param ageRestriction - age restriction of the movie (optional)
     * @param time - time (optional)
     * @return a list of sessions
     */
    @GetMapping("/filteredSessions")
    public List<Session> getFilteredSessions(@RequestParam(required = false) String genre,
                                             @RequestParam(required = false) String language,
                                             @RequestParam(required = false) String ageRestriction,
                                             @RequestParam(required = false) String time) {
        // Frontend sends empty strings instead of nulls
        if (Objects.equals(genre, "")) {
            genre = null;
        }
        if (Objects.equals(language, "")) {
            language = null;
        }
        if (Objects.equals(ageRestriction, "")) {
            ageRestriction = null;
        }
        if (Objects.equals(time, "")) {
            time = null;
        }
        return sessionService.getFilteredSessions(genre, language, ageRestriction, time);
    }

}
