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
    public int z;

    public Position(Position p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public Position add(Direction d) {
        return new Position(x + d.x, y + d.y, z + d.z);
    }

    public Position add(Position d) {
        return new Position(x + d.x, y + d.y, z + d.z);
    }

    public int distance(Position p2) {
        return (p2.x - x) + (p2.y - y) + (p2.z - z);
    }

    public Position vector(Position p2) {
        return new Position(getVectorDimention(p2.x - x), getVectorDimention(p2.y - y), getVectorDimention(p2.z - z));
    }

    private int getVectorDimention(int diff) {
        return diff > 0 ? 1 : (diff == 0 ? 0 : -1);
    }
}
