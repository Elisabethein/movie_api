package com.api.movie_api.Services;

import com.api.movie_api.Entities.Seat;
import com.api.movie_api.Repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public String bookSeat(Long sessionId, int rowNumber, int seatNumber) {
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

    public char[][] getSeatPlan(Long sessionId) {
        List<Seat> seatList = seatRepository.findBySessionId(sessionId);
        if (seatList.isEmpty()) {
            throw new IllegalStateException("Invalid session ID");
        }
        char[][] seatPlan = new char[9][9];
        for (Seat seat : seatList) {
            seatPlan[seat.getRow() - 1][seat.getSeat() - 1] = seat.isReserved() ? 'X' : 'O';
        }
        return seatPlan;
    }

    public String getSuggestedSeat(Long sessionId, int tickets) {
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
        int[] ticketList = new int[tickets];
        int row = 0;
        int i = seatScore.length / 2;
        for (int j = 0; j <= seatScore.length / 2; j++) {
            int rowIndex1 = i + j;
            int rowIndex2 = i - j;
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
                    for (int l = 0; l < tickets; l++) {
                        ticketList[l] = k + l + 1;
                        row = rowIndex1 + 1;
                    }
                }
            }
            if (rowIndex1 == rowIndex2) {
                continue;
            }
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
                    for (int l = 0; l < tickets; l++) {
                        ticketList[l] = k + l + 1;
                        row = rowIndex2 + 1;
                    }
                }
            }
        }
        
        return "Row: " + row + ", Seats: " + ticketList[0] + " - " + ticketList[tickets - 1];
    }
}
