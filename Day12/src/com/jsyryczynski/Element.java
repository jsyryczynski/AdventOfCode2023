package com.jsyryczynski;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 *
 */
@AllArgsConstructor
@EqualsAndHashCode
public class Element {
    public int firstCheckIdx;
    public int lastCheckIdx;
    public int firstBrokenIdx;
    public int lastBrokenIdx;
}
