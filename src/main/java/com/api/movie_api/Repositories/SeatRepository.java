package com.api.movie_api.Repositories;


import com.api.movie_api.Entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Seat findByRowAndSeatAndSessionId(int row, int seat, Long session_id);

    List<Seat> findBySessionId(Long sessionId);
}
