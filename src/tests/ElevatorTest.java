package tests;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import elevator.Elevator;
import elevator.Person;
import exceptions.ElevatorFullException;
import exceptions.InvalidInputException;

public class ElevatorTest {

	@Test
	public void testElevator() {
		try {
			Elevator a = new Elevator(-1,1000,1000,1,10);
			fail();			
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Elevator a = new Elevator(1,-1000,1000,1,10);
			fail();			
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Elevator a = new Elevator(1,1000,-1000,1,10);
			fail();			
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		try {
			Elevator a = new Elevator(1,1000,1000,-1,10);
			fail();			
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Elevator a = new Elevator(1,1000,1000,1,-10);
			fail();			
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Elevator a = new Elevator(1,1000,1000,1,10);//failed because of default floor		
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			fail();	
			e.printStackTrace();
		}
	}

	@Test
	public void testAddRider() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveRider() {
		fail("Not yet implemented");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetElevatorID() {
		try {
			Elevator a = new Elevator(1,1000,1000,1,10);
			Assert.assertEquals(1, a.getElevatorID());
			Elevator b = new Elevator(2,1000,1000,1,10);
			Assert.assertEquals(2, b.getElevatorID());
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetMaximumCapacity() {
		try {
			Elevator a = new Elevator(1,1000,1000,1,10);
			Assert.assertEquals(10, a.getMaximumCapacity());
			Elevator b = new Elevator(2,1000,1000,1,15);
			Assert.assertEquals(15, b.getMaximumCapacity());
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetDefaultFloor() {
		try {
			Elevator a = new Elevator(1,1000,1000,1,10);
			Assert.assertEquals(1, a.getDefaultFloor());
			Elevator b = new Elevator(2,1000,1000,3,15);
			Assert.assertEquals(3, b.getDefaultFloor());
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSetDefaultFloor() {
		try {
			Elevator a = new Elevator(1,1000,1000,-1,10);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetDoorStatus() {
		Elevator a;
		try {
			a = new Elevator(1,1000,1000,1,10);
			a.setDoorStatus(true);
			Assert.assertEquals(true, a.getDoorStatus());
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSetDoorStatus() {
		Elevator a;
		try {
			a = new Elevator(1,1000,1000,1,10);
			a.setDoorStatus(true);
			Assert.assertEquals(true, a.getDoorStatus());
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void testPushButton() {
		
		try {
			Elevator a = new Elevator(1,1000,1000,1,10);
			a.pushButton(12);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Elevator a = new Elevator(1,1000,1000,1,10);
			a.pushButton(-10);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
