package com.srodi.lottery.service;

import com.srodi.lottery.model.LotteryTicket;

import java.util.List;
import java.util.UUID;

public interface LotteryTicketService
{
    LotteryTicket createTicket(int numOfLines);

    LotteryTicket getTicketById(UUID ticketId);

    List<LotteryTicket> getAllTickets();

    LotteryTicket amendTicketById(UUID ticketId, int numLines);

    List<String> checkTicketStatus(UUID ticketId);
}
