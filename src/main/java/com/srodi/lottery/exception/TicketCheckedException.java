package com.srodi.lottery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TicketCheckedException extends RuntimeException {

    public TicketCheckedException(String message) {
        super(message);
    }
}
