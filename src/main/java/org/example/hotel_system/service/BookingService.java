package org.example.hotel_system.service;

import org.example.hotel_system.entity.Booking;
import org.example.hotel_system.mapper.BookingMapper;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;


public interface BookingService {
    int insert(Booking booking);

    int delete(Long id);

    int updateBooking(Booking booking);

    List<Booking> findAll();

    void placeOrder(Booking booking);

    void checkOut(Long id);

    List<Booking> findByName(String name);

    List<Booking> findByCondition(Booking  booking);

    int updateBookingStatus(Integer status, Long id);
}
