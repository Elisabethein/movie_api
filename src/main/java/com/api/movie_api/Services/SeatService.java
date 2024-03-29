package com.api.movie_api.Services;

import com.api.movie_api.Entities.Seat;
import com.api.movie_api.Repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /**
     * Book a seat for a session with a specific row and seat number
     */
    public String bookSeat(Long sessionId, int rowNumber, int seatNumber) {
        // Check if seat exists and is not already reserved
        Seat seat = seatRepository.findByRowAndSeatAndSessionId(rowNumber, seatNumber, sessionId);
        if (seat != null) {
            if (seat.isReserved()) {
                throw new IllegalStateException("Seat is already reserved");
            }
            seat.setReserved(true);
            seatRepository.save(seat);
            return "Seat reserved";
        }
        throw new IllegalStateException("Seat does not exist");
    }

    /**
     * Cretaes a 2D array of characters representing the seat plan, where 'O' is available and 'X' is booked
     */
    public char[][] getSeatPlan(Long sessionId) {
        // Get the seat list for the session and sort it by row and seat number
        List<Seat> seatList = seatRepository.findBySessionId(sessionId);
        seatList = seatList.stream()
                .sorted(Comparator.comparingInt(Seat::getRow)
                        .thenComparingInt(Seat::getSeat))
                .toList();
        if (seatList.isEmpty()) {
            throw new IllegalStateException("Invalid session ID");
        }
        // Create a 2D array of characters representing the seat plan
        char[][] seatPlan = new char[9][9];
        for (Seat seat : seatList) {
            seatPlan[seat.getRow() - 1][seat.getSeat() - 1] = seat.isReserved() ? 'X' : 'O';
        }
        return seatPlan;
    }

    /**
     * Creates a list of suggested seats for a client based on the number of tickets
     * Uses a scoring system to suggest the best seats, where the middle seats and middle rows have higher scores than others
     * The algorithm starts from the middle row and moves to the top and bottom rows in a zigzag pattern
     * It then calculates the score for each combination of seats and only checks the availability if the score is higher than the current max score
     * It returns the list of suggested seats with the highest score if any
     */
    public List<Seat> getSuggestedSeat(Long sessionId, int tickets) {
        char[][] seatPlan = getSeatPlan(sessionId);
        int[][] seatScore = {
                {1, 2, 3, 4, 5, 4, 3, 2, 1},
                {2, 3, 4, 5, 6, 5, 4, 3, 2},
                {3, 4, 5, 6, 7, 6, 5, 4, 3},
                {4, 5, 6, 7, 8, 7, 6, 5, 4},
                {5, 6, 7, 8, 9, 8, 7, 6, 5},
                {4, 5, 6, 7, 8, 7, 6, 5, 4},
                {3, 4, 5, 6, 7, 6, 5, 4, 3},
                {2, 3, 4, 5, 6, 5, 4, 3, 2},
                {1, 2, 3, 4, 5, 4, 3, 2, 1}
        };
        int maxScore = 0;
        List<Seat> suggestedSeats = new ArrayList<>();

        // Starting from the middle row and moving to the top and bottom rows in a zigzag pattern
        int i = seatScore.length / 2;
        for (int j = 0; j <= seatScore.length / 2; j++) {
            int rowIndex1 = i + j;
            int rowIndex2 = i - j;
            // Calculate the score of bottom row
            for (int k = 0; k <= seatScore[0].length - tickets; k++) {
                int currentScore = 0;
                for (int l = 0; l < tickets; l++) {
                    if (seatPlan[rowIndex1][k + l] == 'X') {
                        k = k + l;
                        currentScore = 0;
                        break;
                    }
                    currentScore += seatScore[rowIndex1][k + l];
                }
                if (currentScore > maxScore) {
                    maxScore = currentScore;
                    suggestedSeats.clear();
                    for (int l = 0; l < tickets; l++) {
                        suggestedSeats.add(seatRepository.findByRowAndSeatAndSessionId(rowIndex1 + 1, k + l + 1, sessionId));
                    }
                }
            }
            // If the top row is the same as the bottom row, skip the iteration
            if (rowIndex1 == rowIndex2) {
                continue;
            }
            // Calculate the score of top row
            for (int k = 0; k <= seatScore[0].length - tickets; k++) {
                int currentScore = 0;
                for (int l = 0; l < tickets; l++) {
                    if (seatPlan[rowIndex2][k + l] == 'X') {
                        k = k + l;
                        currentScore = 0;
                        break;
                    }
                    currentScore += seatScore[rowIndex2][k + l];
                }
                if (currentScore > maxScore) {
                    maxScore = currentScore;
                    suggestedSeats.clear();
                    for (int l = 0; l < tickets; l++) {
                        suggestedSeats.add(seatRepository.findByRowAndSeatAndSessionId(rowIndex2 + 1, k + l + 1, sessionId));
                    }
                }
            }
        }
        return suggestedSeats;
    }

    public Seat getSeat(Long sessionId, int rowNumber, int seatNumber) {
        return seatRepository.findByRowAndSeatAndSessionId(rowNumber, seatNumber, sessionId);
    }
}
