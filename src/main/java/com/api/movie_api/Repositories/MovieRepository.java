package com.api.movie_api.Repositories;

import com.api.movie_api.Entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT DISTINCT m.genre FROM Movie m")
    List<String> findAllGenres();

    @Query("SELECT DISTINCT m.language FROM Movie m")
    List<String> findAllLanguages();

    @Query("SELECT DISTINCT m.ageRestriction FROM Movie m")
    List<String> findAllAgeRestrictions();
}
