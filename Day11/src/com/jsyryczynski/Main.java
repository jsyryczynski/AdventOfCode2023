package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {

            Coordinates currentPos = new Coordinates(0,0);
            ArrayList<String> unexpandedMap = new ArrayList<>();

            // read & count expand rows
            HashSet<Integer> rowsToExpand = new HashSet<>();
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    continue;
                }
                ArrayList<Character> currentRow = new ArrayList<>();
                unexpandedMap.add(line);

                if (line.indexOf('#') < 0) {
                    rowsToExpand.add((int) currentPos.y);
                }
                for (int i = 0; i < line.length(); i++) {
                    currentPos.x = i;

                    Character c = line.charAt(i);
                    currentRow.add(c);
                }
                currentPos.y += 1;
            }

            // count columns to expand
            HashSet<Integer> columnsToExpand = new HashSet<>();
            for (int xIdx = 0; xIdx < unexpandedMap.get(0).length(); ++xIdx) {
                boolean containsGalaxy = false;
                for (int yIdx = 0; yIdx < unexpandedMap.size(); ++yIdx) {
                    String line = unexpandedMap.get(yIdx);
                    if (line.charAt(xIdx) == '#') {
                        containsGalaxy = true;
                        break;
                    }
                }
                if (!containsGalaxy) {
                    columnsToExpand.add(xIdx);
                }
            }

            // expand rows & columns & convert to just galaxies
            int galaxyIdx = 1;

            HashMap<Integer, Coordinates> galaxyMap = new HashMap<>();
            for (int yIdx = 0; yIdx < unexpandedMap.size(); ++yIdx) {
                for (int xIdx = 0; xIdx < unexpandedMap.get(0).length(); ++xIdx) {
                    String line = unexpandedMap.get(yIdx);
                    if (line.charAt(xIdx) == '#') {
                        int finalXIdx = xIdx;
                        int finalYIdx = yIdx;
                        long expandedColumnsBefore = (1000000L - 1L) * columnsToExpand.stream().filter(elem -> elem < finalXIdx).count();
                        long expandedRowsBefore = (1000000L - 1L) * rowsToExpand.stream().filter(elem -> elem < finalYIdx).count();
                        galaxyMap.put(galaxyIdx, new Coordinates(xIdx + expandedColumnsBefore, yIdx + expandedRowsBefore));
                        galaxyIdx++;
                    }
                }
            }

            int galaxyCount = galaxyIdx;
            long sum = 0;
            // count the distances between
            for (int galaxyIdxA = 1; galaxyIdxA < galaxyCount; galaxyIdxA++) {
                for (int galaxyIdxB = galaxyIdxA + 1; galaxyIdxB < galaxyCount; galaxyIdxB++) {
                    long distance = Math.abs(galaxyMap.get(galaxyIdxA).x - galaxyMap.get(galaxyIdxB).x)
                            + Math.abs(galaxyMap.get(galaxyIdxA).y - galaxyMap.get(galaxyIdxB).y);
                    //System.out.println("distance " + galaxyIdxA + "-" + galaxyIdxB + ": " + distance );
                    sum += distance;
                    System.out.println("sum " + sum);
                }
            }

            System.out.println("final sum " + sum);

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }
}
