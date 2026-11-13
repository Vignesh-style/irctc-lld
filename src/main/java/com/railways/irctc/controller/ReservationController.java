package com.railways.irctc.controller;

import com.railways.irctc.models.Booking;
import com.railways.irctc.models.Customer;
import com.railways.irctc.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/irctc")
public class ReservationController {

    private final BookingService bookingService;

    public ReservationController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<Booking> bookTicket(
            @RequestParam String trainNumber,
            @RequestBody Customer customer) {

        Booking booking = bookingService.bookTicket(trainNumber, customer);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/details")
    public ResponseEntity<?> viewBooking(@RequestParam String pnr) {
        Booking booking = bookingService.getBooking(pnr);
        if (booking == null) {
            return ResponseEntity.status(404).body("Booking not found!");
        }
        return ResponseEntity.ok(booking);
    }

    @DeleteMapping("/cancel?train=6&")
    public ResponseEntity<?> cancelBooking(@RequestParam String pnr) {
        Booking booking = bookingService.cancelBooking(pnr);

        if (booking == null) {
            return ResponseEntity.status(404).body("Booking not found!");
        }
        return ResponseEntity.ok(booking);
    }
}
