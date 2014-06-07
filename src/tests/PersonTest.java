package tests;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import elevator.Building;
import elevator.Person;
import exceptions.InvalidInputException;

public class PersonTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testGetInitialFloor() {
		try {
			Person a = new Person(5,10);
			Assert.assertEquals(5, a.getInitialFloor());
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Test
	public void testSetInitialFloor() {
		try {
			Person a = new Person(1,10);
			a.setInitialFloor(-1);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Person a = new Person(1,10);
			a.setInitialFloor(20);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testPerson() {
		Building b = Building.getInstance();
		try {
			Person a = new Person(-1,10);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Person a = new Person(1,b.getNumberOfFloors()+5);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetDestination() {
		try {
			Person a = new Person(3,7);
			Assert.assertEquals(7,a.getDestination());
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetDirection() {
		try {
			Person a = new Person(3,7);
			Assert.assertEquals(1,a.getDirection());
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Person a = new Person(7,3);
			Assert.assertEquals(-1,a.getDirection());
		
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
