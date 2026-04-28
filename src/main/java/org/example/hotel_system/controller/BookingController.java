package org.example.hotel_system.controller;

import org.example.hotel_system.common.Result;
import org.example.hotel_system.entity.Booking;
import org.example.hotel_system.mapper.BookingMapper;
import org.example.hotel_system.service.BookingService;
import org.example.hotel_system.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    BookingService bookingService;
//    @Autowired
//    RoomService roomService;

    @PostMapping("/save")
    public Result save(@RequestBody Booking booking) {
        int flag = bookingService.insert( booking);
        if(flag == 0) return Result.error("新增订单失败");
        return Result.success(booking);
    }

    @GetMapping("/findByCondition")
    public Result<List<Booking>> findByCondition(@ModelAttribute Booking  booking) {
        // Spring 会自动将 URL 参数映射到对象的属性中
        return Result.success(bookingService.findByCondition(booking));
    }
    @GetMapping("/findByName")
    public Result<List<Booking>> findByName(@RequestParam String name) {
        return Result.success(bookingService.findByName(name));
    }

    @PutMapping("/checkOut")
    public Result checkOut(@RequestParam Long id) {
        bookingService.checkOut(id);
        return Result.success("id为" + id + "的订单已退房");
    }

    @PostMapping("/placeOrder")
    public Result placeOrder(@RequestBody Booking booking) {
        bookingService.placeOrder( booking);
        return Result.success(booking);
    }

    @PostMapping("/add")
    public Result insert(@RequestBody Booking booking) {
        int c = bookingService.insert(booking);
        if(c == 0) return Result.error("新增订单失败");
        else return Result.success(booking);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam Long id) {
        int c = bookingService.delete(id);
        if(c == 0) return Result.error("删除订单失败: 该订单不存在或已删除");
        else return Result.success("删除id为" + id + "的订单成功");
    }

    @PutMapping("/update")
    public Result update(@RequestBody Booking booking) {
        int c = bookingService.update(booking);
        if(c == 0) return Result.error("修改订单失败,id为" + booking.getId() + "的订单不存在或已删除");
        else return Result.success(booking);
    }
    @GetMapping("/list")
    public Result<List<Booking>> list() {
        return Result.success(bookingService.findAll());
    }



}
