package com.jsyryczynski;

import lombok.EqualsAndHashCode;

/**
 *
 */
@EqualsAndHashCode
public class Element {
    public Integer x;
    public Integer y;
    Element(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
}
