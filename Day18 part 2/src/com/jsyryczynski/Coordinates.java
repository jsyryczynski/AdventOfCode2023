package com.jsyryczynski;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Coordinates {
    public long x;
    public long y;

    public Coordinates add(Direction dir) {
        return new Coordinates(x + dir.x, y + dir.y);
    }

    public Coordinates add(Direction dir, long len) {
        return new Coordinates(x + len * dir.x, y + len * dir.y);
    }
}
