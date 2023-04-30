package com.isa681.scrabble.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class ErrorDetail {
    @Getter @Setter
    private Date timestamp;
    @Getter @Setter
    private String message;
    @Getter @Setter
    private String details;

    public ErrorDetail(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}
