package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            List<Long> times = new ArrayList<>();
            List<Long> distances = new ArrayList<>();

            while (scanner.hasNext()) {

                String line = scanner.nextLine();
                var split = line.split("\s");

                if (times.isEmpty()) {
                    times = Arrays.stream(split).skip(1).filter(str -> !str.isEmpty()).map(str -> Long.parseLong(str)).collect(Collectors.toList());
                }
                else {
                    distances = Arrays.stream(split).skip(1).filter(str -> !str.isEmpty()).map(str -> Long.parseLong(str)).collect(Collectors.toList());
                }
            }
            Long mult = 1L;
            for (int idx = 0; idx < times.size(); ++idx) {
                var maxTime = times.get(idx);
                var maxDistance = distances.get(idx);
                Long sum = 0L;
                for (int time = 0; time <= maxTime; ++ time) {
                    var distance = time * (maxTime - time);
                    if (distance > maxDistance) {
                        sum++;
                    }
                }
                mult = mult * sum;
            }
            System.out.println(mult);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }
}
