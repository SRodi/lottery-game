package com.srodi.lottery.service.implementation;

import com.srodi.lottery.exception.TicketCheckedException;
import com.srodi.lottery.exception.TicketNotFoundException;
import com.srodi.lottery.model.LotteryTicket;
import com.srodi.lottery.repository.LotteryTicketRepository;
import com.srodi.lottery.service.LotteryTicketService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PoppuloLotteryTicketService implements LotteryTicketService {

    private final LotteryTicketRepository ticketRepository;

    public PoppuloLotteryTicketService(LotteryTicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public LotteryTicket createTicket(int numOfLines) {
        return ticketRepository.save(new LotteryTicket(numOfLines));
    }

    @Override
    public LotteryTicket getTicketById(UUID ticketId) {
        return validateTicket(ticketId);
    }

    @Override
    public List<LotteryTicket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public LotteryTicket amendTicketById(UUID ticketId, int numLines) {
        LotteryTicket ticket = validateTicket(ticketId);

        if (ticket.isChecked()) {
            throw new TicketCheckedException(String.format("Ticket for ID: %s has already been checked and cannot be amended.", ticketId));
        }

        // Amend ticket
        ticket.addNumOfLines(numLines);

        // Save the amended ticket
        return ticketRepository.save(ticket);
    }

    @Override
    public List<String> checkTicketStatus(UUID ticketId) {
        LotteryTicket ticket = validateTicket(ticketId);

        if(!ticket.isChecked()){
            ticket.setChecked(true);
            ticketRepository.save(ticket);
        }

        // Sort and return lines
        return ticket
                .getLines()
                .stream()
                .sorted(Comparator.reverseOrder())
                .map(i -> String.format("%-10d %s", i.getScore(), i.getNumbers()))
                .collect(Collectors.toList());
    }

    private LotteryTicket validateTicket(UUID ticketId) {
        Optional<LotteryTicket> ticketOptional = ticketRepository.findById(ticketId);

        if (!ticketOptional.isPresent()) {
            throw new TicketNotFoundException(String.format("Ticket for ID: %s not found.", ticketId));
        }

        return ticketOptional.get();
    }

}
