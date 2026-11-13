package com.railways.irctc.reopsitory;

import com.railways.irctc.models.Booking;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BookingRepository {

    private final Map<String, Booking> bookingStore = new HashMap<>();

    public void save(Booking booking) {
        bookingStore.put(booking.getPnr(), booking);
    }

    public Booking findByPnr(String pnr) {
        return bookingStore.get(pnr);
    }

    public List<Booking> findAll() {
        return new ArrayList<>(bookingStore.values());
    }

    public void update(Booking booking) {
        bookingStore.put(booking.getPnr(), booking);
    }

    public void delete(String pnr) {
        bookingStore.remove(pnr);
    }
}
