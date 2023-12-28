package com.jsyryczynski;

import java.util.ArrayList;

/**
 *
 */
public abstract class Module {
    public Module(String name, ArrayList<String> outputs) {
        this.name = name;
        this.inputs = new ArrayList<>();
        this.outputs = outputs;
    }

    public String name;
    public ArrayList<String> outputs;
    public ArrayList<String> inputs;
    public abstract ArrayList<Pulse> operate(Pulse p);
}
