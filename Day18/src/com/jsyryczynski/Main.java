package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static boolean debug = true;
    public static boolean part2 = false;

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            ArrayList<ArrayList<MapElement>> map = new ArrayList<>();

            int mapSize = 500;
            for (int yIdx = 0; yIdx < 2 * mapSize; ++yIdx) {
                ArrayList<MapElement> row = new ArrayList<>(Collections.nCopies(2 * mapSize, null));
                map.add(row);
            }

            int xMin = mapSize;
            int xMax = mapSize;
            int yMin = mapSize;
            int yMax = mapSize;
            Coordinates startPos = new Coordinates(mapSize,mapSize);
            var start = new MapElement(new ArrayList<>());
            map.get(startPos.y).set(startPos.x, start);

            Coordinates currentPos = new Coordinates(mapSize,mapSize);
            Direction firstDir = null;
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                var split = line.split("[\s]");

                Direction dir = Direction.fromChar(split[0].charAt(0));

                if (firstDir == null) {
                    firstDir = dir;
                }
                int length = Integer.parseInt(split[1]);

                // add current direction to current position
                MapElement mapElement = map.get(currentPos.y).get(currentPos.x);
                if (mapElement != null) {
                    mapElement.directions.add(dir);
                }

                for (int idx = 0; idx < length; ++idx) {
                    currentPos = currentPos.add(dir);
                    MapElement me;
                    if (idx != length - 1) {
                        ArrayList<Direction> tmp = new ArrayList<>();
                        tmp.add(dir);
                        tmp.add(dir.negated());
                        me = new MapElement(tmp);
                    }
                    else {
                        ArrayList<Direction> tmp = new ArrayList<>();
                        tmp.add(dir.negated());
                        me = new MapElement(tmp);
                    }
                    map.get(currentPos.y).set(currentPos.x, me);
                }

                if (currentPos.x < xMin) {
                    xMin = currentPos.x;
                }
                if (currentPos.x > xMax) {
                    xMax = currentPos.x;
                }
                if (currentPos.y < yMin) {
                    yMin = currentPos.y;
                }
                if (currentPos.y > yMax) {
                    yMax = currentPos.y;
                }
            }

            // close the map
            map.get(currentPos.y).get(currentPos.x).directions.add(firstDir);

            if (debug) {
                printMap(map, xMin, xMax, yMin, yMax);
            }
            long result = calcArea(map, xMin, xMax, yMin, yMax);
            System.out.println("totalSum " + result);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    private static void printMap(ArrayList<ArrayList<MapElement>> map, int xMin, int xMax, int yMin, int yMax) {
        for (int yIdx = yMin; yIdx <= yMax; ++yIdx) {
            StringBuilder sb = new StringBuilder();
            for (int xIdx = xMin; xIdx <= xMax; ++xIdx) {
                if (map.get(yIdx).get(xIdx) != null) {
                    sb.append('#');
                }
                else {
                    sb.append('.');
                }
            }
            System.out.println(sb);
        }
    }

    private static long calcArea(ArrayList<ArrayList<MapElement>> map, int xMin, int xMax, int yMin, int yMax) {
        long area = 0L;
        for (int yIdx = yMin; yIdx <= yMax; ++yIdx) {
            long startArea = area;
            StringBuilder sb = new StringBuilder();
            ArrayList<MapElement> row = map.get(yIdx);

            HashMap<Direction, Integer> dirMap = new HashMap<>();
            for (var dir: Direction.getRealValues()) {
                dirMap.put(dir, 0);
            }

            boolean inside = false;

            for (int xIdx = xMin; xIdx <= xMax; ++xIdx) {
                MapElement mapElement = row.get(xIdx);
                if (mapElement != null) {
                    sb.append('#');
                    area++;
                    for (var dir : mapElement.directions) {
                        var value = dirMap.get(dir);
                        dirMap.put(dir,value+1);
                    }
                }
                else {
                    sb.append('.');
                }


                var northCount = dirMap.get(Direction.NORTH);
                var southCount = dirMap.get(Direction.SOUTH);

                if (southCount > 0 && northCount > 0) {
                    inside=!inside;
                    dirMap.put(Direction.NORTH, northCount-1);
                    dirMap.put(Direction.SOUTH, southCount-1);
                }
                else if (inside && mapElement ==null) {
                    area++;
                }
            }

            sb.append(area - startArea);
            System.out.println(sb);
        }
        return area;
    }
}
