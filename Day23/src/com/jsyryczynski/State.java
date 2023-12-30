package com.jsyryczynski;

import java.util.HashSet;
import lombok.AllArgsConstructor;

/**
 *
 */
@AllArgsConstructor
public class State {
    public Position position;
    public long step;
    public HashSet<Position> visited;
}
