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

    public String addHistory(Long sessionId, Long clientId) {
        List<History> historyList = historyRepository.findByClientIdAndSessionId(clientId, sessionId);
        if (!historyList.isEmpty()) {
            return "History already exists";
        }
        History history = new History(clientId, sessionId);
        historyRepository.save(history);
        return "History added";
    }
}
