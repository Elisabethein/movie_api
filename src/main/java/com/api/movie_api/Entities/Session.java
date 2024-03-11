package com.api.movie_api.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long movieId;
    private Long roomId;
    private LocalDate date;
    private LocalTime time;

    public Session(Long movie_id, Long room_id, LocalDate date, LocalTime time) {
        this.movieId = movie_id;
        this.roomId = room_id;
        this.date = date;
        this.time = time;
    }
}
