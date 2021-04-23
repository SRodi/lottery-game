package com.srodi.lottery.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Entity
public class LotteryTicket {
    private static final int DEFAULT_NUM_OF_LINES = 5;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Line> lines;

    private boolean checked = false;

    public LotteryTicket() {
        this.lines = new ArrayList<>();
        addNumOfLines(DEFAULT_NUM_OF_LINES);
    }

    public LotteryTicket(int numberOfLines) {
        this.lines = new ArrayList<>();
        addNumOfLines(numberOfLines);
    }

    public UUID getId() {
        return id;
    }

    public List<Line> getLines() {
        return lines;
    }

    public boolean isChecked() {
        return checked;
    }

    public void check() {
        this.checked = true;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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
