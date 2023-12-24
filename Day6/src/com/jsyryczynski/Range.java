package com.jsyryczynski;

/**
 *
 */
public class Range implements Comparable<Range>{
    public Long start;
    public Long length;
    public Long end;
    Range(Long start, Long length) {
        this.start = start;
        this.length = length;
        end = start + length - 1;
    }

    @Override
    public int compareTo(Range o) {
        return Long.compare(start, o.start);
    }

    static Range fromBeginEnd(Long start, Long end) {
        return new Range(start, end - start + 1);
    }
}
