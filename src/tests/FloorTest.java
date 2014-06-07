package tests;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import elevator.Floor;
import elevator.Person;
import exceptions.InvalidInputException;

public class FloorTest {

	@Test
	public void testFloor() {
		try {
			Floor a = new Floor(-15);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Floor a = new Floor(-1);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void testAddRider() {
		Person a = null;
		try {
			Floor f = new Floor(5);
			f.addRider(a);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void testRemoveRider() {
		Person a = null;
		try {
			Floor f = new Floor(5);
			f.removeRider(a);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetFloorButtonStatus() {
		try {
			Person p = new Person(3,7);
			Floor f = new Floor(3);
			Assert.assertEquals(1,f.getFloorButtonStatus());
			} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void testRequestElevator() {
		try {
			Floor a = new Floor(7);
			a.requestElevator(0);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Floor a = new Floor(7);
			a.requestElevator(5);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetFloorNumber() {
		try {
			Floor a = new Floor(7);
			Assert.assertEquals(7,a.getFloorNumber());
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
