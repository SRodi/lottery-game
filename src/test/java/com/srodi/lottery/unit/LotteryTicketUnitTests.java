package com.srodi.lottery.unit;

import com.srodi.lottery.exception.TicketCheckedException;
import com.srodi.lottery.exception.TicketNotFoundException;
import com.srodi.lottery.model.LotteryTicket;
import com.srodi.lottery.repository.LotteryTicketRepository;
import com.srodi.lottery.service.LotteryTicketService;
import com.srodi.lottery.service.implementation.PoppuloLotteryTicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class LotteryTicketUnitTests {
    @Mock
    private LotteryTicketRepository mockTicketRepository;

    @Test
    public void createTicketSuccessTest() {
        // Mock the returned object from the save method
        when(mockTicketRepository.save(any(LotteryTicket.class))).thenReturn(getMockTicket(false));

        // Instantiate an instance of the implementation with mock repo
        LotteryTicketService ticketService = new PoppuloLotteryTicketService(mockTicketRepository);
        LotteryTicket ticket = ticketService.createTicket(100);

        // Assert that the expected and actual are equal
        assertEquals(getMockTicket(false).isChecked(), ticket.isChecked());
        assertEquals(getMockTicket(false).getLines().size(), ticket.getLines().size());
        assertEquals(getMockTicket(false).getId(), ticket.getId());
    }

    @Test
    public void getTicketByIdSuccessTest() {
        // Mock the returned object from the findById method
        when(mockTicketRepository.findById(any(UUID.class))).thenReturn(getMockTicketResult(false));

        // Instantiate an instance of the implementation with mock repo
        LotteryTicketService ticketService = new PoppuloLotteryTicketService(mockTicketRepository);
        LotteryTicket ticket = ticketService.getTicketById(getMockUUID());

        // Assert that the expected and actual are equal
        assertEquals(getMockTicket(false).isChecked(), ticket.isChecked());
        assertEquals(getMockTicket(false).getLines().size(), ticket.getLines().size());
        assertEquals(getMockTicket(false).getId(), ticket.getId());
    }

    @Test
    public void getTicketExceptionThrownTest() {
        when(mockTicketRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        // Instantiate an instance of the implementation with mock repo
        LotteryTicketService ticketService = new PoppuloLotteryTicketService(mockTicketRepository);

        // Assert that the LinesNotValidException is thrown
        Throwable exception = assertThrows(TicketNotFoundException.class, () ->
        {
            // Number of lines should be at least 1. It will throw the exception on '0'
            ticketService.getTicketById(getMockUUID());
        });

        // Assert that the exception error message is the same as our expectation
        assertEquals("Ticket for ID: 00000000-0000-0064-0000-000000000064 not found.", exception.getMessage());
    }

    @Test
    public void getAllTicketsSuccessTest() {
        // Mock the returned object from the findById method
        when(mockTicketRepository.findAll()).thenReturn(getMockTickets());

        // Instantiate an instance of the implementation with mock repo
        LotteryTicketService ticketService = new PoppuloLotteryTicketService(mockTicketRepository);
        List<LotteryTicket> tickets = ticketService.getAllTickets();

        // Assert that the expected and actual are equal.
        // Note: We could check the resulting ticket in the list
        // against our expectations for added integrity
        assertEquals(1, tickets.size());
    }

    @Test
    public void amendTicketByIdTicketCheckedExceptionTest() {
        when(mockTicketRepository.findById(any(UUID.class))).thenReturn(getMockTicketResult(true));

        // Instantiate an instance of the implementation with mock repo
        LotteryTicketService ticketService = new PoppuloLotteryTicketService(mockTicketRepository);

        // Assert that the TicketCheckedException is thrown
        Throwable exception = assertThrows(TicketCheckedException.class, () ->
        {
            //
            ticketService.amendTicketById(getMockUUID(), 1);
        });

        // Assert that the exception error message is the same as our expectation
        assertEquals("Ticket for ID: 00000000-0000-0064-0000-000000000064 has already been checked and cannot be amended.", exception.getMessage());
    }

    @Test
    public void amendTicketByIdSuccessTest() {
        // Mock the returned object from the save method
        when(mockTicketRepository.findById(any(UUID.class))).thenReturn(getMockTicketResult(false));
        when(mockTicketRepository.save(any(LotteryTicket.class))).thenReturn(getMockTicket(false));

        // Instantiate an instance of the implementation with mock repo
        LotteryTicketService ticketService = new PoppuloLotteryTicketService(mockTicketRepository);

        LotteryTicket ticket = ticketService.amendTicketById(getMockUUID(), 1);

        // Assert that the expected and actual are equal
        assertEquals(getMockTicket(false).isChecked(), ticket.isChecked());
        assertEquals(getMockTicket(false).getLines().size(), ticket.getLines().size());
        assertEquals(getMockTicket(false).getId(), ticket.getId());
    }

    @Test
    public void checkTicketStatusTicketCheckedSuccessTest() {
        // Mock the returned object from the save method
        when(mockTicketRepository.findById(any(UUID.class))).thenReturn(getMockTicketResult(true));

        // Instantiate an instance of the implementation with mock repo
        LotteryTicketService ticketService = new PoppuloLotteryTicketService(mockTicketRepository);

        List<String> lines = ticketService.checkTicketStatus(getMockUUID());

        // Assert that the expected and actual are equal
        // Note: We should also check that the sorting is correct,
        // however there currently is no native Matcher() implementation for this functionality
        assertEquals(100, lines.size());
    }

    @Test
    public void checkTicketStatusSuccessTest() {
        // Mock the returned object from the save method
        when(mockTicketRepository.findById(any(UUID.class))).thenReturn(getMockTicketResult(false));
        when(mockTicketRepository.save(any(LotteryTicket.class))).thenReturn(getMockTicket(true));

        // Instantiate an instance of the implementation with mock repo
        LotteryTicketService ticketService = new PoppuloLotteryTicketService(mockTicketRepository);

        List<String> lines = ticketService.checkTicketStatus(getMockUUID());

        // Assert that the expected and actual are equal
        // Note: We should also check that the sorting is correct,
        // however there currently is no native Matcher() implementation for this functionality
        assertEquals(100, lines.size());
    }

    private LotteryTicket getMockTicket(boolean isChecked) {
        LotteryTicket ticket = new LotteryTicket(100);
        ticket.setChecked(isChecked);
        ticket.setId(getMockUUID());
        return ticket;
    }

    private List<LotteryTicket> getMockTickets() {
        List<LotteryTicket> tickets = new ArrayList<>();
        tickets.add(getMockTicket(false));
        return tickets;
    }

    private UUID getMockUUID() {
        return new UUID(100L, 100L);
    }

    private Optional<LotteryTicket> getMockTicketResult(boolean isChecked) {
        return Optional.of(getMockTicket(isChecked));
    }
}