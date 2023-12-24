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
            PriorityQueue<Range> seeds = new PriorityQueue<>();
            PriorityQueue<MappingElement> maps = new PriorityQueue<>();
            while (scanner.hasNext()) {

                String line = scanner.nextLine();
                if (line.isEmpty() || line.contains("map")) {
                    System.out.println(line);
                    if (seeds.isEmpty() || maps.isEmpty()) {
                        continue;
                    }

                    seeds = mapRanges(seeds, maps);

                    for (Range seed :seeds) {
                        System.out.println(" " + seed.start + " - " + seed.end);
                    }
                }
                else if (line.contains("seeds:")) {
                    var stripped = line.substring(7);
                    var split = Arrays.stream(stripped.split("[\s]")).map(str -> Long.parseLong(str)).collect(Collectors.toList());

                    for (int idx = 0; idx < split.size() / 2; ++idx) {
                        seeds.add(new Range(split.get(idx * 2), split.get(idx * 2 + 1)));
                    }
                }
                else {
                    var split = Arrays.stream(line.split("[\s]")).map(str -> Long.parseLong(str)).collect(Collectors.toList());
                    maps.add(new MappingElement(split.get(1), split.get(2), split.get(0)));
                }
            }

            Long min = seeds.poll().start;
            System.out.println("result " + min);

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    private static PriorityQueue<Range> mapRanges(PriorityQueue<Range> seeds, PriorityQueue<MappingElement> mappings) {
        PriorityQueue<Range> outSeeds = new PriorityQueue<>();

        Range currentSeedsRange = null;
        MappingElement currentMapping = null;

        while (!seeds.isEmpty() || currentSeedsRange != null) {
            if (currentSeedsRange == null && currentMapping == null) {
                // start
                currentSeedsRange = seeds.poll();
                currentMapping = mappings.poll();
            }
            else if (currentSeedsRange == null) {
                currentSeedsRange = seeds.poll();
            }
            else if (currentSeedsRange.end < currentMapping.end) {
                // move range
                // put current range into output without processing
                outSeeds.add(currentSeedsRange);
                currentSeedsRange = seeds.poll();
            }
            else if (!mappings.isEmpty()){
                // move mapping
                currentMapping = mappings.poll();
            }
            else {
                break;
            }

            if (currentSeedsRange.start <= currentMapping.end && currentSeedsRange.end >= currentMapping.start) {
                // there is a common part - check if there is need to divide
                Long start = Math.max(currentSeedsRange.start, currentMapping.start);
                Long end = Math.min(currentSeedsRange.end, currentMapping.end);

                if (start > currentSeedsRange.start) {
                    // there is a part remaing not mapped before - add it to out seeds without processing
                    outSeeds.add(Range.fromBeginEnd(currentSeedsRange.start, start - 1));
                }

                // handle a part of current range that needs mapping
                Long mappedStart = currentMapping.destinationStart + (start - currentMapping.start);
                Long mappedEnd = mappedStart + (end - start);
                outSeeds.add(Range.fromBeginEnd(mappedStart, mappedEnd));

                if (end < currentSeedsRange.end) {
                    // there is a part remaing not mapped after - check it again
                    currentSeedsRange = Range.fromBeginEnd(end + 1, currentSeedsRange.end);
                } else {
                    currentSeedsRange = null;
                }
            }
        }

        // add remaining seeds
        outSeeds.addAll(seeds);
        if (currentSeedsRange != null) {
            outSeeds.add(currentSeedsRange);
        }
        mappings.clear();
        return outSeeds;
    }

}
