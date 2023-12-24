package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {

            List<Integer> instructions = scanner.nextLine().chars().mapToObj(c -> (char) c == 'L' ? 0 : 1).collect(
                    Collectors.toList());
            HashMap<String, List<String >> connections = new HashMap<>();
            HashMap<Integer, String> currentNodes = new HashMap<>();

            int idx = 0;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    continue;
                }
                var split = line.split("[\s=(,)]");
                connections.put(split[0], Arrays.asList(split[4], split[6]));
                if (split[0].charAt(2) == 'A') {
                    currentNodes.put(idx, split[0]);
                    ++idx;
                }
            }

            long step = 0;

            HashMap<Integer, Long> lastEndReachedMap = new HashMap<>();
            HashMap<Integer, Long> howLongMap = new HashMap<>();
            HashMap<Integer, List<Long> > allPosMap = new HashMap<>();

            boolean logged = false;
            while (!shouldExit(currentNodes)) {

                if (step >= 100000) {
                    break;
                }
                long result = 1;
                if (howLongMap.size() >= currentNodes.size()) {
                    for (Map.Entry<Integer, Long> entry : howLongMap.entrySet()) {
                        //System.out.println("idx " + entry.getKey() + " diff " + entry.getValue());
                        result = result * entry.getValue();
                    }
                    //break;
                }
                int direction = instructions.get((int) (step % (long) (instructions.size())));
                step++;

                for (Map.Entry<Integer, String> node : currentNodes.entrySet()) {
                    String currentPosition = connections.get(node.getValue()).get(direction);
                    if (currentPosition.charAt(2) == 'Z') {
                        if (!logged) {
                            System.out.println("first strart at step " + step);
                            logged = true;
                        }
                        if (lastEndReachedMap.containsKey(node.getKey())) {
                            var prvCount = lastEndReachedMap.get(node.getKey());
                            howLongMap.put(node.getKey(), step - prvCount);

                        }
                        lastEndReachedMap.put(node.getKey(), step);

                        if (!allPosMap.containsKey(node.getKey())) {
                            allPosMap.put(node.getKey(), new LinkedList<>());
                        }
                        allPosMap.get(node.getKey()).add(step);
                    }
                    node.setValue(currentPosition);
                }
            }
            System.out.println("Steps taken " + step);

            for (var entry : allPosMap.entrySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append("idx " + entry.getKey());
                for (var position : entry.getValue()) {
                    sb.append(" " + position);
                }
                System.out.println(sb);
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    private static boolean shouldExit(HashMap<Integer, String> currentNodes) {
        for (Map.Entry<Integer, String> entry : currentNodes.entrySet()) {
            if (entry.getValue().charAt(2) != 'Z') {
                return false;
            }
        }
        return true;
    }
}
