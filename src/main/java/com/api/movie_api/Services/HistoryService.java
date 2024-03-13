package com.api.movie_api.Services;

import com.api.movie_api.Entities.History;
import com.api.movie_api.Repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    /**
     * Add a session history for a client
     * @param sessionId - id of the session
     * @param clientId - id of the client
     * @return a string message
     */
    public String addHistory(Long sessionId, Long clientId) {
        List<History> historyList = historyRepository.findByClientIdAndSessionId(clientId, sessionId);
        // Check if client has already booked the session
        if (!historyList.isEmpty()) {
            return "History already exists";
        }
        History history = new History(clientId, sessionId);
        historyRepository.save(history);
        return "History added";
    }
}
