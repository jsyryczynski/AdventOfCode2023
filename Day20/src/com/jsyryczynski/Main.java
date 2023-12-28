package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static boolean debug = false;
    public static boolean part2 = false;

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {

            HashMap<String, Module> moduleMap = new HashMap<>();
            HashMap<Boolean, Long> pulsesCount = new HashMap<>();
            pulsesCount.put(true, 0L);
            pulsesCount.put(false, 0L);

            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                var splitStr = line.split("[->\s,]");
                ArrayList<String> outputs = Arrays.stream(splitStr).skip(1).filter(f -> !f.isEmpty()).collect(Collectors.toCollection(ArrayList::new));

                Module m = null;
                var typeStr = splitStr[0].substring(0,1);
                if (typeStr.equals("%")) {
                    String name = splitStr[0].substring(1);
                    m = new FlipFlop(name, outputs);
                    moduleMap.put(name, m);
                }
                else if (typeStr.equals("&")) {
                    String name = splitStr[0].substring(1);
                    m = new Conjunction(name, outputs);
                    moduleMap.put(name, m);
                }
                else {
                    String name = splitStr[0];
                    m = new Broadcast(name, outputs);
                    moduleMap.put(name, m);
                }
            }

            for (var keySet : moduleMap.entrySet()) {
                for (String output : keySet.getValue().outputs) {
                    Module outModule = moduleMap.get(output);
                    if (outModule==null) {
                        continue;
                    }
                    outModule.inputs.add(keySet.getKey());
                }
            }

            LinkedList<Pulse> pulseQueue = new LinkedList<>();
            Pulse buttonPulse = new Pulse(false, "button", "broadcaster");
            long maxPress = 1000L;
            if (part2) {
                maxPress = 100000000L;
            }

            HashMap<String, Long> zpMap = new HashMap<>();

            for (long buttonPress = 0; buttonPress < maxPress; buttonPress++) {
                pulseQueue.add(buttonPulse);
                var currentLow = pulsesCount.get(false);
                pulsesCount.put(false, currentLow + 1);

                while(!pulseQueue.isEmpty()) {
                    Pulse currentPulse = pulseQueue.pop();
                    String moduleName = currentPulse.destination;
                    if (!moduleMap.containsKey(moduleName)) {
                        continue;
                    }

                    if (part2 && moduleName.equals("zp") && currentPulse.high) {
                        System.out.println("zp high from " + currentPulse.source + " button " + (buttonPress + 1));
                        zpMap.put(currentPulse.source, buttonPress+1);
                        if (zpMap.size() ==4) {
                            Long result = zpMap.get("sb") * zpMap.get("nd") * zpMap.get("ds")* zpMap.get("hf");
                            System.out.println("Part2 " + result);
                            return;
                        }
                    }

                    ArrayList<Pulse> outPulses = moduleMap.get(moduleName).operate(currentPulse);
                    if (part2) {
                        if (moduleName.equals("rx") && currentPulse.high == false) {
                            System.out.println("Part 2: " + (buttonPress + 1));
                            break;
                        }

                    }

                    if (debug) {
                        for (Pulse pulse : outPulses) {
                            System.out.println(pulse.source + " -" + (pulse.high?"high" : "low") + "->" + pulse.destination);

                        }
                    }

                    pulseQueue.addAll(outPulses);
                    addPulses(outPulses, pulsesCount);
                }
            }

            System.out.println("high " + pulsesCount.get(true)  + " low " + pulsesCount.get(false));
            System.out.println("result " + (pulsesCount.get(false) * pulsesCount.get(true)));
        }
    }

    private static void addPulses(ArrayList<Pulse> pulses,HashMap<Boolean, Long> pulseCount) {
        if (!pulses.isEmpty()) {
            boolean high = pulses.get(0).high;
            var currentCount = pulseCount.get(high);
            pulseCount.put(high, currentCount + pulses.size());
        }
    }

}
