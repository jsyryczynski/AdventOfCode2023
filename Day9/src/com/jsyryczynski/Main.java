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
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            Long sum = 0L;
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                List <Long> maxLevelData = Arrays.stream(line.split("[\s]")).map(str -> Long.valueOf(str)).collect(
                        Collectors.toList());

                int maxLevel = 0;
                HashMap<Integer, List<Long>> fullMap = new HashMap<>();
                fullMap.put(0, maxLevelData);

                while (Collections.frequency(fullMap.get(maxLevel),0L) != fullMap.get(maxLevel).size()) {
                    var nextLevelData = new ArrayList<Long>();
                    for (int idx = 1; idx <maxLevelData.size(); ++idx) {
                        nextLevelData.add(maxLevelData.get(idx) - maxLevelData.get(idx - 1));
                    }
                    maxLevelData = nextLevelData;

                    maxLevel++;
                    fullMap.put(maxLevel, maxLevelData);
                }


                var level = fullMap.get(maxLevel);
                level.add(0, 0L);
                int addLevel = maxLevel - 1;
                long addedElement = 0L;
                while (addLevel >= 0) {
                    level = fullMap.get(addLevel);
                    var firstElement = level.get(0);
                    var newElement = firstElement - addedElement;
                    level.add(0, newElement);
                    addedElement = newElement;
                    addLevel--;
                }

                sum+= addedElement;
                System.out.println(fullMap.size());
            }
            System.out.println("sum " + sum);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }
}
