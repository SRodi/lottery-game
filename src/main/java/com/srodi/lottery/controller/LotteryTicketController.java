package com.srodi.lottery.controller;

import com.srodi.lottery.model.request.CreateTicketRequest;
import com.srodi.lottery.model.LotteryTicket;
import com.srodi.lottery.service.LotteryTicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class LotteryTicketController {

    private final LotteryTicketService ticketService;

    public LotteryTicketController(LotteryTicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping(path = "/ticket")
    public ResponseEntity<Object> createTicket(@Valid @RequestBody CreateTicketRequest createTicketRequest) {
        LotteryTicket newTicket = ticketService.createTicket(createTicketRequest.getNumberOfLines());

        // return current request URI
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newTicket.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/ticket")
    public List<LotteryTicket> getTickets()
    {
        return ticketService.getAllTickets();
    }

    @GetMapping(path = "/ticket/{id}")
    public LotteryTicket getTicketById(@PathVariable UUID id) {
        return ticketService.getTicketById(id);
    }

    @PutMapping(path = "/ticket/{id}")
    public void amendTicketById(@PathVariable UUID id, @Valid @RequestBody CreateTicketRequest createTicketRequest) {
        ticketService.amendTicketById(id, createTicketRequest.getNumberOfLines());
    }

    @PutMapping(path = "/status/{id}")
    public List<String> checkTicketStatusById(@PathVariable UUID id) {
        return ticketService.checkTicketStatus(id);
    }
}
