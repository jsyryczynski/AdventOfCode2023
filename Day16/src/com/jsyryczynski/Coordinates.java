package com.jsyryczynski;

import com.jsyryczynski.Beam.Direction;
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

    public Coordinates add(Direction direction) {
        return new Coordinates(x+direction.x, y+direction.y);
    }
}
