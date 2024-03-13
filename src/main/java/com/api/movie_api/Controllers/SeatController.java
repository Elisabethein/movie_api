package com.api.movie_api.Controllers;

import com.api.movie_api.Entities.Seat;
import com.api.movie_api.Services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class SeatController {
    @Autowired
    private SeatService seatService;

    /**
     * Get the seat plan for a session
     * @param sessionId - id of the session
     * @return a 2D array of characters representing the seat plan, where 'O' is available and 'X' is booked
     */
    @GetMapping("/seatPlan/{sessionId}")
    public char[][] getSeatPlan(
            @PathVariable("sessionId") Long sessionId
    ) {
        return seatService.getSeatPlan(sessionId);
    }

    /**
     * Get the suggested seats list for a client based on the number of tickets
     * @param sessionId - id of the session
     * @param tickets - number of tickets
     * @return a list of suggested seats
     */
    @GetMapping("/suggestedSeat/{sessionId}/{tickets}")
    public ResponseEntity<List<Seat>> getSuggestedSeat(
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("tickets") int tickets
    ) {
        return ResponseEntity.ok(seatService.getSuggestedSeat(sessionId, tickets));
    }

    /**
     * Book a seat for a session with a specific row and seat number
     * @param sessionId - id of the session
     * @param rowNumber - row number
     * @param seatNumber - seat number
     * @return a string message
     */
    @PostMapping("/bookSeat/{sessionId}/{rowNumber}/{seatNumber}")
    public ResponseEntity<String> bookSeat(
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("rowNumber") int rowNumber,
            @PathVariable("seatNumber") int seatNumber
    ) {
        try {
            return ResponseEntity.ok(seatService.bookSeat(sessionId, rowNumber, seatNumber));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Get a seat object by session id, row number and seat number
     * @param sessionId - id of the session
     * @param rowNumber - row number
     * @param seatNumber - seat number
     * @return the seat object
     */
    @GetMapping("/seat/{sessionId}/{rowNumber}/{seatNumber}")
    public Seat getSeat(
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("rowNumber") int rowNumber,
            @PathVariable("seatNumber") int seatNumber
    ) {
        return seatService.getSeat(sessionId, rowNumber, seatNumber);
    }
}
