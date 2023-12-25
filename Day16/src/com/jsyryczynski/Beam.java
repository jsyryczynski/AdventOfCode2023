package com.jsyryczynski;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 *
 */
@AllArgsConstructor
@EqualsAndHashCode
public class Beam {
    public Coordinates position;

    public Beam move(Character currentChar) {
        Beam newBeam = null;
        if (currentChar == '|' && (direction == Direction.EAST || direction == Direction.WEST)) {
            newBeam = new Beam(position.add(Direction.SOUTH), Direction.SOUTH);
            direction = Direction.NORTH;
        }
        else if (currentChar == '-' && (direction == Direction.NORTH || direction == Direction.SOUTH)) {
            newBeam = new Beam(position.add(Direction.EAST), Direction.EAST);
            direction = Direction.WEST;
        }
        else if (currentChar == '/') {
            direction = Direction.fromCoords(-1 * direction.y, -1 * direction.x);
        }
        else if (currentChar == '\\') {
            direction = Direction.fromCoords(direction.y, direction.x);
        }

        position = position.add(direction);

        return newBeam;
    }

    @AllArgsConstructor
    enum Direction {
        NORTH(0,-1),
        WEST(-1,0),
        SOUTH(0, 1),
        EAST(1, 0);

        public final int x;
        public final int y;

        public static Direction fromCoords(int x, int y) {
            for(Direction d : values()){
                if(d.x == x && d.y == y){
                    return d;
                }
            }
            return SOUTH;
        };
    };

    public Direction direction;

    public Beam deepCopy() {
        Coordinates c = new Coordinates(position.x, position.y);
        return new Beam(c, direction);
    }
}
