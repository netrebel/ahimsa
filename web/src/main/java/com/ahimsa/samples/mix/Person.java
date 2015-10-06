package com.ahimsa.samples.mix;

import java.util.Date;
import java.lang.Comparable;
import static org.junit.Assert.*;

public class Person implements Comparable<Person> {
	
	public int id;
	public String name;
	public Date dob;
	
	public Person(int id, String name, Date dob){
		this.id = id;
		this.name = name;
		this.dob = dob;
	}
	
	@Override
	public boolean equals(Object other) {
		if(this == other) return true;
		
		if(other == null || this.getClass() != other.getClass()) {
			return false;
		}
		
		Person guest = (Person) other;
		return(
				(this.id == guest.id) &&
				(this.name != null && name.equals(guest.name)) &&
				(this.dob != null && dob.equals(guest.dob)));
	}
	
	@Override
	public int compareTo(Person o){		
		return this.id - o.id;
	}
	
	@Override
	public int hashCode(){
		int result = 0;
		result = 31*result + id;
		result = 31*result + (name !=null ? name.hashCode(): 0);
		result = 31*result + (dob != null ? dob.hashCode():0);
		return result;
	}
	
	public static void main(String[] args){	
		System.out.println("Running test...");
		Person james = new Person(1, "James", new Date(1980,1,1));
		Person same = new Person(1, "James", new Date(1980,1,1));
		Person similar = new Person(1, "Harry", new Date(1980,1,1));
		
		assertTrue(james.equals(same));
		assertTrue(james.hashCode() == same.hashCode());
		
		assertFalse(james.equals(null));
		assertFalse(james.equals(similar));
		assertTrue(james.hashCode() != similar.hashCode());
		System.out.println("james: " + james.hashCode());
		System.out.println("same: " + same.hashCode());
		System.out.println("similar: " + similar.hashCode());
		
	}

}