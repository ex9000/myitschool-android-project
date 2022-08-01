package com.sr9000.gdx.x3p1.business.part.struct;

import java.time.LocalDateTime;

public class Record {
    public long number, hops;
    public LocalDateTime datetime;
    public String author;

    public Record() {
        number = 0;
        hops = 0;
        author = "";
        datetime = LocalDateTime.MIN;
    }

    public Record(Record other) {
        number = other.number;
        hops = other.hops;
        author = other.author;
        datetime = LocalDateTime.from(other.datetime);
    }

    public Record(long number, long hops, String author) {
        this.number = number;
        this.hops = hops;
        this.author = author;

        datetime = LocalDateTime.now();
    }
}
