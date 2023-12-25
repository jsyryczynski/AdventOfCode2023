package com.jsyryczynski;

import com.jsyryczynski.State.Direction;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    public static boolean debug = true;
    public static boolean part2 = true;

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            ArrayList<ArrayList<Integer>> map = new ArrayList<>();

            int xCount = 0;
            int yCount = 0;
            int yIdx = 0;
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                xCount = line.length();
                ArrayList<Integer> lineMap = new ArrayList<>();
                for (int xIdx = 0; xIdx < line.length(); ++xIdx) {
                    lineMap.add(Integer.parseInt(String.valueOf(line.charAt(xIdx))));
                }
                map.add(lineMap);
                ++yIdx;
            }
            yCount = yIdx;

            long result = solve(map, xCount, yCount);
            System.out.println("totalSum " + result);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    private static long solve(ArrayList<ArrayList<Integer>> map, int xCount, int yCount) {

        PriorityQueue<State> queue = new PriorityQueue<>();
        queue.add(new State(new Coordinates(0,0), Direction.NONE, 10, 0, null));

        Coordinates endPosition = new Coordinates(xCount - 1, yCount - 1);
        HashSet<State> visitedStates = new HashSet<>();

        while (!queue.isEmpty()) {
            State currentState = queue.poll();

            // check if reached end
            if (currentState.position.equals(endPosition) && currentState.stepCount > 2) {

                var tmpState = currentState;
                while (tmpState!= null) {
                    Coordinates c = tmpState.position;
                    map.get(c.y).set(c.x, (int) tmpState.direction.c);

                    tmpState = tmpState.prvState;
                }

                for (int yIdx = 0; yIdx < yCount; ++yIdx) {
                    StringBuilder sb = new StringBuilder();
                    var row = map.get(yIdx);
                    for (int xdx = 0; xdx < xCount; ++xdx) {
                        Integer i = row.get(xdx);
                        if (i < 10) {
                            sb.append(i);
                        }
                        else {
                            sb.append((char)i.intValue());
                        }
                    }
                    System.out.println(sb);
                }

                return currentState.cost;
            }

            // check if already visited
            if (!visitedStates.contains(currentState)) {
                visitedStates.add(currentState);
            }
            else {
                // already visited
                continue;
            }

            if (!part2) {
                for (var dir : Direction.getRealValues()) {
                    Coordinates nextCoord = currentState.position.add(dir);
                    int nextStepCount = 0;
                    if (!valid(nextCoord, xCount, yCount)) {
                        // out of board
                        continue;
                    }
                    if (dir == currentState.direction) {
                        if (currentState.stepCount >= 2) {
                            // too long
                            continue;
                        } else {
                            nextStepCount = currentState.stepCount + 1;
                        }
                    }
                    if (dir == currentState.direction.negated()) {
                        continue;
                    }

                    long nextCost = currentState.cost + map.get(nextCoord.y).get(nextCoord.x);
                    queue.add(new State(nextCoord, dir, nextStepCount, nextCost, currentState));
                }
            }
            else {
                if (currentState.stepCount < 3) {
                    int nextStepCount = currentState.stepCount + 1;
                    Direction dir = currentState.direction;
                    Coordinates nextCoord = currentState.position.add(dir);
                    if (!valid(nextCoord, xCount, yCount)) {
                        // out of board
                        continue;
                    }
                    long nextCost = currentState.cost + map.get(nextCoord.y).get(nextCoord.x);
                    queue.add(new State(nextCoord, dir, nextStepCount, nextCost, currentState));
                }
                else {
                    for (var dir : Direction.getRealValues()) {
                        Coordinates nextCoord = currentState.position.add(dir);
                        int nextStepCount = 0;
                        if (!valid(nextCoord, xCount, yCount)) {
                            // out of board
                            continue;
                        }
                        if (dir == currentState.direction) {
                            if (currentState.stepCount >= 9) {
                                // too long
                                continue;
                            } else {
                                nextStepCount = currentState.stepCount + 1;
                            }
                        }
                        if (dir == currentState.direction.negated()) {
                            continue;
                        }

                        long nextCost = currentState.cost + map.get(nextCoord.y).get(nextCoord.x);
                        queue.add(new State(nextCoord, dir, nextStepCount, nextCost, currentState));
                    }
                }
            }
        }

        return -1;
    }

    private static boolean valid(Coordinates nextCoord, int xCount, int yCount) {
        if (nextCoord.x < 0 || nextCoord.x >=xCount || nextCoord.y < 0 || nextCoord.y >= yCount) {
            return false;
        }
        return true;
    }

}
