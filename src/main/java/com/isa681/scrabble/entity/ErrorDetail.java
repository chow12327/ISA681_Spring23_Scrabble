package com.isa681.scrabble.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorDetail {
    private Date timestamp;
    private String message;
    private String details;

    public ErrorDetail(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}
