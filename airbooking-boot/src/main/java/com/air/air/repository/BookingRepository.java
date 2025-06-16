package com.air.air.repository;

import com.air.air.model.BookingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingInfo, String> {
    List<BookingInfo> findByUsername(String username); // ⭐️ 반드시 있어야 함
}
