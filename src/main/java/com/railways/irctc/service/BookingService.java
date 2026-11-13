package com.railways.irctc.service;

import com.railways.irctc.models.*;
import com.railways.irctc.reopsitory.BookingRepository;
import com.railways.irctc.reopsitory.TrainRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TrainRepository trainRepository;

    // For simplicity, we’ll keep small queues for RAC and waiting list
    private static final int RAC_LIMIT = 10;
    private static final int WAITING_LIST_LIMIT = 10;

    // trainNumber -> RAC / WL lists
    private final Map<String, Map<SeatType, Queue<Booking>>> racList = new HashMap<>();
    private final Map<String,Map<SeatType, Queue<Booking>>> waitingList = new HashMap<>();

    public BookingService(BookingRepository bookingRepository, TrainRepository trainRepository) {
        this.bookingRepository = bookingRepository;
        this.trainRepository = trainRepository;
    }

    /**
     * BOOK A TICKET
     */
    public Booking bookTicket(String trainNumber, Customer customer) {
        Train train = trainRepository.findByNumber(trainNumber);
        if (train == null) {
            throw new IllegalArgumentException("Train not found!");
        }

        // Find available seat
        Seat availableSeat = train.findAvailableSeat(customer.getPreference());
        BookingStatus status;
        if (availableSeat != null) {
            availableSeat.setAvailable(false);
            status = BookingStatus.CONFIRMED;
        } else if (getRacList(trainNumber).getOrDefault(customer.getPreference(), new LinkedList<>()).size() < RAC_LIMIT) {
            status = BookingStatus.RAC;
        } else if (getWaitingList(trainNumber).getOrDefault(customer.getPreference(), new LinkedList<>()).size() < WAITING_LIST_LIMIT) {
            status = BookingStatus.WAITING_LIST;
        } else {
            throw new IllegalStateException("No seats or waiting list slots available!");
        }

        String pnr = UUID.randomUUID().toString();
        Booking booking = new Booking(pnr, customer, train, availableSeat, status);
        bookingRepository.save(booking);

        // Add to queue if RAC or WL
        if (status == BookingStatus.RAC) {
            getRacList(trainNumber).getOrDefault(customer.getPreference(), new LinkedList<>()).add(booking);
        } else if (status == BookingStatus.WAITING_LIST) {
            getWaitingList(trainNumber).getOrDefault(customer.getPreference(), new LinkedList<>()).add(booking);
        }

        return booking;
    }

    /**
     * CANCEL A BOOKING
     */
    public Booking cancelBooking(String pnr) {
        Booking booking = bookingRepository.findByPnr(pnr);
        if (booking == null) {
            return null;
        }

        Train train = booking.getTrain();
        if (booking.getSeat() != null) {
            // Free up the seat
            booking.getSeat().setAvailable(true);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.update(booking);

        //todo check the BOOKING STATUS AND DO DECISION.

        // Check RAC → Confirm, then WL → RAC
        Map<SeatType, Queue<Booking>> racQueueRef = getRacList(train.getTrainNumber());
        Map<SeatType, Queue<Booking>> wlQueueRef = getWaitingList(train.getTrainNumber());

        Queue<Booking> racQueue = racQueueRef.get(booking.getSeatType());
        Queue<Booking> wlQueue = wlQueueRef.get(booking.getSeatType());


        if (!racQueue.isEmpty()) {
            Booking rcaBooking = racQueue.poll();
            Seat newSeat = train.findAvailableSeat(rcaBooking.getCustomer().getPreference());
            if (newSeat != null) {
                newSeat.setAvailable(false);
                rcaBooking.setSeat(newSeat);
                rcaBooking.setStatus(BookingStatus.CONFIRMED);
                bookingRepository.update(rcaBooking);

                if (!wlQueue.isEmpty()) {
                    Booking wlBooking = wlQueue.poll();
                    wlBooking.setStatus(BookingStatus.RAC);
                    getRacList(train.getTrainNumber()).get(wlBooking.getSeatType()).add(wlBooking);
                    bookingRepository.update(wlBooking);
                }
            }
        }

        System.out.println("Booking cancelled successfully. PNR: " + pnr);
        return  booking;
    }

    /**
     * VIEW A SINGLE BOOKING
     */
    public Booking getBooking(String pnr) {
        return bookingRepository.findByPnr(pnr);
    }

    /**
     * VIEW ALL BOOKINGS
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * VIEW TRAIN DETAILS (INCLUDING SEAT AVAILABILITY)
     */
    public Train getTrainDetails(String trainNumber) {
        return trainRepository.findByNumber(trainNumber);
    }

    /**
     * PREPARE CHART — SHOW ALL CONFIRMED BOOKINGS FOR A TRAIN
     */
    public List<Booking> prepareChart(String trainNumber) {
        List<Booking> allBookings = bookingRepository.findAll();
        List<Booking> confirmed = new ArrayList<>();

        for (Booking b : allBookings) {
            if (b.getTrain().getTrainNumber().equals(trainNumber) && b.getStatus() == BookingStatus.CONFIRMED) {
                confirmed.add(b);
            }
        }

        return confirmed;
    }

    // --- Helper methods for queues ---
    private Map<SeatType, Queue<Booking>> getRacList(String trainNumber) {
        return racList.computeIfAbsent(trainNumber, k -> new HashMap<>());
    }

    private Map<SeatType, Queue<Booking>> getWaitingList(String trainNumber) {
        return waitingList.computeIfAbsent(trainNumber, k -> new HashMap<>());
    }
}
