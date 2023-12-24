package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
            long result = 0;
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                var steps =line.split("[,]");

                ArrayList<ArrayList<Lens>> map = new ArrayList<>();

                for (int idx = 0; idx < 256; ++idx) {
                    map.add(new ArrayList<>());
                }

                for (var step : steps) {
                    if (!part2) {
                        int hash = calcHash(step);
                        if (debug) {
                            System.out.println("" + step + " " + hash);
                        }
                        result += hash;
                    }
                    else {
                        var split = step.split("[-=]");
                        var label = split[0];
                        var hash = calcHash(label);

                        if (split.length > 1) {
                            var focalLength = Integer.valueOf(split[1]);
                            // assignment
                            int idx = map.get(hash).indexOf(new Lens(label, focalLength));
                            if (idx >= 0) {
                                // already present
                                map.get(hash).set(idx, new Lens(label, focalLength));
                            }
                            else {
                                map.get(hash).add(new Lens(label, focalLength));
                            }
                        }
                        else {
                            map.get(hash).remove(new Lens(label, 0));
                        }

                        if (debug) {
                            System.out.println("After " + step);
                            for (int boxIdx = 0; boxIdx < 256; ++boxIdx) {
                                var list = map.get(boxIdx);
                                if (list.size() == 0) {
                                    continue;
                                }
                                StringBuilder sb = new StringBuilder();
                                sb.append("Box " + boxIdx + " ");

                                for (int lensIdx = 0; lensIdx < list.size(); ++lensIdx) {
                                    sb.append(" " + list.get(lensIdx));
                                }
                                System.out.println(sb);
                            }
                        }
                    }
                }


                for (int boxIdx = 0; boxIdx < 256; ++boxIdx) {
                    var box = map.get(boxIdx);
                    for (int lensIdx = 0; lensIdx < box.size(); ++lensIdx) {
                        var lens = box.get(lensIdx);
                        result += (boxIdx + 1) * (lensIdx + 1) * lens.focalLength;
                    }
                }

            }
            System.out.println("totalSum " + result);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    private static int calcHash(String step) {
        int value = 0;

        for (int idx = 0; idx < step.length(); ++idx) {
            char c = step.charAt(idx);
            value += c;
            value *= 17;
            value = value % 256;
        }

        return value;
    }

}
