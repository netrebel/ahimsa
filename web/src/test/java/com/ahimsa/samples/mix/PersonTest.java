package com.ahimsa.samples.mix;

import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class PersonTest {
	
	@Test
	public void testEquals(){
		Person james = new Person(1, "James", new Date(1980,1,1));
		Person same = new Person(1, "James", new Date(1980,1,1));
		Person similar = new Person(1, "Harry", new Date(1980,1,1));
		
		assertTrue(james.equals(same));
		assertTrue(james.hashCode() == same.hashCode());
		
		assertFalse(james.equals(null));
		assertFalse(james.equals(similar));
		assertTrue(james.hashCode() != similar.hashCode());
	}
	
}