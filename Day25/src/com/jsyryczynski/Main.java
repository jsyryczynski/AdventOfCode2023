package com.jsyryczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;


public class Main {


    public static boolean debug = false;
    public static boolean part2 = false;

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            HashMap<String, HashSet<String>> map = new HashMap<>();

            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                var split = Arrays.stream(line.split("[:\s]")).filter(s -> !s.isEmpty()).collect(Collectors.toList());

                String current = split.get(0);

                if (!map.containsKey(current)) {
                    map.put(current, new HashSet<>());
                }

                for (int nIdx = 1; nIdx < split.size(); ++ nIdx) {
                    String connected = split.get(nIdx);
                    if (!map.containsKey(connected)) {
                        map.put(connected, new HashSet<>());
                    }

                    map.get(connected).add(current);
                    map.get(current).add(connected);
                }
            }

            HashMap<Integer, Integer> countMap = new HashMap<>();
            for (int idx = 0; idx < 1000; ++idx) {
                System.out.println("idx " + idx);
                int count1 = runKarger(map);
                int key = (map.size() - count1) * count1;
                if (countMap.containsKey(key)){
                    var current = countMap.get(key);
                    countMap.put(key, current + 1);
                }
                else {
                    countMap.put(key, 1);
                }
            }
            System.out.println("Result " + countMap);
        }
    }

    private static int runKarger(HashMap<String, HashSet<String>> map) {
        Set<String> verticesSet = new HashSet<>(map.keySet());
        Set<Edge> edgesSet = new HashSet<>();

        for (var entryset : map.entrySet()) {
            var vert1 = entryset.getKey();
            for (var vert2 : entryset.getValue()) {
                edgesSet.add(new Edge(vert1, vert2));
            }
        }

        List<Edge> edgesList = new LinkedList<>(edgesSet);

        while (verticesSet.size() > 2 && edgesList.size() > 1) {
            Edge removedEdge = popRandomElement(edgesList);
            String v1 = removedEdge.vertices[0];
            String v2 = removedEdge.vertices[1];

            verticesSet.remove(v1);
            verticesSet.remove(v2);

            var names1 = Arrays.stream(v1.split("[-]")).collect(Collectors.toCollection(HashSet::new));
            var names2 = Arrays.stream(v2.split("[-]")).collect(Collectors.toCollection(HashSet::new));

            names1.addAll(names2);

            StringBuilder stringBuilder = new StringBuilder();
            int idx = 0;
            for (String name : names1) {
                stringBuilder.append(name);
                if (idx < (names1.size() - 1)) {
                    stringBuilder.append('-');
                }
                ++idx;
            }
            String newVertex = stringBuilder.toString();
            verticesSet.add(newVertex);

            // this will assure the edges are unique after adding
            for (Iterator<Edge> iterator = edgesList.iterator(); iterator.hasNext();) {
                Edge checkedEdge = iterator.next();
                if (checkedEdge.leadsTo(v1) || checkedEdge.leadsTo(v2)) {
                    checkedEdge.replace(v1, newVertex);
                    checkedEdge.replace(v2, newVertex);
                }
            }


            for (var checkedEdge: edgesList) {
                if (checkedEdge.vertices[0].equals(v1) || checkedEdge.vertices[0].equals(v2)) {
                    checkedEdge.vertices[0] = newVertex;
                }
                if (checkedEdge.vertices[1].equals(v1) || checkedEdge.vertices[1].equals(v2)) {
                    checkedEdge.vertices[1] = newVertex;
                }
            }
        }

        int minCount = Integer.MAX_VALUE;
        if (debug) {
            System.out.println("remaing verticesSet");
        }
        for (var vertex : verticesSet) {
            int count = Arrays.stream(vertex.split("[-]")).collect(Collectors.toCollection(HashSet::new)).size();

            if (debug) {
                System.out.println(vertex + " " + count);
            }
            if (count < minCount) {
                minCount = count;
            }
        }

        return minCount;
    }

    private static <E> E popRandomElement(Collection<? extends E> set)
    {
        Random random = new Random();

        // Generate a random number using nextInt
        // method of the Random class.
        int randomNumber = random.nextInt(set.size());
        int currentIndex = 0;
        E randomElement = null;
        for (Iterator<? extends E> iterator = set.iterator(); iterator.hasNext();) {
            randomElement = iterator.next();

            // if current index is equal to random number
            if (currentIndex == randomNumber) {
                iterator.remove();
                return randomElement;
            }

            // increase the current index
            currentIndex++;
        }

        return null;
    }

}
