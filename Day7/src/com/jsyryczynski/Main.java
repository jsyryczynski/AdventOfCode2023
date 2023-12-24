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
            PriorityQueue<Hand> game = new PriorityQueue<>();
            while (scanner.hasNext()) {

                String line = scanner.nextLine();
                var split = line.split("\s");

                game.add(new Hand(split[0], Long.parseLong(split[1])));
            }

            Long result = 0L;
            Long value = 1L;
            while (!game.isEmpty()) {
                var h = game.poll();
                System.out.println(h.cards + " " + h.bid + " " + value + " " + h.type);
                result += value * h.bid;
                value++;
            }
            System.out.println("result " + result);


            var hand1 = new Hand("KTJJT",765L);
            var hand2 = new Hand("QQQJA",684L);

            var res = hand1.compareTo(hand2);
            System.out.println(res);


        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }
}
