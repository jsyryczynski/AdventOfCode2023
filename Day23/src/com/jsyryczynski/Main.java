package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static boolean debug = false;
    public static boolean part2 = true;

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {

            ArrayList<ArrayList<MapElement>> map = new ArrayList<>();

            int rowIdx = 0;
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                ArrayList<MapElement> row = line.chars().mapToObj(c -> (char) c).map(c -> MapElement.fromChar(c)).collect(Collectors.toCollection(ArrayList::new));
                map.add(row);
                rowIdx++;
            }

            int sizeY = rowIdx;
            int sizeX = map.get(0).size();



            Position startPos = null;
            Position endPos = null;



            for (int xIdx = 0; xIdx < sizeX; ++xIdx) {
                if (map.get(0).get(xIdx).equals(MapElement.EMPTY)) {
                    startPos = new Position(xIdx, 0);
                    break;
                }
            }

            for (int xIdx = 0; xIdx < sizeX; ++xIdx) {
                if (map.get(sizeY - 1).get(xIdx).equals(MapElement.EMPTY)) {
                    endPos = new Position(xIdx, sizeY - 1);
                    break;
                }
            }



            Deque<State> positionList = new LinkedList<>();
            HashSet<Position> visited = new HashSet<>();

            printMap(map, visited, sizeX, sizeY);

            positionList.add(new State(startPos, 0, new HashSet<>()));


            Position[] possibleMoves = new Position[]{new Position(1,0), new Position(-1,0), new Position(0, -1), new Position(0,1)};

            System.out.println("Start pos " + startPos);
            System.out.println("End pos " + endPos);

            long longestStep = -1;
            long lastStep = 0;
            while (!positionList.isEmpty()) {
                State currentState = positionList.pollLast();

                if (currentState.step > lastStep) {
                    lastStep = currentState.step;
                    System.out.println("Step " + lastStep);
                }

                if (debug) {
                    System.out.println("Position " + currentState.position + " step " + currentState.step);
                }




                ArrayList<Position> validNextMoves = new ArrayList<>();

                do {
                    if (currentState.position.equals(endPos)) {
                        if (longestStep < currentState.step) {
                            longestStep = currentState.step;
                        }
                        if (debug) {
                            System.out.println("  End reached " + currentState.step);
                        }
                        break;
                    }

                    currentState.visited.add(currentState.position);
                    currentState.step++;
                    validNextMoves.clear();

                    for (Position move : possibleMoves) {
                        Position newPos = currentState.position.add(move);
                        if (isValidMove(map, move, newPos) && !currentState.visited.contains(newPos)) {
                            validNextMoves.add(newPos);
                        }
                    }

                    if (validNextMoves.size() == 1) {
                        var newPos = validNextMoves.get(0);
                        currentState.position = newPos;
                        if (debug) {
                            System.out.println("  Position " + currentState.position + " step " + currentState.step);
                        }

                        continue;
                    }
                    else {
                        break;
                    }
                } while (validNextMoves.size() == 1);

                if (debug) {
                    System.out.println("Position " + currentState.position + " step " + currentState.step);
                    printMap(map, currentState.visited, sizeX, sizeY);
                }


                for (Position move : validNextMoves) {
                    positionList.add(new State(move, currentState.step, new HashSet<>(currentState.visited)));
                }
            }
            System.out.println("longest path " + longestStep);
        }
    }

    private static void printMap(ArrayList<ArrayList<MapElement>> map, HashSet<Position> visited, int sizeX, int sizeY) {

        for (int yIdx = 0; yIdx < sizeY; ++yIdx) {
            StringBuilder sb = new StringBuilder();
            for (int xIdx = 0; xIdx < sizeX; ++xIdx) {
                if (visited.contains(new Position(xIdx, yIdx))) {
                    sb.append('O');
                    continue;
                }
                var me = map.get(yIdx).get(xIdx);
                sb.append(me.character);
            }
            System.out.println(sb);
        }
    }

    private static boolean isValidMove(ArrayList<ArrayList<MapElement>> map, Position move, Position newPos) {
        int ySize = map.size();
        int xSize = map.get(0).size();
        if (newPos.x < 0 || newPos.y < 0 || newPos.y >= ySize || newPos.x >= xSize) {
            return false;
        }
        MapElement destElement = map.get(newPos.y).get(newPos.x);
        if (!part2) {
            if (destElement.equals(MapElement.EMPTY)) {
                return true;
            } else if (destElement.equals(MapElement.ROCK)) {
                return false;
            } else if (move.x == destElement.x && move.y == destElement.y) {
                return true;
            } else {
                return false;
            }
        }
        else{
            if (destElement.equals(MapElement.EMPTY)) {
                return true;
            } else if (destElement.equals(MapElement.ROCK)) {
                return false;
            }
            else {
                return true;
            }
        }
    }
}
