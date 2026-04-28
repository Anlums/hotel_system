package org.example.hotel_system.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Room {
    private Long id;
    private Long roomNumber;
    private String type;
    private BigDecimal price;
    private Integer status; // 0-空闲, 1-已预订
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
