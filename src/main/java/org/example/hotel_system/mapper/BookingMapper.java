package org.example.hotel_system.mapper;


import org.apache.ibatis.annotations.*;
import org.example.hotel_system.entity.Booking;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BookingMapper {

    @Select("SELECT * FROM bookings WHERE guest_name LIKE CONCAT('%',#{name},'%')")
    List<Booking> findByName(String name);

    List<Booking> findByCondition(Booking  booking);

    @Select("SELECT * FROM bookings WHERE id= #{id}")
    Booking findById(Long id);

    List<Booking> findByIds(List<Long> ids);

    List<Booking> findByPriority(Booking  booking );

    int insert(Booking booking);

    int delete(Long id);

    // 注意：如果是在 IDEA 里，直接写成一整行最保险，或者利用 IDEA 自动的字符串连接
    int update(Booking booking);

    @Update("UPDATE bookings SET status = #{status} WHERE id = #{id}")
    int updateBookingStatus(Integer status, Long id);

    @Select("SELECT * FROM bookings")
    List<Booking> findAll();

    int checkOut(Long bookingId, LocalDateTime checkOutDate, BigDecimal totalAmount);


}