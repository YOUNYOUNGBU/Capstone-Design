package com.air.air.service;

import com.air.air.model.BookingInfo;
import com.air.air.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // 예약 등록
    public BookingInfo createBooking(BookingInfo bookingInfo) {
        // (여기서 예약번호 중복체크 등 검증 추가 가능)
        return bookingRepository.save(bookingInfo);
    }

    // 유저별 예약 조회
    public List<BookingInfo> getBookingsByUser(String username) {
        return bookingRepository.findByUsername(username);
    }

    // 예약 취소
    public void cancelBooking(String bookingId) {
        bookingRepository.deleteById(bookingId);
    }
}
