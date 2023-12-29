package com.jsyryczynski;

import java.util.List;
import java.util.Map;

/**
 *
 */
public enum Direction {
    DOWN(0, -1, 0),
    UP(0, 1, 0);

    public int x;
    public int y;
    public int z;

    private static Map<Direction, Direction> negatedMap = Map.of(
            UP, DOWN,
            DOWN, UP
    );

    Direction(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Direction negated() {
        return negatedMap.get(this);
    }
}
