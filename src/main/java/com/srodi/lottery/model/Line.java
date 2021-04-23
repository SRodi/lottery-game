package com.srodi.lottery.model;

import com.srodi.lottery.rules.LotteryRules;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Line implements Comparable<Line>{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID lineId;

    @ElementCollection
    private final List<Integer> numbers;

    @ManyToOne
    private LotteryTicket lotteryTicket;

    private final int score;

    public Line(LotteryTicket lotteryTicket) {
        this.numbers = LotteryRules.generateRandomNumbers();
        this.lotteryTicket = lotteryTicket;
        this.score = LotteryRules.generateScore(this.numbers);
    }

    public Line() {
        this.numbers = new ArrayList<>();
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    @Override
    public int compareTo(Line line) {
        return Integer.compare(this.score, line.score);
    }
}
