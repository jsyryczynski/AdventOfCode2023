package com.jsyryczynski;

import com.jsyryczynski.Workflow.Condition;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static final char C_X = 'x';
    public static final char C_M = 'm';
    public static final char C_A = 'a';
    public static final char C_S = 's';
    public static final String ACCEPT = "A";
    public static final String REJECT = "R";
    public static boolean debug = true;
    public static boolean part2 = true;

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new File("input.txt"))) {

            boolean readWorkflows = true;

            HashMap<String, Workflow> workflowMap = new HashMap<>();
            ArrayList<Part> parts = new ArrayList<>();


            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isEmpty() && readWorkflows) {
                    readWorkflows = false;
                    continue;
                }
                else if (line.isEmpty()) {
                    break;
                }

                if (readWorkflows){
                    readWorkflow(workflowMap, line);
                }
                else {
                    readPart(parts, line);
                }
            }

            if (!part2) {
                long result = 0;
                for (int partIdx = 0; partIdx < parts.size(); ++partIdx) {
                    Part part = parts.get(partIdx);

                    String destination = getPartDestination(workflowMap, part);
                    if (destination.equals(ACCEPT)) {
                        result += part.a + part.x + part.m + part.s;
                    }
                }

                System.out.println("totalSum " + result);
            }
            else {
                long result = 0;
                HashMap<Character, ArrayList<Integer>> intervalMap = new HashMap<>();
                intervalMap.put(C_X, new ArrayList<>());
                intervalMap.put(C_M, new ArrayList<>());
                intervalMap.put(C_A, new ArrayList<>());
                intervalMap.put(C_S, new ArrayList<>());

                // not included in area
                ArrayList<Integer> cList = intervalMap.get(C_X);
                cList.add(0);
                ArrayList<Integer> mList = intervalMap.get(C_M);
                mList.add(0);
                ArrayList<Integer> aList = intervalMap.get(C_A);
                aList.add(0);
                ArrayList<Integer> sList = intervalMap.get(C_S);
                sList.add(0);

                for (var worflowKV : workflowMap.entrySet()) {
                    cList.addAll(worflowKV.getValue().intervalMap.get(C_X));
                    mList.addAll(worflowKV.getValue().intervalMap.get(C_M));
                    aList.addAll(worflowKV.getValue().intervalMap.get(C_A));
                    sList.addAll(worflowKV.getValue().intervalMap.get(C_S));
                }

                // included in area
                cList.add(4000);
                mList.add(4000);
                aList.add(4000);
                sList.add(4000);

                cList = cList.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
                mList = mList.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
                aList = aList.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
                sList = sList.stream().distinct().collect(Collectors.toCollection(ArrayList::new));

                Collections.sort(cList);
                Collections.sort(mList);
                Collections.sort(aList);
                Collections.sort(sList);

                for (Integer xIdx = 1; xIdx < cList.size(); ++xIdx) {

                    int xStart = cList.get(xIdx-1);
                    int xEnd = cList.get(xIdx);
                    int xDiff = xEnd - xStart;
                    if (debug) {
                        System.out.println("X: " + xStart + " " + xEnd);
                        System.out.println("result so far " + result);
                    }
                    for (Integer mIdx = 1; mIdx < mList.size(); ++mIdx) {
                        int mStart = mList.get(mIdx-1);
                        int mEnd = mList.get(mIdx);
                        int mDiff = mEnd - mStart;
                        for (Integer aIdx = 1; aIdx < aList.size(); ++aIdx) {
                            int aStart = aList.get(aIdx-1);
                            int aEnd = aList.get(aIdx);
                            int aDiff = aEnd - aStart;
                            for (Integer sIdx = 1; sIdx < sList.size(); ++sIdx) {
                                int sStart = sList.get(sIdx-1);
                                int sEnd = sList.get(sIdx);
                                int sDiff = sEnd - sStart;

                                Part testPart = new Part(xEnd, mEnd, aEnd, sEnd);
                                String dest = getPartDestination(workflowMap, testPart);
                                if (dest.equals(ACCEPT)) {

                                    result += (long) xDiff * mDiff * aDiff * sDiff;
                                }
                            }
                        }
                    }
                }
                System.out.println("totalSum " + result);
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static String getPartDestination(HashMap<String, Workflow> workflowMap, Part part) throws NoSuchFieldException, IllegalAccessException {
        String workflowName = "in";
        while (!workflowName.equals(ACCEPT) && !workflowName.equals(REJECT)) {
            var workflow = workflowMap.get(workflowName);
            for (int conditionIdx = 0; conditionIdx < workflow.conditionList.size(); ++conditionIdx) {
                var opt = workflow.conditionList.get(conditionIdx).check(part);
                if (opt.isPresent()) {
                    workflowName = opt.get();
                    break;
                }
            }
        }
        return workflowName;
    }

    private static void readPart(ArrayList<Part> parts, String line) {
        var split = line.split("[{,=}]");
        parts.add(new Part(Integer.parseInt(split[2]), Integer.parseInt(split[4]), Integer.parseInt(split[6]), Integer.parseInt(split[8])));
    }

    private static void readWorkflow(HashMap<String, Workflow> workflowMap, String line) {
        var split = line.split("[{,}]");
        var workflowName = split[0];
        ArrayList<Condition> condList = new ArrayList<>();

        HashMap<Character, ArrayList<Integer>> intervalMap = new HashMap<>();
        intervalMap.put(C_X, new ArrayList<>());
        intervalMap.put(C_M, new ArrayList<>());
        intervalMap.put(C_A, new ArrayList<>());
        intervalMap.put(C_S, new ArrayList<>());

        for (int conditionIdx = 1; conditionIdx < split.length - 1; ++conditionIdx) {
            String condFull = split[conditionIdx];
            var condSplit = condFull.split("[:]");
            var dest = condSplit[condSplit.length-1];
            var param = condSplit[0].substring(0,1);
            var operator = condSplit[0].substring(1,2);
            Integer desiredValue = Integer.parseInt(condSplit[0].substring(2));

            if (operator.equals("<")) {
                intervalMap.get(param.charAt(0)).add(desiredValue - 1);
            }
            else {
                intervalMap.get(param.charAt(0)).add(desiredValue);
            }
            condList.add(input -> {
                Field field = input.getClass().getDeclaredField(param);
                Integer currentValue = (Integer) field.get(input);

                if (operator.equals("<")) {
                    if (currentValue < desiredValue) {
                        return Optional.of(dest);
                    }
                }
                else {
                    if (currentValue > desiredValue) {
                        return Optional.of(dest);
                    }
                }
                return Optional.empty();
            });
        }
        var def = split[split.length - 1];
        condList.add(f -> Optional.of(def));

        workflowMap.put(workflowName, new Workflow(condList, intervalMap));
    }

}
