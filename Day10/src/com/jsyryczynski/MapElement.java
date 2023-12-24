package com.jsyryczynski;

import static com.jsyryczynski.MapElement.Directions.EAST;
import static com.jsyryczynski.MapElement.Directions.NORTH;
import static com.jsyryczynski.MapElement.Directions.SOUTH;
import static com.jsyryczynski.MapElement.Directions.WEST;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public enum MapElement {
    E_nort_west('J', Set.of(NORTH, WEST)),
    E_start('S', Set.of(NORTH, EAST)), // manually put
    E_south_east('F', Set.of(SOUTH, EAST)),
    E_south_west('7', Set.of(SOUTH, WEST)),
    E_north_east('L', Set.of(NORTH, EAST)),
    E_horizontal('-', Set.of(WEST, EAST)),
    E_vertical('|', Set.of(NORTH, SOUTH)),
    E_empty('.');


    public Character character;
    public Set<Directions> connections;

    enum Directions {
        NORTH,
        SOUTH,
        WEST,
        EAST
    };

    private static final Map<Character, MapElement> BY_LABEL = new HashMap<>();

    static {
        for (MapElement e: values()) {
            BY_LABEL.put(e.character, e);
        }
    }

    MapElement(Character character, Set<Directions> directions) {
        this.character = character;
        connections = directions;
    }
    MapElement(Character character) {
        this.character = character;
    }

    public static MapElement valueOfLabel(Character label) {
        return BY_LABEL.get(label);
    }
}
