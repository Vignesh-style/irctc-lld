package com.railways.irctc.models;

public class Seat {
    private int seatNumber;
    private SeatType seatType;
    private boolean isAvailable;

    public Seat(int seatNumber, SeatType seatType) {
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.isAvailable = true; // default
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatNumber=" + seatNumber +
                ", seatType=" + seatType +
                ", available=" + isAvailable +
                '}';
    }
}
