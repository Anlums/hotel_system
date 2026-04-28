package org.example.hotel_system.exception;

import org.example.hotel_system.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Result> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error("服务器异常,请稍后再试：" + e.getMessage()));
    }

    @ExceptionHandler(java.sql.SQLException.class)
    public ResponseEntity<Result> handleSqlException(java.sql.SQLException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error("数据库操作失败，请检查数据格式：" + e.getMessage()));
    }

    @ExceptionHandler(RoomOccupiedException.class)
    public ResponseEntity<Result> handleFullRoomException(RoomOccupiedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Result.error(e.getMessage()));
    }


}
