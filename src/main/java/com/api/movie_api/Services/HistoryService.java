package com.api.movie_api.Services;

import com.api.movie_api.Entities.History;
import com.api.movie_api.Repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;
    @Autowired
    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public String addHistory(Long sessionId, Long clientId) {
        History history = new History(sessionId, clientId);
        historyRepository.save(history);
        return "History added";
    }
}
