package edu.ctu.SpringShopBE.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class ErrorResponse{
    private int status;
    private String message;
    private String timestamp; // Đổi kiểu dữ liệu từ long sang String

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = formatTimestamp(System.currentTimeMillis());
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }
}
