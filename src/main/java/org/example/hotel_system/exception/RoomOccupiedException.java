package org.example.hotel_system.exception;

public class RoomOccupiedException extends RuntimeException {
    private Integer code ;

    public RoomOccupiedException(String message) {
        super(message);
        this.code = 500;
    }
    public RoomOccupiedException(String message, Integer code) {
        super(message);
        this.code = code;
    }
    public Integer getCode() {
        return code;
    }


}
