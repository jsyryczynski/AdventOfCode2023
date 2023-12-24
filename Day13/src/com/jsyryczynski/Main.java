package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static boolean debug = true;
    public static boolean part2 = true;
    private static int debugIdx = 0;

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            long totalSum = 0;
            ArrayList<String> inputs = new ArrayList<>();
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    totalSum += calcResult(inputs);
                    inputs.clear();
                    continue;
                }
                inputs.add(line);
            }
            totalSum += calcResult(inputs);
            System.out.println("totalSum " + totalSum);

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    private static long calcResult(ArrayList<String> inputs) {
        long result = 0;
        long rowLength = inputs.get(0).length();
        ArrayList<Long> values = new ArrayList<>();
        // convert to horizontal list
        for (int rowIdx = 0; rowIdx < inputs.size(); rowIdx++) {
            long currentValue = 0;
            String row = inputs.get(rowIdx);
            for (int columnIdx = 0; columnIdx < rowLength; ++columnIdx) {
                if (row.charAt(columnIdx) == '#') {
                    currentValue += (1 << columnIdx);
                }
            }
            values.add(currentValue);
        }

        int horizontalMirrorIdx = checkMirror(values, rowLength);
        if (debug) {
            System.out.println("Horizontal idx " + horizontalMirrorIdx);
        }
        if (horizontalMirrorIdx > 0) {
            result += 100 * horizontalMirrorIdx;
        }
        values.clear();

        // convert to vertical list
        for (int columnIdx = 0; columnIdx < rowLength; columnIdx++) {
            long currentValue = 0;

            for (int rowIdx = 0; rowIdx < inputs.size(); ++rowIdx) {
                String row = inputs.get(rowIdx);
                if (row.charAt(columnIdx) == '#') {
                    currentValue += (1 << rowIdx);
                }
            }
            values.add(currentValue);
        }



        int verticallMirrorIdx = checkMirror(values, inputs.size());
        if (debug) {

            System.out.println("Vertical idx " + verticallMirrorIdx);
        }
        System.out.println("Input " + debugIdx);
        System.out.println(verticallMirrorIdx + 100 * horizontalMirrorIdx);
        ++debugIdx;

        if (verticallMirrorIdx > 0) {
            result += verticallMirrorIdx;
        }
        return result;
    }

    private static int checkMirror(ArrayList<Long> values, long bits) {
        BigInteger left = BigInteger.valueOf(values.get(0));
        int valuesLeft = 1;
        BigInteger right = BigInteger.valueOf(0);
        int valuesRight = values.size() - 1;
        for (int idx = valuesRight; idx > 0 ; --idx) {
            right = right.shiftLeft((int) bits);
            right = right.add(BigInteger.valueOf(values.get(idx)));
        }

        BigInteger baseMask = BigInteger.valueOf(0);
        for (int idx = 0; idx < bits; ++idx) {
            baseMask = baseMask.shiftLeft(1);
            baseMask = baseMask.add(BigInteger.valueOf(1));
        }

        ArrayList<BigInteger> masks = new ArrayList<>();
        BigInteger mask = baseMask;
        for (int idx = 0; idx < values.size()/2 + 1; ++idx) {
            masks.add(mask);
            mask = mask.shiftLeft((int) bits);
            mask = mask.add(baseMask);
        }

        int idx = 1;
        while (idx < values.size()) {
            int maskIdx = (int) Math.min(idx, values.size() - idx) - 1;

            mask = masks.get(maskIdx);
            var leftMasked = left.and(mask);
            var rightMasked = right.and(mask);

            if (debug) {
                System.out.println("Comparing idx " + idx);
                System.out.println("left");
                String format = "%64s";
                System.out.println(String.format(format, left.toString(2)).replace(' ', '0'));
                System.out.println(String.format(format, mask.toString(2)).replace(' ', '0'));
                System.out.println(String.format(format, leftMasked.toString(2)).replace(' ', '0'));

                System.out.println("\nright");
                System.out.println(String.format(format, right.toString(2)).replace(' ', '0'));
                System.out.println(String.format(format, mask.toString(2)).replace(' ', '0'));
                System.out.println(String.format(format, rightMasked.toString(2)).replace(' ', '0'));
            }

            if (!part2) {
                if (leftMasked.equals(rightMasked)) {
                    return idx;
                }
            }
            else {
                if (leftMasked.xor(rightMasked).bitCount() == 1) {
                    return idx;
                }
            }

            left = left.shiftLeft((int) bits);
            left = left.add(BigInteger.valueOf(values.get(idx)));
            right = right.shiftRight((int) bits);

            ++idx;
        }
        return 0;
    }
}
