package org.example.hotel_system.exception;

import org.example.hotel_system.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {
//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<Result> handleException(Exception e) {
//        e.printStackTrace();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(Result.error("服务器异常,请稍后再试：" + e.getMessage()));
//    }
    // 拦截业务逻辑异常 (比如房间被占用)
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        // 返回 e.getMessage()，这就是你在 Service 里 throw new RuntimeException("房间已被占用") 的文字
        return Result.error(e.getMessage());
    }
    // 拦截参数校验异常 (比如姓名不能为空)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationException(MethodArgumentNotValidException e) {
        return Result.error("参数输入有误，请检查后提交");
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
