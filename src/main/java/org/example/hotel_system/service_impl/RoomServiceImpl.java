package org.example.hotel_system.service_impl;

import org.example.hotel_system.entity.Room;
import org.example.hotel_system.mapper.RoomMapper;
import org.example.hotel_system.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class RoomServiceImpl implements RoomService{
    @Autowired
    private RoomMapper roomMapper;
    @Override
    @Transactional
    public int insert(Room room) {
        try {
            int c = roomMapper.insert(room);
            if(c == 0) System.out.println("新增房间失败");
            return c;
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("房间号为" + room.getRoomNumber() + "的房间已存在，新增房间失败");
        }

    }
    @Override
    public List<Room> findAll() {
        return roomMapper.findAll();
    }
    @Override
    public List<Room> getAvailableRooms() {
        return roomMapper.findAvailableRooms();
    }

    @Override
    public int updateRoomStatus(Long id, Integer status) {
        return roomMapper.updateRoomStatus(id, status);
    }
    @Override
    public List<Room> findBookedRooms() {
        return roomMapper.findBookedRooms();
    }
    @Override
    public Room findByRoomNumber(Long roomNumber) {
        return roomMapper.findByRoomNumber(roomNumber);
    }
}
