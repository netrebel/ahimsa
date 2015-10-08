package com.ahimsa.samples.mix;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Miguel Reyes
 *         Date: 10/6/15
 *         Time: 6:01 PM
 */
public class BubbleSort {

    public static void main(String[] args) {

        int[] numbers = new int[10];

        int counter = 0;
        while (counter < 10) {
            numbers[counter] = (int)(Math.random() * 100);
            counter++;
        }

        for (int number : numbers) {
            System.out.print(number + ", ");
        }

        int outer = 0;
        int inner = 0;
        for (int i = 0; i < numbers.length - 1; i++) {
            outer++;
            for (int j = 1; j < numbers.length - i; j++) {
                inner++;
                if (numbers[j - 1] > numbers[j]) {
                    int temp = numbers[j - 1];
                    numbers[j - 1] = numbers[j];
                    numbers[j] = temp;
                }
            }
        }

        System.out.println("\nSorted: ");
        for (int number : numbers) {
            System.out.print(number + ", ");
        }

        System.out.println("\nouter = " + outer);
        System.out.println("inner = " + inner);

    }

}
