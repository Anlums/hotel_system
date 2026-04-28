package org.example.hotel_system.controller;


import org.example.hotel_system.common.Result;
import org.example.hotel_system.entity.Room;
import org.example.hotel_system.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @GetMapping("/allRoom")
    public Result<List<Room>> findAllRoom() {
        return Result.success(roomService.findAll());
    }
    @GetMapping("/availableRooms")
    public Result<List<Room>> list() {
        return Result.success(roomService.getAvailableRooms());
    }
    @GetMapping("/bookedRooms")
    public Result<List<Room>> findBookedRoom() {
        return Result.success(roomService.findBookedRooms());
    }

    @PostMapping("/insertRoom")
    public Result insertRoom(@RequestBody Room room) {
        int c = roomService.insert(room);
        if(c == 0) throw new RuntimeException("房间号为" + room.getRoomNumber() + "的房间已存在，新增房间失败");
        else return Result.success(room);
    }



}
