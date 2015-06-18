package com.ahimsa.lambdas;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

/**
 * @author Miguel Reyes
 *         Date: 6/18/15
 *         Time: 11:58 AM
 */
public class AgileDeveloperSample {

    public static void main(String[] args) {

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

        numbers.stream()
                .map(AgileDeveloperSample::countFactors)
                .forEach(System.out::println);

    }

    public static long countFactors(int number) {
        IntPredicate isDivisible = index -> number % index == 0;

        return IntStream.rangeClosed(1, number)
                .filter(isDivisible)
                .count();
    }

}
