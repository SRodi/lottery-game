package com.srodi.lottery.repository;

import com.srodi.lottery.model.LotteryTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LotteryTicketRepository extends JpaRepository<LotteryTicket, UUID> {
    Optional<LotteryTicket> findById(UUID ticketId);
}
