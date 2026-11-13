package com.railways.irctc.reopsitory;

import com.railways.irctc.models.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TrainRepository {
    private final Map<String, Train> trainStore = new HashMap<>();

    public TrainRepository() {
        // Initialize demo data
        Train train = new Train("101", "Chennai Express");

        // Add 60 seats per type (AC, NON_AC, SEATER)
        int seatNum = 1;
        for (SeatType type : SeatType.values()) {
            for (int i = 0; i < 60; i++) {
                train.addSeat(new Seat(seatNum++, type));
            }
        }

        trainStore.put(train.getTrainNumber(), train);
    }

    public Train findByNumber(String trainNumber) {
        return trainStore.get(trainNumber);
    }

    public List<Train> findAll() {
        return new ArrayList<>(trainStore.values());
    }

    public void save(Train train) {
        trainStore.put(train.getTrainNumber(), train);
    }
}
