package com.api.movie_api.Controllers;

import com.api.movie_api.Services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    /**
     * Add a session history for a client
     * @param sessionId - id of the session
     * @param clientId - id of the client
     * @return a string message
     */
    @PostMapping("/addHistory/{sessionId}/{clientId}")
    public ResponseEntity<String> addHistory(
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("clientId") Long clientId
    ) {
        try {
            return ResponseEntity.ok(historyService.addHistory(sessionId, clientId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
}
