package com.ahimsa.samples.mix;

public class StringUtils {

    public static void main(String[] args) {
        System.out.println(join("-", "hello", "there"));

    }

    public static String join(String separator, String... parts) {
        if (parts == null || parts.length == 0) {
            return "NOK";
        } else if (parts.length == 1) {
            return parts[0];

        } else {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < parts.length; i++) {
                result.append(parts[i]).append(separator);
            }
            String totalString = result.toString();
            return totalString.substring(0, totalString.length() - 1);
        }
    }

    public static void reverseString() {
        String hello = "Here is a long sentence to be reversed";
        System.out.println("Input:  " + hello);

        StringBuilder strBuilder = new StringBuilder();

        char[] array = hello.toCharArray();

        for (int i = array.length - 1; i >= 0; i--) {
            strBuilder.append(array[i]);
        }
        System.out.println("Result: " + strBuilder.toString());

    }

}