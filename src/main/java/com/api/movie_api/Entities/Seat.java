package com.api.movie_api.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int row;
    private int seat;
    private Long sessionId;
    private boolean isReserved;

    public Seat(Integer row, Integer seat, Long sessionId, boolean isReserved) {
        this.row = row;
        this.seat = seat;
        this.sessionId = sessionId;
        this.isReserved = isReserved;
    }
}
