package com.jsyryczynski;

import java.util.Arrays;

/**
 *
 */
public enum MapElement {
    ROCK('#'),
    EMPTY('.'),
    SLOPE_DOWN('v', 0, 1),
    SLOPE_RIGHT('>', 1, 0),
    SLOPE_UP('^', 0, -1),
    SLOPE_LEFT('<', -1, 0);

    Character character;
    int x;
    int y;

    private MapElement(Character character) {
        this.character = character;
        this.x = 0;
        this.y = 0;
    }

    private MapElement(Character character,int x, int y) {
        this.character = character;
        this.x = x;
        this.y = y;
    }

    public static MapElement fromChar(Character c) {
        return Arrays.stream(values()).filter(a -> c.equals(a.character)).findFirst().get();
    }
}
