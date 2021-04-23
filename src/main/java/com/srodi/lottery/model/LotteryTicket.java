package com.srodi.lottery.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.toList;

@Getter @Setter @NoArgsConstructor
@Entity
public class LotteryTicket {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Line> lines = new ArrayList<>();

    private boolean checked = false;

    public LotteryTicket(int numberOfLines) {
        addNumOfLines(numberOfLines);
    }

    /**
     * Add list of new Line objects to existing list of Line
     *  - Iterate numOfLines times
     *  - Create a new Line object for each iteration
     *  - Convert back to a list of Line objects
     * @param numOfLines number of lines to be added to the Ticket
     */
    public void addNumOfLines(int numOfLines) {
        this.lines.addAll(
                IntStream
                        .range(0, numOfLines)
                        .mapToObj(i -> new Line(this))
                        .collect(toList())
        );
    }
}
