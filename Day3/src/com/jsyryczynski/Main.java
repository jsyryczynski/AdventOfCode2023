package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static String digits = "1234567890";
    static String empty = ".";
    static String gear = "*";

    static ArrayList <Integer> uniqueNumbers = new ArrayList<>();
    static HashMap <Element, Integer> numbersMap = new HashMap<>();
    static HashMap <Element, Character> partsMap = new HashMap<>();

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {

            int y = 0;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                StringBuilder currentNumber = new StringBuilder();
                for (int x = 0; x < line.length(); x++) {
                    Character c = line.charAt(x);
                    if (digits.indexOf(c) >= 0 ) {
                        // digit part
                        currentNumber.append(c);
                    }
                    else if (empty.indexOf(c) >= 0 ){
                        // empty
                        finishNumber(currentNumber, x, y);
                    }
                    else if (gear.indexOf(c) >= 0)
                    {
                        // possible gear
                        finishNumber(currentNumber, x, y);
                        partsMap.put(new Element(x, y), c);
                    }
                    else {
                        // dont care
                        finishNumber(currentNumber, x, y);
                    }
                }
                finishNumber(currentNumber, line.length() -1, y);
                y++;
            }

            Long sum = 0L;
            for(Map.Entry<Element, Character> entry : partsMap.entrySet()) {
                var part = entry.getKey();

                HashSet<Integer> numbersToAdd = new HashSet<>();

                markNumber(numbersToAdd, new Element(part.x - 1, part.y - 1));
                markNumber(numbersToAdd, new Element(part.x - 1, part.y));
                markNumber(numbersToAdd, new Element(part.x - 1, part.y + 1));
                markNumber(numbersToAdd, new Element(part.x, part.y - 1));
                markNumber(numbersToAdd, new Element(part.x, part.y + 1));
                markNumber(numbersToAdd, new Element(part.x + 1, part.y - 1));
                markNumber(numbersToAdd, new Element(part.x + 1, part.y));
                markNumber(numbersToAdd, new Element(part.x + 1, part.y + 1));

                if (numbersToAdd.size() == 2) {
                    Long mult = 1L;
                    for (Integer num : numbersToAdd) {
                        mult *= uniqueNumbers.get(num);
                    }
                    sum += mult;
                }
            }
            System.out.println("sum " + sum);


        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    private static void markNumber(HashSet<Integer> numbersToAdd, Element key) {
        if (numbersMap.containsKey(key)) {
            Integer numIdx = numbersMap.get(key);
            numbersToAdd.add(numIdx);
        }
    }

    private static void finishNumber(StringBuilder currentNumber, int x, int y) {
        if (!currentNumber.isEmpty()) {
            String strNum = currentNumber.toString();
            Integer num = Integer.parseInt(strNum);
            uniqueNumbers.add(num);
            int numberIdx = uniqueNumbers.size() - 1;
            int beginX = x - strNum.length();
            for (int digit = 0; digit < strNum.length(); ++digit) {
                numbersMap.put(new Element(beginX + digit, y), numberIdx);
            }
            currentNumber.setLength(0);
        }
    }

}
