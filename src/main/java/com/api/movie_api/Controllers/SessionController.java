package com.api.movie_api.Controllers;

import com.api.movie_api.Entities.Movie;
import com.api.movie_api.Entities.Session;
import com.api.movie_api.Services.HistoryService;
import com.api.movie_api.Services.MovieService;
import com.api.movie_api.Services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/sessionByGenre/{genre}")
    public List<Session> getSessionByGenre(
            @PathVariable("genre") String genre
    ) {
        return sessionService.getSessionByGenre(genre);
    }
    @GetMapping("/sessionByLanguage/{language}")
    public List<Session> getSessionByLanguage(
            @PathVariable("language") String language
    ) {
        return sessionService.getSessionByLanguage(language);
    }
    @GetMapping("/sessionByAgeRestriction/{ageRestriction}")
    public List<Session> getSessionByAgeRestriction(
            @PathVariable("ageRestriction") String ageRestriction
    ) {
        return sessionService.getSessionByAgeRestriction(ageRestriction);
    }
    @GetMapping("/sessionByTime/{time}")
    public List<Session> getSessionByTime(
            @PathVariable("time") String time
    ) {
        return sessionService.getSessionByTime(time);
    }
    @GetMapping("/sessionByHistory/{clientId}")
    public List<Session> getSessionByHistory(
            @PathVariable("clientId") Long clientId
    ) {
        return sessionService.getSessionByHistory(clientId);
    }

}
