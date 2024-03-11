package com.api.movie_api.Repositories;


import com.api.movie_api.Entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findAllByClientId(Long clientId);
}
