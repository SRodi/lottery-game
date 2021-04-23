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

        if (!ticket.isChecked()) {
            // check ticket and save
            ticket.check();
            ticketRepository.save(ticket);
        }

        // Sort and return lines
        return ticket
                .getLines()
                .stream()
                .map(i -> String.format("%d %s", generateScore(i.getNumbers()), i.getNumbers()))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private LotteryTicket validateTicket(UUID ticketId) {
        Optional<LotteryTicket> ticketOptional = ticketRepository.findById(ticketId);

        if (!ticketOptional.isPresent()) {
            throw new TicketNotFoundException(String.format("Ticket for ID: %s not found.", ticketId));
        }

        return ticketOptional.get();
    }

    /**
     * Checks what the score of the line is
     * @return the score
     */
    private static int generateScore(List<Integer> numbers) {
        // Get the sum of all the numbers in the Line's list
        int sumOfNumbers = numbers
                .stream()
                .mapToInt(Integer::intValue)    // Map to an IntStream upon which we can use math operations
                .sum();                         // Sum all numbers

        if (sumOfNumbers == 2) {
            return 10;
        }

        // Determine equality of numbers values - if true return '5'
        boolean areAllEqual = numbers
                .stream()
                .distinct()     // Get a stream of distinct values
                .limit(2)       // We only need two differing values to know that at least one value is different
                .count() == 1;  // If we have exactly one, all are equal

        if (areAllEqual) {
            return 5;
        }

        // Check if first number is unique
        boolean firstIsUnique = numbers
                .stream()
                .skip(1)        // Skip the first item
                .noneMatch(     // The below predicate will return true if all numbers (after first) are equal to first
                        number -> number.equals(numbers.get(0))
                );

        if (firstIsUnique) {
            return 1;
        }

        return 0;
    }
}
