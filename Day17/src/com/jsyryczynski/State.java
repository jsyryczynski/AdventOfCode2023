package com.jsyryczynski;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 *
 */
@AllArgsConstructor
@EqualsAndHashCode
public class State implements Comparable<State>{
    public Coordinates position;

    @Override
    public int compareTo(State o) {
        return new Long(cost).compareTo(o.cost);
    }

    enum Direction {
        NORTH(0, -1, '^'),
        EAST(1, 0, '>'),
        SOUTH(0, 1, 'v'),
        WEST(-1, 0, '<'),
        NONE(0,0, ' ');

        public int x;
        public int y;
        public char c;

        private static Map<Direction, Direction> negatedMap = Map.of(
                NORTH, SOUTH,
                SOUTH, NORTH,
                EAST, WEST,
                WEST, EAST,
                NONE, NONE
        );

        Direction(int x, int y, char c) {
            this.x = x;
            this.y = y;
            this.c = c;
        }

        public Direction negated() {
            return negatedMap.get(this);
        }

        public static List<Direction> getRealValues() {
            return List.of(NORTH, SOUTH, EAST, WEST);
        }
    }

    public Direction direction;
    public int stepCount;
    @EqualsAndHashCode.Exclude
    public long cost;
    @EqualsAndHashCode.Exclude
    public State prvState;
}
