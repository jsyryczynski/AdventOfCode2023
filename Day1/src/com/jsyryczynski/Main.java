package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            var sum = 0l;

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                System.out.println(line);

                line = replaceStringDigits(line);

                int firstDigit = -1;
                int secondDigit = -1;

                for (int i = 0; i < line.length(); i++) {

                    Character c = line.charAt(i);
                    if (Character.isDigit(c)) {
                        int numValue = Character.getNumericValue(c);
                        if (firstDigit == -1) {
                            firstDigit = numValue;
                        }
                        else {
                            secondDigit = numValue;
                        }
                    }
                }
                System.out.println("line  " + (10 * firstDigit + secondDigit));
                if (secondDigit == -1) {
                    secondDigit = firstDigit;
                }
                sum += 10* firstDigit + secondDigit;
            }

            System.out.println("sum  " + sum);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    private static String replaceStringDigits(String line) {
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < line.length(); ++index) {
            Character c = line.charAt(index);
            if (Character.isDigit(c)) {
                int numValue = Character.getNumericValue(c);
                sb.append(numValue);
            }
            else {
                Element e = findFirst(line.substring(index));
                if (e != null && e.position == 0) {
                    sb.append(e.value);
                }
            }
        }

        return sb.toString();
    }

    private static Element findFirst(String line) {
        PriorityQueue<Element> pq = new PriorityQueue<>();
        searchString(line, pq, "one", 1);
        searchString(line, pq, "two", 2);
        searchString(line, pq, "three", 3);
        searchString(line, pq, "four", 4);
        searchString(line, pq, "five", 5);
        searchString(line, pq, "six", 6);
        searchString(line, pq, "seven", 7);
        searchString(line, pq, "eight", 8);
        searchString(line, pq, "nine", 9);
        return pq.poll();
    }

    private static void searchString(String line, PriorityQueue<Element> pq, String digitStr, int value) {
        int index = line.indexOf(digitStr);
        if (index >= 0) {
            Element e = new Element(index, value, digitStr);
            pq.add(e);
        }
    }
}
