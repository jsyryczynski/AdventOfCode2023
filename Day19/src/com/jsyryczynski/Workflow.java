package com.jsyryczynski;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;

/**
 *
 */
@AllArgsConstructor
public class Workflow {
    List<Condition> conditionList;
    HashMap<Character, ArrayList<Integer>> intervalMap;

    interface Condition {
        abstract Optional<String> check(Part p) throws NoSuchFieldException, IllegalAccessException;
    }
}
