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
    public int x;
    public int y;

    public Coordinates add(Direction dir) {
        return new Coordinates(x + dir.x, y + dir.y);
    }
}
