package com.railways.irctc.models;

import java.time.Instant;

public class Booking {
    private String pnr;
    private Customer customer;
    private Train train;
    private Seat seat;
    private BookingStatus status;
    private long bookingTime;
    private SeatType seatType;

    public Booking(String pnr, Customer customer, Train train, Seat seat, BookingStatus status) {
        this.pnr = pnr;
        this.customer = customer;
        this.train = train;
        this.seat = seat;
        this.status = status;
        this.bookingTime = Instant.now().toEpochMilli();
        this.seatType = seat != null ? seat.getSeatType() : customer.getPreference();
    }

    public String getPnr() {
        return pnr;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Train getTrain() {
        return train;
    }

    public Seat getSeat() {
        return seat;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public long getBookingTime() {
        return bookingTime;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "PNR='" + pnr + '\'' +
                ", customer=" + customer +
                ", train=" + train.getTrainName() +
                ", seat=" + (seat != null ? seat.getSeatNumber() : "N/A") +
                ", type=" + seatType +
                ", status=" + status +
                '}';
    }
}
