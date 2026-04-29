package org.example.hotel_system.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.hotel_system.entity.Room;
import java.util.List;

@Mapper
public interface RoomMapper {
    @Select("SELECT * FROM rooms ")
    List<Room> findAll();
    @Select("SELECT * FROM rooms WHERE status = 0") // 只查询空闲房间
    List<Room> findAvailableRooms();
    //根据roomId更新房间状态
    @Update("UPDATE rooms SET status = #{status} WHERE room_number = #{roomNumber}")
    int updateRoomStatus(Long roomNumber,Integer status);
    //根据roomId查询房间
    @Select("SELECT * FROM rooms WHERE room_number = #{roomNumber}")
    Room findByRoomNumber(Long roomNumber);
    //查询已预订房间
    @Select("SELECT * FROM rooms WHERE status = 1")
    List<Room> findBookedRooms();
    //@Insert("INSERT INTO rooms (room_number, type, price, status) VALUES (#{roomNumber}, #{type}, #{price}, #{status})")
    int insert(Room room) ;

    @Select("SELECT COUNT(*) FROM rooms WHERE status = 0")
    Integer countAvailableRooms();

    @Select("SELECT COUNT(*) FROM rooms WHERE status = 1")
    Integer countBookedRooms();

    @Select("SELECT COUNT(*) FROM rooms WHERE status = 2")
    Integer countOccupiedRooms();


}