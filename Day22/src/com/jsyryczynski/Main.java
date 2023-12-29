package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static boolean debug = true;
    public static boolean part2 = true;

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {

            ArrayList<Brick> allBricksMap = new ArrayList<>();

            Position minPos = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            Position maxPos = new Position(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

            Character name = 'A';
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                var values = Arrays.stream(line.split("[,~]")).map(s -> Integer.valueOf(s)).collect(Collectors.toList());
                Position p1 = new Position(values.get(0),values.get(1),values.get(2));
                Position p2 = new Position(values.get(3),values.get(4),values.get(5));

                allBricksMap.add(new Brick(new Position[]{p1, p2}, String.valueOf(name)));
                name++;

                setMinMax(p1, minPos, maxPos);
                setMinMax(p2, minPos, maxPos);

            }
            System.out.println("min " + minPos);
            System.out.println("max " + maxPos);
            System.out.println("Total positions ");

            ArrayList<HashSet<Brick>> mapByLevel = new ArrayList<>();
            for (int level = 0; level <= maxPos.z; ++level) {
                mapByLevel.add(new HashSet<>());
            }
            HashMap<Position, Brick> mapByAllPosition = new HashMap<>();

            fillMaps(allBricksMap, mapByLevel, mapByAllPosition);

            System.out.println("start");
            printStack(mapByAllPosition, minPos, maxPos);

            // fall down
            fallDown(maxPos, mapByLevel, mapByAllPosition, allBricksMap);

            System.out.println("after falling");
            printStack(mapByAllPosition, minPos, maxPos);

            int count = 0;
            int fallenCount = 0;
            for (Brick b : allBricksMap) {
                ArrayList<HashSet<Brick>> tmpMapByLevel = duplicate(mapByLevel, b);
                HashMap<Position, Brick> tmpMapByAllPosition = duplicate(mapByAllPosition, b);
                ArrayList<Brick> tmpAllBricksMap = new ArrayList<>(allBricksMap);

                ArrayList<String> names = fallDown(maxPos, tmpMapByLevel, tmpMapByAllPosition, tmpAllBricksMap);
                fallenCount += names.size();
                if (names.isEmpty()) {
                    if (debug) {
                        System.out.println("Removing brick " + b.name + " will NOT result in fall");
                    }
                    count++;
                }
                else {
                    if (debug) {
                        System.out.println("Removing brick " + b.name + " will result in fall of " + names);
                    }
                }
            }
            System.out.println("part1 result " + count);
            System.out.println("part2 result " + fallenCount);
        }
    }

    private static ArrayList<HashSet<Brick>> duplicate(ArrayList<HashSet<Brick>> mapByLevel, Brick brick) {
        ArrayList<HashSet<Brick>> result = new ArrayList<>();

        for (var oldLevelSet : mapByLevel) {
            HashSet<Brick> newLevelSet = new HashSet<>(oldLevelSet);
            newLevelSet.remove(brick);
            result.add(newLevelSet);
        }

        return result;
    }

    private static HashMap<Position, Brick> duplicate(HashMap<Position, Brick> mapByAllPosition, Brick brick) {
        HashMap<Position, Brick> result = new HashMap<>(mapByAllPosition);
        result.values().removeAll(List.of(brick));
        return result;
    }

    private static ArrayList<String> fallDown(Position maxPos, ArrayList<HashSet<Brick>> mapByLevel,
            HashMap<Position, Brick> mapByAllPosition, ArrayList<Brick> allBricksMap) {
        HashSet<Brick> prvLevelBricks = mapByLevel.get(1);
        int currentLevel = 2;
        ArrayList<String> fallen = new ArrayList<>();
        while (currentLevel <= maxPos.z) {
            HashSet<Brick> currentLevelBricks = new HashSet<>(mapByLevel.get(currentLevel));
            currentLevelBricks.removeAll(prvLevelBricks);

            for (Brick triedBrick :currentLevelBricks) {
                int desiredLevel = currentLevel - 1;

                while (desiredLevel > 0) {
                    int lowering = desiredLevel - currentLevel;
                    Brick tmpBrick = triedBrick.add(new Position(0,0, lowering));
                    if (checkBrickFits(tmpBrick, mapByAllPosition, currentLevel)) {
                        if (desiredLevel == 1) {
                            break;
                        }
                        else {
                            desiredLevel--;
                        }
                    }
                    else {
                        desiredLevel = desiredLevel+1;
                        break;
                    }
                }


                if (desiredLevel < currentLevel) {
                    int lowering = desiredLevel - currentLevel;

                    fallen.add(triedBrick.name);
                    allBricksMap.remove(triedBrick);

                    // remove old pos from maps
                    Position op1 = triedBrick.positions[0];
                    Position op2 = triedBrick.positions[1];

                    Position tmpPos = new Position(op1);
                    Position vector = op1.vector(op2);
                    for (int idx = 0; idx <= op1.distance(op2); ++idx) {
                        mapByAllPosition.remove(tmpPos);
                        mapByLevel.get(tmpPos.z).remove(triedBrick);
                        tmpPos = tmpPos.add(vector);
                    }

                    // add new position to maps
                    Brick tmpBrick = triedBrick.add(new Position(0,0, lowering));

                    allBricksMap.add(tmpBrick);

                    Position np1 = tmpBrick.positions[0];
                    Position np2 = tmpBrick.positions[1];

                    tmpPos = new Position(np1);
                    for (int idx = 0; idx <= np1.distance(np2); ++idx) {
                        mapByAllPosition.put(tmpPos, tmpBrick);
                        mapByLevel.get(tmpPos.z).add(tmpBrick);
                        tmpPos = tmpPos.add(vector);
                    }
                }

            }

            prvLevelBricks = mapByLevel.get(currentLevel);
            currentLevel++;
        }
        return fallen;
    }

    private static void fillMaps(ArrayList<Brick> allBricksMap, ArrayList<HashSet<Brick>> mapByLevel,
            HashMap<Position, Brick> mapByAllPosition) {
        for (Brick b : allBricksMap) {
            Position p1 = b.positions[0];
            Position p2 = b.positions[1];

            Position tmpPos = new Position(p1);
            Position vector = p1.vector(p2);
            for (int idx = 0; idx <= p1.distance(p2); ++idx) {
                mapByAllPosition.put(tmpPos, b);
                mapByLevel.get(tmpPos.z).add(b);
                tmpPos = tmpPos.add(vector);
            }
        }
    }

    private static void printStack(HashMap<Position, Brick> mapByAllPosition, Position minPos, Position maxPos) {

        for (int zPos = maxPos.z; zPos >0; --zPos) {
            StringBuilder sb = new StringBuilder();
            for (int xPos = minPos.x; xPos <= maxPos.x; ++xPos) {
                String xChar = ".";
                for (int yPos = minPos.y; yPos <= maxPos.y; ++yPos) {
                    var testedPos = new Position(xPos, yPos, zPos);
                    String brickName = null;
                    if (mapByAllPosition.containsKey(testedPos)) {
                        brickName = mapByAllPosition.get(testedPos).name;
                    }

                    if (xChar.equals(".") && brickName != null) {
                        xChar = brickName;
                    }
                    else if (!xChar.equals(brickName) && brickName != null) {
                        xChar = "?";
                    }
                }
                sb.append(xChar);
            }
            sb.append(" " + zPos + " ");

            for (int yPos = minPos.y; yPos <= maxPos.y; ++yPos) {
                String xChar = ".";
                for (int xPos = minPos.x; xPos <= maxPos.x; ++xPos) {

                    var testedPos = new Position(xPos, yPos, zPos);
                    String brickName = null;
                    if (mapByAllPosition.containsKey(testedPos)) {
                        brickName = mapByAllPosition.get(testedPos).name;
                    }

                    if (xChar.equals(".") && brickName != null) {
                        xChar = brickName;
                    }
                    else if (!xChar.equals(brickName) && brickName != null) {
                        xChar = "?";
                    }
                }
                sb.append(xChar);
            }

            System.out.println(sb);
        }
        StringBuilder sb = new StringBuilder();
        for (int xPos = minPos.x; xPos <= maxPos.x; ++xPos) {
            sb.append('-');
        }
        sb.append(" " + 0 + " ");
        for (int xPos = minPos.x; xPos <= maxPos.x; ++xPos) {
            sb.append('-');
        }
        System.out.println(sb);

    }

    private static boolean checkBrickFits(Brick brick, HashMap<Position, Brick> mapByAllPosition, int level) {
        Position p1 = brick.positions[0];
        Position p2 = brick.positions[1];

        Position tmpPos = new Position(p1);
        Position vector = p1.vector(p2);
        for (int idx = 0; idx <= p1.distance(p2); ++idx) {

            if (tmpPos.z >= level) {
                continue;
            }
            if (mapByAllPosition.containsKey(tmpPos)){
                return false;
            }
            tmpPos = tmpPos.add(vector);
        }
        return true;
    }

    private static void setMinMax(Position p1, Position minPos, Position maxPos) {
        calc(p1, minPos, p1.x < minPos.x, p1.y < minPos.y, p1.z < minPos.z);
        calc(p1, maxPos, p1.x > maxPos.x, p1.y > maxPos.y, p1.z > maxPos.z);
    }

    private static void calc(Position p1, Position maxPos, boolean b, boolean b2, boolean b3) {
        if (b) {
            maxPos.x = p1.x;
        }
        if (b2) {
            maxPos.y = p1.y;
        }
        if (b3) {
            maxPos.z = p1.z;
        }
    }

}
