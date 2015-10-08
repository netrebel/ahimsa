package com.ahimsa.samples.mix;

import java.util.*;
import java.util.Map.Entry;

public class DataStructures {

    public static void main(String[] args) {
        //Reverse string
        //reverseString();
        useHashMap();
//		iterateMap();
//        linkedListTest();

//        checkAnagram("abba", "bcaa");
    }

    private static void checkAnagram(String anagram, String sample) {

        List<String> list1 = fillListWithArray(anagram.toCharArray());
        List<String> list2 = fillListWithArray(sample.toCharArray());

        Collections.sort(list1);
        printList(list1);

        System.out.println("\n");

        Collections.sort(list2);
        printList(list2);

        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).equals(list2.get(i))) {
                System.out.println("Not an Anagram!");
            }
        }


    }

    private static List<String> fillListWithArray(char[] chars) {
        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            list1.add("" + chars[i]);
        }
        return list1;
    }

    private static void printList(List<String> list1) {
        for (String item : list1) {
            System.out.println("item = " + item);
        }
    }

    private static void linkedListTest() {
        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add("Mr.");
        linkedList.add("Miguel");
        linkedList.add("Angel");
        linkedList.add("Reyes");
        linkedList.add("Aburto");

        Stack<String> stack = new Stack<String>();
        stack.push("Miguel");
        stack.push("Angel");
        stack.push("Reyes");

        System.out.println("size: " + linkedList.size());

        Iterator<String> iterator = linkedList.iterator();
        while (iterator.hasNext()) {
            System.out.println("linkedList = " + iterator.next());
        }

        linkedList.removeLast();
        System.out.println("size: " + linkedList.size());

        for (String item : linkedList) {
            System.out.print(item + " ");
        }
    }

    public static void useHashMap() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("hello", "there");
        hashMap.put("miguel", "angel");
        hashMap.put("aburto", "lastname");
        hashMap.put("c", "test");

        for (String key : hashMap.keySet()) {
            System.out.println("key: " + key);
        }

        Map<String, String> treeMap = new TreeMap<>();
        treeMap.put("hello", "there");
        treeMap.put("miguel", "angel");
        treeMap.put("aburto", "lastname");
        treeMap.put("c", "test");

        for (String key : treeMap.keySet()) {
            System.out.println("treeKey: " + key);
        }


    }

    public static void iterateMap() {
        HashMap<String, String> map = new HashMap();
        map.put("hello", "there");
        map.put("hola", "ahi");

        for (String key : map.keySet()) {
            // System.out.println("Key: " + key + ", value: " + map.get(key));
        }

        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            // System.out.println("key: " +  key + " value:" + map.get(key) );
        }

        Set<Entry<String, String>> entrySet = map.entrySet();

        for (Entry entry : entrySet) {
            System.out.println("key: " + entry.getKey() + " value:" + entry.getValue());
        }


    }
}