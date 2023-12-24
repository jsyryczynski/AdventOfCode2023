package com.jsyryczynski;

import static com.jsyryczynski.MapElement.Directions.NORTH;
import static com.jsyryczynski.MapElement.Directions.SOUTH;

import com.jsyryczynski.MapElement.Directions;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {

            ArrayList<ArrayList<MapElement>> map = new ArrayList<>();
            Coordinates startPos = null;
            Coordinates currentPos = new Coordinates(0,0);

            while (scanner.hasNext()) {
                var line = scanner.nextLine();

                var currentLine = new ArrayList<MapElement>();

                for (int i = 0; i < line.length(); i++) {
                    currentPos.x = i;

                    var currentElem = MapElement.valueOfLabel(line.charAt(i));

                    if (currentElem.equals(MapElement.E_start)) {
                        startPos = new Coordinates(currentPos.x, currentPos.y);
                    }

                    currentLine.add(currentElem);
                }

                map.add(currentLine);
                currentPos.y += 1;
            }

            // assuming start is in the middle of data - if your input has start at edge, just add another empty line
            MapElement leftElement = map.get(startPos.y).get(startPos.x - 1);
            MapElement topElement = map.get(startPos.y - 1).get(startPos.x);
            MapElement rightElement = map.get(startPos.y).get(startPos.x+1);
            if (leftElement.connections != null && leftElement.connections.contains(Directions.EAST)) {
                currentPos = new Coordinates(startPos.x - 1, startPos.y);
            }
            else if (topElement.connections != null && topElement.connections.contains(SOUTH)) {
                currentPos = new Coordinates(startPos.x, startPos.y - 1);
            }
            else {
                currentPos = new Coordinates(startPos.x + 1, startPos.y);
            }
            // fourth unnecessary, as it will be handled by loop

            HashSet<Coordinates> visitedMap = new HashSet<>();
            visitedMap.add(startPos);

            int totalLength = 0;
            while (true) {
                var element = map.get(currentPos.y).get(currentPos.x);
                List<Coordinates> possibleMoves = new ArrayList<>();

                if (element.connections.contains(Directions.WEST)) {
                    possibleMoves.add(new Coordinates(currentPos.x -1, currentPos.y));
                }
                if (element.connections.contains(Directions.EAST)) {
                    possibleMoves.add(new Coordinates(currentPos.x + 1, currentPos.y));
                }
                if (element.connections.contains(NORTH)) {
                    possibleMoves.add(new Coordinates(currentPos.x, currentPos.y - 1));
                }
                if (element.connections.contains(SOUTH)) {
                    possibleMoves.add(new Coordinates(currentPos.x, currentPos.y + 1));
                }

                visitedMap.add(new Coordinates(currentPos.x, currentPos.y));
                ++totalLength;
                if (possibleMoves.contains(startPos) && totalLength > 1) {
                    // found exit
                    break;
                }



                currentPos = possibleMoves.get(0);
                if (visitedMap.contains(currentPos)) {
                    currentPos = possibleMoves.get(1);
                }

            }


            int sizeX = map.get(0).size();
            int sizeY = map.size();

            int area = 0;
            for (int yIdx = 0; yIdx < sizeY; ++yIdx) {
                StringBuilder sb = new StringBuilder();
                boolean isInside = false;
                HashSet<Directions> directionsMap = new HashSet<>();
                for (int xIdx = 0; xIdx < sizeX; ++xIdx) {
                    boolean visited = visitedMap.contains(new Coordinates(xIdx, yIdx));
                    if (isInside && !visited) {
                        area++;
                        sb.append("I");
                    }
                    else if (visited) {
                        Character c = map.get(yIdx).get(xIdx).character;
                        sb.append(c);

                        for(var dir : MapElement.valueOfLabel(c).connections) {
                            if (directionsMap.contains(dir)) {
                                directionsMap.remove(dir);
                            }
                            else {
                                directionsMap.add(dir);
                            }
                        }

                        if (directionsMap.containsAll(Set.of(NORTH, SOUTH))) {
                            isInside = !isInside;
                            directionsMap.clear();
                        }
                    }
                    else {
                        sb.append("O");
                        directionsMap.clear();
                    }
                }
                System.out.println(sb);
            }

            System.out.println("totalLength " + totalLength);
            System.out.println("maxPathLength " + (totalLength / 2+ 1));
            System.out.println("area " + area);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }
}
