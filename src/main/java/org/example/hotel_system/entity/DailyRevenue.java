package org.example.hotel_system.entity;


import lombok.Data;
import java.math.BigDecimal;

@Data
public class DailyRevenue {
    private String date;              // 日期（如 2026-04-29）
    private BigDecimal revenue;       // 当日营业额
}
