package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        HashMap <String, Integer> ballsMap = new HashMap<>();
        ballsMap.put("red", 12);
        ballsMap.put("green", 13);
        ballsMap.put("blue", 14);

        try (Scanner scanner = new Scanner(new File("input.txt"))) {

            int gameIdx = 1;
            int sum = 0;
            long sumPower = 0;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                System.out.println(line);
                var splitLIne = List.of(line.split("[:;]"));
                splitLIne = splitLIne.stream().filter(str -> !str.isEmpty()).collect(Collectors.toList());

                boolean gamePossible = true;
                HashMap <String, Integer> minBallsMap = new HashMap<>();
                minBallsMap.put("red", 0);
                minBallsMap.put("green", 0);
                minBallsMap.put("blue", 0);
                for (int setIdx = 1; setIdx < splitLIne.size(); ++setIdx) {
                    var setStr = splitLIne.get(setIdx);
                    var splitSet =  List.of(setStr.split("[\\s,]"));
                    splitSet = splitSet.stream().filter(str -> !str.isEmpty()).collect(Collectors.toList());
                    for (int ballIdx = 0; ballIdx < splitSet.size() / 2; ++ballIdx) {
                        var ballColor = splitSet.get(ballIdx * 2 + 1);
                        var ballNum = Integer.parseInt(splitSet.get(ballIdx * 2));
                        System.out.println("ballNum " + ballNum);
                        System.out.println("ballColor " + ballColor);

                        if (!ballsMap.containsKey(ballColor) || (ballsMap.get(ballColor) < ballNum)) {
                            gamePossible = false;
                        }

                        if (minBallsMap.get(ballColor) < ballNum) {
                            minBallsMap.put(ballColor, ballNum);
                        }
                    }
                }
                if (gamePossible) {
                    sum += gameIdx;
                }
                long power = minBallsMap.get("red") * minBallsMap.get("blue") * minBallsMap.get("green");
                sumPower += power;
                ++gameIdx;
            }
            System.out.println("Sum "  + sum);
            System.out.println("sumPower "  + sumPower);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

}
