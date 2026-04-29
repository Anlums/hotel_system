package org.example.hotel_system.service_impl;

import org.example.hotel_system.entity.Booking;
import org.example.hotel_system.entity.DailyRevenue;
import org.example.hotel_system.entity.Room;
import org.example.hotel_system.exception.RoomOccupiedException;
import org.example.hotel_system.mapper.BookingMapper;
import org.example.hotel_system.mapper.RoomMapper;
import org.example.hotel_system.service.BookingService;
import org.example.hotel_system.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class BookingServiceImpl implements BookingService {
    @Autowired private BookingMapper bookingMapper;
    @Autowired private RoomMapper roomMapper;
    @Override
    public List<Booking> findByCondition(Booking booking) {
        return bookingMapper.findByCondition(booking);
    }
    @Override
    public List<Booking>  findByName(String name) {
        return bookingMapper.findByName(name);
    }
    @Transactional
    @Override
    public void checkOut(Long id) {
        Booking booking = bookingMapper.findById( id);
        if(booking == null) throw new RuntimeException("id为" + id + "的订单不存在");

        if(booking.getStatus() == 4) throw new RuntimeException("id为" + id + "的订单已取消");

        Room room = roomMapper.findByRoomNumber(booking.getRoomNumber());
        if(room == null) throw new RuntimeException("房间号为" + booking.getRoomNumber() + "的房间不存在");
        LocalDateTime checkInDate = booking.getCheckInDate();
        LocalDateTime checkOutDate = LocalDateTime.now();
        // 计算天数差（不足一天按一天算，或者根据业务定）
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate );
        if (days <= 0) days = 1;
        BigDecimal totalPrice = room.getPrice().multiply(BigDecimal.valueOf(days));

        int c1 = bookingMapper.checkOut(id,checkOutDate, totalPrice);
        System.out.println("订单更新结果: " + c1 + ", 订单ID: " + id);

        int c2 = roomMapper.updateRoomStatus(booking.getRoomNumber(), 0);
        System.out.println("房间状态更新结果: " + c2 + ", 房间ID: " + booking.getRoomNumber());

        if (c1 == 0) throw new RuntimeException("更新订单状态失败，订单ID: " + id);
        if (c2 == 0) throw new RuntimeException("更新房间状态失败，房间ID: " + booking.getRoomNumber());
    }
    @Override
    @Transactional
    public int insert(Booking booking) {
        Room room = roomMapper.findByRoomNumber(booking.getRoomNumber());
        if(room == null) throw new RuntimeException("房间号为" + booking.getRoomNumber() + "的房间不存在");
        if(room != null && room.getStatus() == 1)
            throw new RoomOccupiedException("房间号为" + booking.getRoomNumber() + "的房间已被占用，无法预约");

        int c = bookingMapper.insert(booking);
        roomMapper.updateRoomStatus(booking.getRoomNumber(), 1);
        return c;
//        if(c == 0) System.err.println("新建订单失败");
    }
    @Override
    @Transactional
    public int delete(Long id) {
        Booking booking = bookingMapper.findById(id);
        if(booking == null) throw new RuntimeException("id为" + id + "的订单不存在");
        int c = bookingMapper.delete(id);
        if(c != 0) roomMapper.updateRoomStatus(booking.getRoomNumber(), 0);
        return c;
//        try {
//            int c = bookingMapper.delete(id);
//            if(c != 0) {
//                roomMapper.updateRoomStatus(bookingMapper.findById(id).getRoomNumber(), 0);
//                return c;
//            }
//            else throw new RuntimeException("删除订单失败：id为" +  id + "的订单不存在或已删除");
//        } catch (Exception e) {
//            throw new RuntimeException("删除订单失败：id为" +  id + "的订单不存在或已删除");
//        }
    }
    @Override
    @Transactional
    public int updateBooking(Booking booking) {
        // 1. 获取数据库中的原始订单数据
        Booking oldBooking = bookingMapper.findById(booking.getId());
        if (oldBooking == null) {
            throw new RuntimeException("该订单不存在");
        }

        // 2. 核心逻辑：判断是否更改了房间号
        if (booking.getRoomNumber() != null && !booking.getRoomNumber().equals(oldBooking.getRoomNumber())) {

            // A. 检查新房间是否可用（假设 0 为空闲）
            Integer newRoomStatus = roomMapper.findByRoomNumber(booking.getRoomNumber()).getStatus();
            if (newRoomStatus != null && newRoomStatus != 0) {
                throw new RuntimeException("新房间 " + booking.getRoomNumber() + " 已被占用，无法换房");
            }
            // B. 释放旧房间（改为 0）
            roomMapper.updateRoomStatus(oldBooking.getRoomNumber(), 0);
            // C. 占用新房间（改为 1）
            roomMapper.updateRoomStatus(booking.getRoomNumber(), 1);

            // D. 手动同步新的房间号到 booking 对象，以便更新到数据库
            // 虽然 XML 里没写 room_number，但建议你 XML 的 <set> 里最好加上 room_number 的更新
        }
        // 3. 调用你 XML 中写的 update 方法更新订单表
        int c = bookingMapper.update(booking);
//        if(c == 0) System.out.println("修改订单失败，id为" + booking.getId() + "的订单不存在");
        return c;
    }
    @Override
    public List<Booking> findAll() {
        return bookingMapper.findAll();
    }
    @Override
    @Transactional
    public void placeOrder(Booking booking) {
        Room room = roomMapper.findByRoomNumber(booking.getRoomNumber());
        if(room == null) throw new RuntimeException("房间号为" + booking.getRoomNumber() + "的房间不存在");
        if(room.getStatus() == 1) throw new RoomOccupiedException("房间号为" + booking.getRoomNumber() + "的房间已被占用，无法下单");
        bookingMapper.insert(booking);
        roomMapper.updateRoomStatus(booking.getRoomNumber(), 1);
    }

    @Override
    @Transactional
    public int updateBookingStatus(Integer status, Long id) {
        Booking booking = bookingMapper.findById(id);
        if(booking == null) throw new RuntimeException("id为" + id + "的订单不存在");
        booking.setStatus(status);
        int c = bookingMapper.updateBookingStatus(status, id);
        if(c == 0) throw new RuntimeException("更新订单状态失败，订单ID: " + id);

        // 3. 房间状态联动逻辑
        if(status == 1) {
            // 预约入住 -> 确保房间状态为 1 (占用)
            roomMapper.updateRoomStatus(booking.getRoomNumber(), 1);
        } else if (status == 2) {
            // 办理入住 -> 确保房间状态为 1 (占用)
            roomMapper.updateRoomStatus(booking.getRoomNumber(), 1);
        } else if (status == 3) {
            // 退房 -> 释放房间为 0 (空闲)
            roomMapper.updateRoomStatus(booking.getRoomNumber(), 0);
        } else if (status == 4) {
            // 取消订单 -> 释放房间为 0 (空闲)
            roomMapper.updateRoomStatus(booking.getRoomNumber(), 0);
        }

        return c;
    }
    @Override
    public List<DailyRevenue> getDailyRevenue(Integer days) {
        return bookingMapper.selectDailyRevenue(days);
    }

}
