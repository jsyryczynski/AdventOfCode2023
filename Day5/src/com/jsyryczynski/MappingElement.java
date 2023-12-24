package com.jsyryczynski;

import lombok.EqualsAndHashCode;

/**
 *
 */
@EqualsAndHashCode
public class MappingElement extends Range {
    public Long destinationStart;
    MappingElement(Long sourceStart, Long length, Long destinationStart) {
        super(sourceStart, length);
        this.destinationStart = destinationStart;
    }
}
