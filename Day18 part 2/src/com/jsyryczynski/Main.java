package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    public static boolean debug = true;
    public static boolean part2 = true;

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            PriorityQueue<Line> lineMap = new PriorityQueue<>();

            long yMin = 10000000L;
            long yMax = -10000000L;
            Coordinates startPos = new Coordinates(0,0);
            Coordinates prvPos = new Coordinates(0,0);
            Coordinates currentPos = new Coordinates(0,0);
            Direction firstDir = null;
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                var split = line.split("[\s()#]");

                String dirChar = split[4].substring(split[4].length() - 1);
                Direction dir = Direction.fromNumber(Integer.parseInt(dirChar));

                if (firstDir == null) {
                    firstDir = dir;
                }
                int length = Integer.parseInt(split[4].substring(0,split[4].length() - 1), 16);
                currentPos = currentPos.add(dir, length);
                if (dir.equals(Direction.SOUTH)) {
                    lineMap.add(new Line(currentPos.x, prvPos.y, currentPos.y));
                }
                else if (dir.equals(Direction.NORTH)) {
                    lineMap.add(new Line(currentPos.x, currentPos.y, prvPos.y));
                }

                prvPos = currentPos;
                if (currentPos.y < yMin) {
                    yMin = currentPos.y;
                }
                if (currentPos.y > yMax) {
                    yMax = currentPos.y;
                }
            }

            long result = calcArea(lineMap, yMin, yMax);
            System.out.println("totalSum " + result);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    private static long calcArea(PriorityQueue<Line> map, long yMin, long yMax) {
        long area = 0L;
        for (long yIdx = yMin; yIdx <= yMax; ++yIdx)
        {
            if (yIdx==1186328) {
                System.out.println("Debug");
            }
            PriorityQueue<Line> lineQueue = new PriorityQueue<>(map);
            long areaAtStartOfRow = area;
            StringBuilder sb = new StringBuilder();

            boolean inside = false;

            long startColumn = -1;
            int upCount = 0;
            int downCount = 0;
            while (!lineQueue.isEmpty()) {
                var line = lineQueue.poll();
                if (yIdx <= line.bottomY && yIdx >= line.topY) {
                    if (yIdx < line.bottomY && yIdx > line.topY) {
                        upCount++;
                        downCount++;

                    } else if (yIdx == line.bottomY) {
                        upCount++;
                    } else if (yIdx == line.topY) {
                        downCount++;
                    }

                    var tmpInside = calcInside(upCount, downCount);
                    if (tmpInside != inside) {
                        if (!tmpInside) {
                            area += line.x - startColumn + 1;
                        } else {
                            startColumn = line.x;
                        }
                    }
                    inside = tmpInside;
                }

            }


            sb.append("" + yIdx + ": " + (area - areaAtStartOfRow));
            System.out.println(sb);
        }
        return area;
    }

    private static boolean calcInside(int upCount, int downCount) {
        if (upCount % 2 == 0 && downCount % 2 == 0) {
            return false;
        }
        else {
            return true;
        }
    }
}
