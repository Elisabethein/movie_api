package com.api.movie_api.Controllers;

import com.api.movie_api.Services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class HistoryController {
    @Autowired
    private HistoryService historyService;
    @PostMapping("/addHistory/{sessionId}/{clientId}")
    public String addHistory(
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("clientId") Long clientId
    ) {
        return historyService.addHistory(sessionId, clientId);
    }
}
