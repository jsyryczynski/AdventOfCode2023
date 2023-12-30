package com.jsyryczynski;

import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Point {
    public BigDecimal x;
    public BigDecimal y;

    public Point add(Point d) {
        return new Point(x.add(d.x), y.add(d.y));
    }
}
