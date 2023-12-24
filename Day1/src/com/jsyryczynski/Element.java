package com.jsyryczynski;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 *
 */
public class Element implements Comparable<Element> {
    public Element(int position, int value, String strValue) {
        this.position = position;
        this.strValue = strValue;
        this.value = value;
    }
    public int position;
    public int value;
    public String strValue;

    public int compareTo(Element o) {
        return Integer.compare(position, o.position);
    }
}
