package com.jsyryczynski;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 */
public class Conjunction extends Module{

    public Conjunction(String name, ArrayList<String> outputs) {
        super(name, outputs);
    }

    private HashMap<String, Boolean> states;

    @Override
    public ArrayList<Pulse> operate(Pulse p) {
        if (states == null) {
            states = new HashMap<>();
            for (var input : inputs) {
                states.put(input, false);
            }
        }

        states.put(p.source, p.high);

        for (var state : states.values()) {
            if (!state) {
                return pulse(true);
            }
        }

        return pulse(false);
    }

    private ArrayList<Pulse> pulse(boolean b) {
        ArrayList out = new ArrayList<>();
        for (var o : outputs) {
            out.add(new Pulse(b, name, o));
        }
        return out;
    }
}
