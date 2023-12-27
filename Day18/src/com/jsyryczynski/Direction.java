package com.jsyryczynski;

import java.util.List;
import java.util.Map;

/**
 *
 */
public enum Direction {
    NORTH(0, -1, 'U', 3),
    EAST(1, 0, 'R', 0),
    SOUTH(0, 1, 'D', 1),
    WEST(-1, 0, 'L', 2),
    NONE(0,0, ' ', -1);

    public int x;
    public int y;
    public char c;
    public int code;

    private static Map<Direction, Direction> negatedMap = Map.of(
            NORTH, SOUTH,
            SOUTH, NORTH,
            EAST, WEST,
            WEST, EAST,
            NONE, NONE
    );

    Direction(int x, int y, char c, int code) {
        this.x = x;
        this.y = y;
        this.c = c;
        this.code = code;
    }

    public Direction negated() {
        return negatedMap.get(this);
    }

    public static List<Direction> getRealValues() {
        return List.of(NORTH, SOUTH, EAST, WEST);
    }

    public static Direction fromChar(Character c) {
        for (var v : values()) {
            if (v.c == c) {
                return v;
            }
        }
        return NONE;
    };
}
