package org.example.hotel_system.service;

import org.example.hotel_system.entity.Room;

import java.lang.invoke.CallSite;
import java.util.List;

public interface RoomService {
    List<Room> findAll();
    List<Room> getAvailableRooms();
    List<Room> findBookedRooms();
    int updateRoomStatus(Long roomNumber, Integer status);
    int insert(Room room);
    Room findByRoomNumber(Long roomNumber);
}