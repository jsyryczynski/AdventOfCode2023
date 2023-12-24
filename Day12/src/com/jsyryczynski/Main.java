package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    private static HashMap<Element, Boolean> cache = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        boolean debug = false;
        boolean part2 = true;
        boolean loadPrevious = false;

        ArrayList<Coordinates> prvResults = new ArrayList<>();
        if (loadPrevious) {
            try (Scanner scanner = new Scanner(new File("output.txt"))) {
                while (scanner.hasNext()) {
                    var line = scanner.nextLine();
                    if (line.isEmpty()) {
                        continue;
                    }
                    var arr = Arrays.stream(line.split("[\s]")).filter(str -> !str.isEmpty()).collect(Collectors.toList());
                    prvResults.add(new Coordinates(Integer.parseInt(arr.get(1)), Integer.parseInt(arr.get(3))));
                }
            }
        }

        int lineIdx = -1;
        try (Scanner scanner = new Scanner(new File("input.txt"));
                FileOutputStream fos = new FileOutputStream("output.txt", true);
        ) {
            long totalSum = 0;
            int maxSpacesCount = 0;
            int maxNumbersCount = 0;
            while (scanner.hasNext()) {
                long startTime = System.currentTimeMillis();
                int maxLength = 0;

                cache.clear();
                var line = scanner.nextLine();
                lineIdx++;
                if (line.isEmpty() || lineIdx < prvResults.size() ) {
                    continue;
                }

                if (debug) {
                    System.out.println(line);
                }

                var arr = line.split("[\s,]");
                ArrayList<Character> inputCharacters = arr[0].chars().mapToObj(in -> (char) in).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<Integer> blockLengths = Arrays.stream(arr).skip(1).map(str -> Integer.valueOf(str)).collect(Collectors.toCollection(ArrayList::new));

                for (int len : blockLengths) {
                    if (len > maxLength) {
                        maxLength = len;
                    }
                }

                if (part2) {
                    ArrayList<Character> tmpInputSpaces = new ArrayList<>();
                    ArrayList<Integer> tmpLengthList = new ArrayList<>();
                    for (int idx = 0; idx < 4; ++idx) {
                        tmpInputSpaces.addAll(inputCharacters);
                        tmpInputSpaces.add('?');
                        tmpLengthList.addAll(blockLengths);
                    }
                    tmpInputSpaces.addAll(inputCharacters);
                    tmpLengthList.addAll(blockLengths);
                    inputCharacters = tmpInputSpaces;
                    blockLengths = tmpLengthList;
                }

                long partialSum = 0;
                HashMap<Coordinates, Long> states = new HashMap<>();
                states.put(new Coordinates(0,0), 1L);
                for (int inputIdx = 0; inputIdx < inputCharacters.size(); ++inputIdx) {

                    Character currentChar = inputCharacters.get(inputIdx);
                    HashMap<Coordinates, Long> nextStates = new HashMap<>();
                    if (debug) {
                        StringBuilder sb = new StringBuilder("Current Char " );
                        for (int idx = 0; idx < inputCharacters.size(); ++idx) {
                            sb.append(inputCharacters.get(idx));
                        }

                        System.out.println(sb);
                        sb.setLength(0);
                        sb.append("             " );
                        for (int idx = 0; idx < inputCharacters.size(); ++idx) {
                            if (idx == inputIdx) {
                                sb.append('^');
                            }
                            else {
                                sb.append(' ');
                            }

                        }
                        System.out.println(sb);
                    }

                    for (var entry: states.entrySet()) {

                        var state = entry.getKey();
                        var count = entry.getValue();

                        if (debug) {
                            System.out.println("  Current state " + state + " : " + count);
                        }

                        if ((currentChar.equals('#') || currentChar.equals('?')) &&
                                state.blockIdx < blockLengths.size() &&
                                state.springInBlockIdx < blockLengths.get(state.blockIdx)) {
                            // build new or continue building
                            var newLength = state.springInBlockIdx + 1;

                            Coordinates key = new Coordinates(state.blockIdx, newLength);
                            putToMap(nextStates, key, count);

                            if (debug) {
                                System.out.println("    Continuing existing match - Creating state " + key + " : " + count);
                            }

                        }
                        else if ((currentChar.equals('.') || currentChar.equals('?')) &&
                                state.blockIdx < blockLengths.size() &&
                                state.springInBlockIdx == blockLengths.get(state.blockIdx)) {
                            // one after state - close current
                            var key = new Coordinates(state.blockIdx + 1, 0);
                            putToMap(nextStates, key, count);

                            if (debug) {
                                System.out.println("    Full match - Creating state " + key + " : " + count);
                            }
                            continue;
                        }
                        else if (currentChar.equals('#') &&
                                state.blockIdx < blockLengths.size() &&
                                state.springInBlockIdx == blockLengths.get(state.blockIdx)) {
                            // wrong state after - too long
                            if (debug) {
                                System.out.println("    Interrupted by too long");
                            }
                            continue;
                        }
                        else if (currentChar.equals('.') &&
                                state.blockIdx < blockLengths.size() &&
                                state.springInBlockIdx > 0 &&
                                state.springInBlockIdx < blockLengths.get(state.blockIdx)) {
                            if (debug) {
                                System.out.println("    Interrupted by wrong ending . - Not creating state");
                            }
                            continue;
                        }

                        // add same state only if not progressing
                        if ((currentChar.equals('.') || currentChar.equals('?')) && state.springInBlockIdx == 0) {
                            // try also without starting new
                            Coordinates key = new Coordinates(state.blockIdx, state.springInBlockIdx);
                            putToMap(nextStates, key, count);

                            if (debug) {
                                System.out.println("    Nothing - Creating state " + key + " : " + count);
                            }
                        }
                    }
                    states = nextStates;

                }

                for (var entry: states.entrySet()) {
                    var state = entry.getKey();
                    var count = entry.getValue();

                    if (state.blockIdx == blockLengths.size() - 1 && state.springInBlockIdx >= blockLengths.get(
                            blockLengths.size() - 1)) {
                        partialSum += count;
                    }
                    else if (state.blockIdx >= blockLengths.size()) {
                        partialSum += count;
                    }
                }


                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;

                fos.write(String.format("total %10d time %d\n", partialSum, elapsedTime).getBytes());

                System.out.println(String.format("total %10d time %d", partialSum, elapsedTime));
                totalSum+= partialSum;
            }
            System.out.println("totalSum " + totalSum);
            System.out.println("maxSpacesCount " + maxSpacesCount);
            System.out.println("maxNumbersCount " + maxNumbersCount);

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void putToMap(HashMap<Coordinates, Long> nextStates, Coordinates key, Long count) {
        if (nextStates.containsKey(key)) {
            var current = nextStates.get(key);
            nextStates.put(key, count+ current);
        }
        else {
            nextStates.put(key, count);
        }
    }

}
