package com.api.movie_api.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String genre;
    private String ageRestriction;
    private String language;
    private double rating;

    public Movie(String title, String genre, String ageRestriction, String language, double rating) {
        this.title = title;
        this.genre = genre;
        this.ageRestriction = ageRestriction;
        this.language = language;
        this.rating = rating;
    }
}
