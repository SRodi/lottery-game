package com.srodi.lottery.model.request;

import javax.validation.constraints.Min;

public class CreateTicketRequest {
    @Min(1)
    private int numberOfLines;

    public int getNumberOfLines() {
        return numberOfLines;
    }
}
