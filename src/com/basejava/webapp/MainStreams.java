package com.basejava.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainStreams {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{9, 8}));
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(oddOrEven(Arrays.asList(5, 7, 3, 2, 4)));
        System.out.println(oddOrEven(Arrays.asList(5, 7, 3, 1, 4)));
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values).distinct().sorted().reduce(0, (x, y) -> x * 10 + y);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> result = integers.stream().collect(
                Collectors.partitioningBy(x -> x % 2 == 0));
        return result.get(false).stream().mapToInt(Integer::intValue).sum() % 2 == 0 ? result.get(false) : result.get(true);
    }
}
