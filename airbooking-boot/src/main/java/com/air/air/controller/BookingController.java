package com.air.air.controller;

import com.air.air.model.BookingInfo;
import com.air.air.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // 예약 등록 (결제 완료시 호출)
    @PostMapping
    public BookingInfo createBooking(@RequestBody BookingInfo bookingInfo) {
        return bookingService.createBooking(bookingInfo);
    }

    // 유저별 예약내역 조회
    @GetMapping("/by-user/{username}")
    public List<BookingInfo> getBookingsByUser(@PathVariable String username) {
        return bookingService.getBookingsByUser(username);
    }

    // 예약 취소
    @DeleteMapping("/{bookingId}")
    public void cancelBooking(@PathVariable String bookingId) {
        bookingService.cancelBooking(bookingId);
    }
}
