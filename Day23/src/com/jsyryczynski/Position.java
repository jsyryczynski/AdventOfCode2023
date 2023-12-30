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
public class Position {
    public int x;
    public int y;

    public Position add(Position d) {
        return new Position(x + d.x, y + d.y);
    }
}
