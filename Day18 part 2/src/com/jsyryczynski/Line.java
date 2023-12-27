package com.jsyryczynski;

import lombok.AllArgsConstructor;

/**
 *
 */
@AllArgsConstructor
public class Line implements Comparable<Line> {
    long x;
    long topY;
    long bottomY;

    @Override
    public int compareTo(Line o) {
        return Long.compare(x, o.x);
    }
}
