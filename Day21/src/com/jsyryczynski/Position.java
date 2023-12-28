package com.jsyryczynski;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 *
 */
@AllArgsConstructor
@EqualsAndHashCode
public class Position {
    public int x;
    public int y;

    public Position add(Direction d) {
        return new Position(x + d.x, y + d.y);
    }
}
