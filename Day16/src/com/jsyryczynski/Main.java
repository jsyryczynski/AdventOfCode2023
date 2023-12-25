package com.jsyryczynski;

import com.jsyryczynski.Beam.Direction;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static boolean debug = true;
    public static boolean part2 = true;

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            HashMap<Coordinates, Character> map = new HashMap<>();

            int xCount = 0;
            int yCount = 0;
            int yIdx = 0;
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                xCount = line.length();
                for (int xIdx = 0; xIdx < line.length(); ++xIdx) {
                    Character c = line.charAt(xIdx);
                    map.put(new Coordinates(xIdx, yIdx), c);
                }

                ++yIdx;
            }
            yCount = yIdx;

            int result = calcResult(map, xCount, yCount);
            System.out.println("totalSum " + result);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    private static int calcResult(HashMap<Coordinates, Character> map, int xCount, int yCount) {

        int result = 0;
        if (!part2) {
            result = calcEnergized(map, new Beam(new Coordinates(0, 0), Direction.EAST));
        }
        else {
            for (int xIdx = 0; xIdx < xCount; ++xIdx) {
                var tmp = calcEnergized(map, new Beam(new Coordinates(xIdx, 0), Direction.SOUTH));
                if (tmp > result) {
                    result = tmp;
                }
                tmp = calcEnergized(map, new Beam(new Coordinates(xIdx, yCount - 1), Direction.NORTH));
                if (tmp > result) {
                    result = tmp;
                }
            }

            for (int yIdx = 0; yIdx < yCount; ++yIdx) {
                var tmp = calcEnergized(map, new Beam(new Coordinates(0, yIdx), Direction.EAST));
                if (tmp > result) {
                    result = tmp;
                }
                tmp = calcEnergized(map, new Beam(new Coordinates(xCount - 1, yIdx), Direction.WEST));
                if (tmp > result) {
                    result = tmp;
                }
            }
        }
        return result;
    }

    private static int calcEnergized(HashMap<Coordinates, Character> map, Beam beam) {
        ArrayList<Beam> beams = new ArrayList<>();
        beams.add(beam);

        HashSet<Coordinates> energizedSet = new HashSet<>();
        HashSet<Beam> prvStates = new HashSet<>();

        while (!beams.isEmpty()) {
            Beam currentBeam = beams.remove(beams.size() - 1);
            while (map.containsKey(currentBeam.position)) {
                Character currentChar = map.get(currentBeam.position);
                energizedSet.add(currentBeam.position);

                if (prvStates.contains(currentBeam)) {
                    break;
                }
                else {
                    prvStates.add(currentBeam.deepCopy());
                }

                Beam newBeam = currentBeam.move(currentChar);
                if (newBeam != null) {
                    beams.add(newBeam);
                }
            }
        }

        var result = energizedSet.size();
        return result;
    }
}
