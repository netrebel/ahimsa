package com.ahimsa.samples.mix;

import java.util.*;
import java.util.Map.Entry;

public class Test{

	public static void main(String[] args) {
		//Reverse string
		//reverseString();
		//useHashMap();
		iterateMap();
		
		
	}
	
	public static void reverseString() {
		String hello = "Here is a long sentence to be reversed";
		System.out.println("Input:  " + hello);
		
		StringBuilder strBuilder = new StringBuilder();
				
	    char[] array = hello.toCharArray();
		
		for(int i = array.length-1; i >= 0  ; i--){
			strBuilder.append(array[i]);
		}
		System.out.println("Result: " + strBuilder.toString());
		
	}
	
	public static void useHashMap(){
		HashMap hashMap = new HashMap();
		hashMap.put("hello", "there");
		hashMap.put("hello", "world");
		hashMap.put(null, "hi");
		
		List<String> list = new ArrayList();
		list.add("simple");
		list.add(null);
		list.add(null);
		
		System.out.println(hashMap.get(null));
		System.out.println(list.size());
		
	}
	
	public static void iterateMap() {
		HashMap<String, String> map = new HashMap();
		map.put("hello", "there");
		map.put("hola", "ahi");
		
		for(String key : map.keySet()) {
			// System.out.println("Key: " + key + ", value: " + map.get(key));
		}
		
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			// System.out.println("key: " +  key + " value:" + map.get(key) );
		}
		
		Set<Entry<String, String>> entrySet = map.entrySet();
		
		for(Entry entry : entrySet) {
			System.out.println("key: " +  entry.getKey() + " value:" + entry.getValue() );
		}
		
		
	}
}