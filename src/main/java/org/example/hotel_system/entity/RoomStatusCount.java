package org.example.hotel_system.entity;


import lombok.Data;

@Data
public class RoomStatusCount {
    private Integer available;   // 空闲房间数
    private Integer booked;      // 已预订房间数
    private Integer occupied;    // 已入住房间数
}
