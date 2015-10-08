package com.ahimsa.samples.mix;

/**
 * @author Miguel Reyes
 *         Date: 10/6/15
 *         Time: 6:26 PM
 */
public class BSortDemo {

    public static void main(String[] args) {
        int i, pass;
        int[] numbers = new int[10];

        int counter = 0;
        while (counter < 10) {
            numbers[counter] = (int) (Math.random() * 100);
            counter++;
        }
        for (int number : numbers) {
            System.out.print(number + ", ");
        }

        int inner = 0;
        for (pass = 0; pass <= numbers.length - 2; pass++) {
            for (i = 0; i <= numbers.length - pass - 2; i++) {
                inner++;
                if (numbers[i] > numbers[i + 1]) {
                    int temp = numbers[i];
                    numbers[i] = numbers[i + 1];
                    numbers[i + 1] = temp;
                }
            }
        }
        System.out.println("\n");
        for (i = 0; i < numbers.length; i++)
            System.out.print(numbers[i] + ", ");

        System.out.println("\npass = " + pass);
        System.out.println("inner = " + inner);
    }
}
