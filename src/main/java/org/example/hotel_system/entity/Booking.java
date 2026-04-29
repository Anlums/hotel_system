package org.example.hotel_system.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Booking {
    private Long id;
    private Long roomNumber;
    private String guestName;
    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 双重保障
    private LocalDateTime checkInDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 双重保障
    private LocalDateTime checkOutDate;

    private BigDecimal totalAmount;
    private Integer status; // 订单状态: 1-已预约, 2-已入住, 3-已退房，4-订单已取消'
    private LocalDateTime createTime;
}
