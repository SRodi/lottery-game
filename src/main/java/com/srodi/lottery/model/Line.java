package com.srodi.lottery.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Entity
public class Line {
    private static final Random random = new Random();

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID lineId;

    @ElementCollection
    private final List<Integer> numbers;

    @ManyToOne
    private LotteryTicket lotteryTicket;

    public Line() {
        this.numbers = new ArrayList<>();
    }

    public Line(LotteryTicket lotteryTicket) {
        this.numbers = List.of(
                random.nextInt(3),
                random.nextInt(3),
                random.nextInt(3)
        );

        this.lotteryTicket = lotteryTicket;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }
}
