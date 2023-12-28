package com.jsyryczynski;

import java.util.ArrayList;

/**
 *
 */
public class FlipFlop extends Module {

    private boolean state;

    FlipFlop(String name, ArrayList<String> outputs) {
        super(name, outputs);
        state = false;
    }

    @Override
    public ArrayList<Pulse> operate(Pulse p) {
        boolean high = p.high;
        ArrayList<Pulse> output = new ArrayList<>();
        if (high) {
            return output;
        }
        else {
            state = !state;
            for (String s : outputs) {
                output.add(new Pulse(state, name, s));
            }
        }
        return output;
    }
}
