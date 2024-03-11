package com.api.movie_api.Controllers;

import com.api.movie_api.Entities.Movie;
import com.api.movie_api.Services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/allMovies")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }
    @GetMapping("/movie/{id}")
    public Movie getMovieById(
            @PathVariable("id") Long id
    ) {
        return movieService.getMovieById(id);
    }
    @GetMapping("/allGenres")
    public List<String> getAllGenres() {
        return movieService.getAllGenres();
    }
    @GetMapping("/allLanguages")
    public List<String> getAllLanguages() {
        return movieService.getAllLanguages();
    }
    @GetMapping("/allAgeRestrictions")
    public List<String> getAllAgeRestrictions() {
        return movieService.getAllAgeRestrictions();
    }
}
