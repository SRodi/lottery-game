package com.srodi.lottery.model;

import com.srodi.lottery.rules.LotteryRules;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor
@Entity
public class Line implements Comparable<Line>{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID lineId;

    @ElementCollection
    private List<Integer> numbers = new ArrayList<>();

    @ManyToOne
    private LotteryTicket lotteryTicket;

    private int score = 0;

    public Line(LotteryTicket lotteryTicket) {
        numbers = LotteryRules.generateRandomNumbers();
        this.lotteryTicket = lotteryTicket;
        score = LotteryRules.generateScore(numbers);
    }

    @Override
    public int compareTo(Line line) {
        return Integer.compare(this.score, line.score);
    }
}
