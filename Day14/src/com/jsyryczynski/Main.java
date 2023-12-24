package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static boolean debug = false;
    public static boolean part2 = true;

    enum Direction {
        NORTH,
        WEST,
        SOUTH,
        EAST
    }

    public static void main(String[] args) {

        ArrayList<ArrayList<Character>> map = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }

                var row = line.chars() .mapToObj(c -> (char) c).collect(Collectors
                        .toCollection(ArrayList::new));
                map.add(row);
            }
            long result = calcResult(map);
            System.out.println("totalSum " + result);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    private static long calcResult(ArrayList<ArrayList<Character>> currentState) {
        int rowCount = currentState.size();
        int columnCount = currentState.get(0).size();

        if (!part2) {
            rollNorth(currentState, Direction.NORTH);
        }
        else {
            if (debug) {
                System.out.println("Initial state");
                printMap(currentState);
            }

            long searchState = 1000000000L;
            HashMap<BigInteger, ArrayList<Long>> saveMap = new HashMap<>();
            for (long idx = 0; idx < 1000000000L; ++idx) {
                ArrayList<ArrayList<Character>> savedState = new ArrayList<>();
                if (idx % 1000 == 0 && idx > 0) {
                    System.out.println("idx " + idx);
                    for (var entry : saveMap.entrySet()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(entry.getKey());
                        for (var value : entry.getValue()) {
                            sb.append(" " + value);
                        }

                        System.out.println(sb);
                    }

                    var periodicElements = saveMap.entrySet().stream().filter(e -> e.getValue().size() > 1).collect(Collectors.toSet());

                    var firstPeriodic = periodicElements.stream().findFirst().get();
                    var period = firstPeriodic.getValue().get(1) - firstPeriodic.getValue().get(0);

                    // calc state below 2000 that will have the same result
                    while (searchState > 2000) {
                        searchState -= period;
                    }
                }
                BigInteger currentStateInt = convertToBigInt(currentState);
                if (saveMap.containsKey(currentStateInt)) {
                    var prvList = saveMap.get(currentStateInt);
                    prvList.add(idx);
                }
                else {
                    var list = new ArrayList<Long>();
                    list.add(idx);
                    saveMap.put(currentStateInt, list);
                }

                // current state have the same result as the big one, exit loop to calc result
                if (idx == searchState) {
                    break;
                }

                rollNorth(currentState, Direction.NORTH);
                if (debug) {
                    System.out.println("Roll North");
                    printMap(currentState);
                }
                rollWest(currentState, Direction.WEST);
                if (debug) {
                    System.out.println("Roll West");
                    printMap(currentState);
                }
                rollNorth(currentState, Direction.SOUTH);
                if (debug) {
                    System.out.println("Roll South");
                    printMap(currentState);
                }
                rollWest(currentState, Direction.EAST);
                if (debug) {
                    System.out.println("Roll East");
                    printMap(currentState);
                }
            }
        }

        if (debug) {
            printMap(currentState);
        }

        long count = 0;
        for (int rowIdx = 0; rowIdx < rowCount; ++rowIdx) {
            for (int columnIdx = 0; columnIdx < columnCount; ++columnIdx) {
                if (currentState.get(rowIdx).get(columnIdx) == 'O') {
                    count += rowCount - rowIdx;
                }
            }
        }
        return count;
    }

    private static BigInteger convertToBigInt(ArrayList<ArrayList<Character>> currentState) {
        int rowCount = currentState.size();
        int columnCount = currentState.get(0).size();
        BigInteger hash = BigInteger.valueOf(0L);
        BigInteger one = BigInteger.valueOf(1L);
        for (int rowIdx = 0; rowIdx < rowCount; ++rowIdx) {
            for (int columnIdx = 0; columnIdx < columnCount; ++columnIdx) {
                if (currentState.get(rowIdx).get(columnIdx) == 'O') {
                    hash = hash.add(one);
                }
                hash = hash.shiftLeft(1);
            }
        }
        return hash;
    }

    private static void rollNorth(ArrayList<ArrayList<Character>> inputs, Direction dir) {
        int rowCount = inputs.size();
        int columnCount = inputs.get(0).size();

        IntStream.range(0, columnCount).forEach(columnIdx -> {
            ArrayList<Integer> emptySlots = new ArrayList<>();
            IntStream.range(0, rowCount).map(i-> {
                if (dir == Direction.SOUTH) {
                    return rowCount -i - 1;
                }
                else return i;
            }).forEach(rowIdx -> {
                ArrayList<Character> currentRow = inputs.get(rowIdx);
                Character c = currentRow.get(columnIdx);
                if (c == '.') {
                    emptySlots.add(rowIdx);
                }
                else if (c == '#') {
                    emptySlots.clear();
                }
                else {
                    // rolling rock
                    if (!emptySlots.isEmpty()) {
                        // roll up
                        Integer slot = emptySlots.remove(0);
                        inputs.get(slot).set(columnIdx, 'O');
                        currentRow.set(columnIdx, '.');
                        emptySlots.add(rowIdx);
                    }
                    // if no empty slots, remain in place
                }
            });
        });
    }

    private static void rollWest(ArrayList<ArrayList<Character>> inputs, Direction dir) {
        int rowCount = inputs.size();
        int columnCount = inputs.get(0).size();

        IntStream.range(0, rowCount).forEach(rowIdx -> {
            ArrayList<Integer> emptySlots = new ArrayList<>();
            IntStream.range(0, columnCount).map(i-> {
                if (dir == Direction.EAST) {
                    return columnCount -i - 1;
                }
                else return i;
            }).forEach(columnIdx -> {
                Character c = inputs.get(rowIdx).get(columnIdx);
                if (c == '.') {
                    emptySlots.add(columnIdx);
                }
                else if (c == '#') {
                    emptySlots.clear();
                }
                else {
                    // rolling rock
                    if (!emptySlots.isEmpty()) {
                        // roll up
                        Integer slot = emptySlots.remove(0);
                        inputs.get(rowIdx).set(slot, 'O');
                        inputs.get(rowIdx).set(columnIdx, '.');
                        emptySlots.add(columnIdx);
                    }
                    // if no empty slots, remain in place
                }
            });
        });
    }

    private static void printMap(ArrayList<ArrayList<Character>> inputs) {
        int rowCount = inputs.size();
        int columnCount = inputs.get(0).size();

        for (int rowIdx = 0; rowIdx < rowCount; ++rowIdx) {
            StringBuilder sb = new StringBuilder();
            for (int columnIdx = 0; columnIdx < columnCount; ++columnIdx) {
                sb.append(inputs.get(rowIdx).get(columnIdx));
            }
            System.out.println(sb);
        }
        System.out.println("");
    }
}
