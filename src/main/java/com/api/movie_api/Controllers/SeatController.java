package com.api.movie_api.Controllers;

import com.api.movie_api.Services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class SeatController {
    @Autowired
    private SeatService seatService;
    @GetMapping("/seatPlan/{sessionId}")
    public char[][] getSeatPlan(
            @PathVariable("sessionId") Long sessionId
    ) {
        return seatService.getSeatPlan(sessionId);
    }

    @GetMapping("/suggestedSeat/{sessionId}/{tickets}")
    public String getSuggestedSeat(
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("tickets") int tickets
    ) {
        return seatService.getSuggestedSeat(sessionId, tickets);
    }

    @PostMapping("/bookSeat/{sessionId}/{rowNumber}/{seatNumber}")
    public String bookSeat(
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("rowNumber") int rowNumber,
            @PathVariable("seatNumber") int seatNumber
    ) {
        return seatService.bookSeat(sessionId, rowNumber, seatNumber);
    }
}
