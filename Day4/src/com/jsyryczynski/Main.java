package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            Long result = 0L;
            HashMap<Integer, Long> cardsNum = new HashMap<>();
            int cardIdx  = 1;
            int maxCard = 1;
            for (int i = 1; i<=213; ++i) {
                cardsNum.put(i, 1L);
            }
            while (scanner.hasNext()) {
                if (maxCard < cardIdx) {
                    maxCard = cardIdx;
                }
                String line = scanner.nextLine();
                var arr = line.split("[\\|:]");
                Set<Integer> winningNums = Arrays.stream(arr[1].split("\s")).filter(str -> !str.isEmpty()).map(str -> Integer.parseInt(str)).collect(Collectors.toSet());
                Set<Integer> myNums = Arrays.stream(arr[2].split("\s")).filter(str -> !str.isEmpty()).map(str -> Integer.parseInt(str)).collect(Collectors.toSet());
                winningNums.retainAll(myNums);
                var winningCount = winningNums.size();
                var howManyToAdd = cardsNum.get(cardIdx);

                System.out.println("card " + cardIdx + " copies " + howManyToAdd);
                System.out.println("card " + cardIdx + " winningCount " + winningCount);

                for (int addingCardIdx = 1; addingCardIdx <= winningCount; ++addingCardIdx) {
                    var resultingIdx = cardIdx + addingCardIdx;
                    if (cardsNum.containsKey(resultingIdx)) {
                        var currentValue = cardsNum.get(resultingIdx);
                        cardsNum.put(resultingIdx, currentValue + howManyToAdd);
                    }
                }
                ++cardIdx;
            }
            long sum = 0;
            for (int idx = 1; idx <= maxCard; ++idx) {
                sum += cardsNum.get(idx);
            }
            System.out.println("result " + sum);

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

}
