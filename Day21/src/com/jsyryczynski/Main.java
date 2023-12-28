package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static boolean debug = false;
    public static boolean part2 = true;

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {

            ArrayList<ArrayList<Boolean>> map = new ArrayList<>();
            Position startPos = null;
            int xSize = 0;

            int yIdx = 0;
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                ArrayList<Boolean> row = new ArrayList<>();
                for (int xIdx = 0; xIdx<line.length(); ++xIdx) {
                    if (line.charAt(xIdx)=='.') {
                        row.add(false);
                    }
                    else if (line.charAt(xIdx)=='#') {
                        row.add(true);
                    }
                    else if (line.charAt(xIdx)=='S') {
                        row.add(false);
                        startPos = new Position(xIdx, yIdx);
                    }
                }
                xSize = line.length();
                map.add(row);
                yIdx++;
            }
            int ySize = yIdx;

            HashSet<Position> currentPositions = new HashSet<>();
            currentPositions.add(startPos);

            long stepsCount = 64;
            if (part2) {
                stepsCount = 400;
            }
            for (long stepIdx = 0; stepIdx < stepsCount; ++stepIdx) {
                System.out.println("Step " + stepIdx + " positions " + currentPositions.size());
                HashSet<Position> unverifiedPositions = new HashSet<>();

                for (Position p : currentPositions) {
                    for (Direction d : Direction.getRealValues()) {
                        unverifiedPositions.add(p.add(d));
                    }
                }

                HashSet<Position> verifiedPositions = new HashSet<>();
                for (Position p : unverifiedPositions) {
                    if (validatePos(p, map, xSize, ySize)) {
                        verifiedPositions.add(p);
                    }
                }

                if (debug && !part2) {
                    printMap(map, verifiedPositions, xSize, ySize);
                }
                currentPositions = verifiedPositions;
            }

            if (!part2) {
                System.out.println("Total positions " + currentPositions.size());
            }
            else {
                /**
                 * calculated in wolfram alpfa using quadratic fit from steps at positions
                 * 65 - 3755
                 * 196 - 33494
                 * 327 - 92811
                 * input to wolfram:
                 * {0, 1, 2}
                 * {3755, 33494, 92811}
                 */


                long a0 = 3755;
                long a1 = 14950;
                long a2 = 14789;

                long x = 26501365 / 131;
                long result = a2 * x * x + a1 * x + a0;
                System.out.println("Total positions " + result);
            }
        }
    }

    private static void printMap(ArrayList<ArrayList<Boolean>> map, HashSet<Position> nextPositions, int xSize, int ySize) {
        for (int yIdx = 0; yIdx < ySize; ++yIdx) {
            StringBuilder sb = new StringBuilder();
            for (int xIdx = 0; xIdx < xSize; ++xIdx) {
                if (map.get(yIdx).get(xIdx)) {
                    sb.append('#');
                }
                else if (nextPositions.contains(new Position(xIdx, yIdx))) {
                    sb.append('O');
                }
                else {
                    sb.append('.');
                }
            }
            System.out.println(sb);
        }
        System.out.println("");
    }

    private static boolean validatePos(Position nextPos, ArrayList<ArrayList<Boolean>> map, int xSize, int ySize) {
        if (!part2) {
            if (nextPos.x < 0 || nextPos.y < 0 || nextPos.x >= xSize || nextPos.y >= ySize) {
                return false;
            }
            if (map.get(nextPos.y).get(nextPos.x)) {
                return false;
            }
        }
        else {
            int x = ((nextPos.x % xSize) + xSize) % xSize;
            int y = ((nextPos.y % ySize) + ySize) % ySize;
            if (map.get(y).get(x)) {
                return false;
            }
        }
        return true;
    }

}
