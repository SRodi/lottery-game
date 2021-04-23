package com.srodi.lottery.rules;

import java.util.List;
import java.util.Random;

public class LotteryRules {

    private static final Random random = new Random();
    private static final int MAX_NUMBER_VALUE = 3;

    /**
     * Generate a list of random numbers
     * @return list of numbers
     */
    public static List<Integer> generateRandomNumbers(){
        return List.of(
                random.nextInt(MAX_NUMBER_VALUE),
                random.nextInt(MAX_NUMBER_VALUE),
                random.nextInt(MAX_NUMBER_VALUE)
        );
    }
    /**
     * Checks what the score of the line is
     * @return the score
     */
    public static int generateScore(List<Integer> numbers) {
        // Get the sum of all the numbers in the Line's list
        int sumOfNumbers = numbers
                .stream()
                .mapToInt(Integer::intValue)    // Map to an IntStream upon which we can use math operations
                .sum();                         // Sum all numbers

        if (sumOfNumbers == 2) {
            return 10;
        }

        // Determine equality of numbers values - if true return '5'
        boolean areAllEqual = numbers
                .stream()
                .distinct()     // Get a stream of distinct values
                .limit(2)       // We only need two differing values to know that at least one value is different
                .count() == 1;  // If we have exactly one, all are equal

        if (areAllEqual) {
            return 5;
        }

        // Check if first number is unique
        boolean firstIsUnique = numbers
                .stream()
                .skip(1)        // Skip the first item
                .noneMatch(     // The below predicate will return true if all numbers (after first) are equal to first
                        number -> number.equals(numbers.get(0))
                );

        if (firstIsUnique) {
            return 1;
        }

        return 0;
    }
}
