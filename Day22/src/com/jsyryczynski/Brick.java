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
public class Brick {
    Position positions[] = new Position[2];
    String name;
    public Brick add(Position position) {
        return new Brick(new Position[]{positions[0].add(position), positions[1].add(position)}, name);
    }
}
