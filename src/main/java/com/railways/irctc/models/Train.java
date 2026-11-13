package com.railways.irctc.models;

import java.util.*;

public class Train {
    private String trainNumber;
    private String trainName;
    private List<Seat> seats;

    public Train(String trainNumber, String trainName) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.seats = new ArrayList<>();
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    /**
     * Add seat during train initialization
     */
    public void addSeat(Seat seat) {
        this.seats.add(seat);
    }

    /**
     * Finds an available seat of given type
     */
    public Seat findAvailableSeat(SeatType seatType) {
        return seats.stream()
                .filter(seat -> seat.isAvailable() && seat.getSeatType() == seatType)
                .findFirst()
                .orElse(null);
    }

    /**
     * Count available seats by type
     */
    public long countAvailableSeats(SeatType type) {
        return seats.stream()
                .filter(seat -> seat.isAvailable() && seat.getSeatType() == type)
                .count();
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainNumber='" + trainNumber + '\'' +
                ", trainName='" + trainName + '\'' +
                ", seats=" + seats.size() +
                '}';
    }
}
