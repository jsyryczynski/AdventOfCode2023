package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * For part 2 just created 6 exuations from 3 first points and put them into https://quickmath.com/webMathematica3/quickmath/equations/solve/advanced.jsp
 197869613734967 = x + (a-150)*d
 292946034245705 = y + (b-5)*d
 309220804687650 = z + (c+8)*d
 344503265587754 = x + (a+69)*e
 394181872935272 = y + (b-11)*e
 376338710786779 = z + (c+46)*e
 293577250654200 = x + (a+17)*f
 176398758803665 = y + (b-101)*f
 272206447651388 = z + (c-26)*f

 solution is x+y+z
 */

public class Main {

    public static boolean debug = true;
    public static boolean part2 = false;
    static BigDecimal min = BigDecimal.valueOf(200000000000000L);
    static BigDecimal max = BigDecimal.valueOf(400000000000000L);
    //static BigDecimal min = BigDecimal.valueOf(7L);
    //static BigDecimal max = BigDecimal.valueOf(27L);

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            ArrayList<Line> lineMap = new ArrayList<>();

            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                var split = Arrays.stream(line.split("[,\s@]")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
                Point startPos = new Point(convertToBigDec(split.get(0)), convertToBigDec(split.get(1)));
                Point vector = new Point(convertToBigDec(split.get(3)), convertToBigDec(split.get(4)));
                lineMap.add(new Line(startPos, vector));
            }

            long count = 0;
            for (int lineAIdx = 0; lineAIdx < lineMap.size(); ++lineAIdx) {
                for (int lineBIdx = lineAIdx + 1; lineBIdx < lineMap.size(); ++lineBIdx) {
                    Point intersection = lineIntersection(lineMap.get(lineAIdx), lineMap.get(lineBIdx));

                    if (debug) {
                        System.out.println("Hailstone A: " + lineMap.get(lineAIdx));
                        System.out.println("Hailstone B: " + lineMap.get(lineBIdx));
                        if (intersection == null) {
                            System.out.println("No intersection");
                        }
                        else if (checkInArea(intersection)) {
                            System.out.println("Intersection " + intersection + " inside area");
                        }
                        else {
                            System.out.println("Intersection " + intersection + " outside area");
                        }
                    }
                    if (intersection != null && checkInArea(intersection)) {
                        count++;
                    }
                }
            }

            System.out.println("part1 result " + count);
        }
    }

    private static boolean checkInArea(Point intersection) {
        if (intersection.x.compareTo(min) >= 0 && intersection.x.compareTo(max) <= 0 && intersection.y.compareTo(min) >= 0
                && intersection.y.compareTo(max) <= 0) {
            return true;
        }
        return false;
    }

    private static Point lineIntersection(Line lineA, Line lineB) {
        
        var aStartPoint = lineA.s;
        var bStartPoint = lineB.s;
        
        var aEndPoint = aStartPoint.add(lineA.d);
        var bEndPoint = bStartPoint.add(lineB.d);
        
        var xdiff = new Point(aStartPoint.x.subtract(aEndPoint.x), bStartPoint.x.subtract(bEndPoint.x));
        var ydiff = new Point(aStartPoint.y.subtract(aEndPoint.y), bStartPoint.y.subtract(bEndPoint.y));

        var div = det(xdiff, ydiff);
        if (div.equals(BigDecimal.ZERO)) {
            return null;
        }

        var d = new Point(det(aStartPoint, aEndPoint), det(bStartPoint, bEndPoint));
        var x = det(d, xdiff).divide(div, RoundingMode.HALF_UP);
        var y = det(d, ydiff).divide(div, RoundingMode.HALF_UP);

        var orderA1 = aEndPoint.x.subtract(lineA.s.x).compareTo(BigDecimal.ZERO);
        var orderA2 = x.subtract(lineA.s.x).compareTo(BigDecimal.ZERO);

        if (orderA1 != orderA2) {
            // point in the past
            return null;
        }

        var orderB1 = bEndPoint.x.subtract(lineB.s.x).compareTo(BigDecimal.ZERO);
        var orderB2 = x.subtract(lineB.s.x).compareTo(BigDecimal.ZERO);

        if (orderB1 != orderB2) {
            // point in the past
            return null;
        }

        return new Point(x, y);
    }
    
    private static BigDecimal det(Point a, Point b) {
        return a.x.multiply(b.y).subtract(a.y.multiply(b.x));
    }

    private static BigDecimal convertToBigDec(String input) {
        return BigDecimal.valueOf(Long.valueOf(input));
    }
}
