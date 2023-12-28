package com.jsyryczynski;

import java.util.ArrayList;

/**
 *
 */
public class Broadcast extends Module{

    public Broadcast(String name, ArrayList<String> outputs) {
        super(name, outputs);
    }

    @Override
    public ArrayList<Pulse> operate(Pulse p) {
        boolean high = p.high;
        ArrayList<Pulse> pulses = new ArrayList<>();
        for (String m : outputs) {
            pulses.add(new Pulse(high, name, m));
        }
        return pulses;
    }
}
