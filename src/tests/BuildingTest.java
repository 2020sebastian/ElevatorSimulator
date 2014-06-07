package tests;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import elevator.Building;
import exceptions.InvalidInputException;

public class BuildingTest {

	@Test
	public void testSetNumberOfFloors() {
		try {
			Building a = Building.getInstance();
			a.setNumberOfFloors(0);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Building a = Building.getInstance();
			a.setNumberOfFloors(-1);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetNumberOfFloors() {
		Building a = Building.getInstance();
		try {
			a.setNumberOfFloors(10);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(10, a.getNumberOfFloors());
	}

	@Test
	public void testSetNumberOfElevators() {
		try {
			Building a = Building.getInstance();
			a.setNumberOfElevators(0);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Building a = Building.getInstance();
			a.setNumberOfElevators(-1);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetNumberOfElevators() {
		Building a = Building.getInstance();
		try {
			a.setNumberOfElevators(5);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(5, a.getNumberOfElevators());
	}
	

	@SuppressWarnings("deprecation")
	@Test
	public void testGetFloorTime() {
		Building a = Building.getInstance();
		try {
			a.setFloorTime(1000);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(1000, a.getFloorTime());
	}
	

	@Test
	public void testSetFloorTime() {
		try {
			Building a = Building.getInstance();
			a.setFloorTime(-100);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Building a = Building.getInstance();
			a.setFloorTime(-1);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testSetDoorTime() {
		try {
			Building a = Building.getInstance();
			a.setDoorTime(-2);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Building a = Building.getInstance();
			a.setDoorTime(-1);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetDoorTime() {
		Building a = Building.getInstance();
		try {
			a.setDoorTime(1000);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(1000, a.getDoorTime());
	}
	

	@Test
	public void testGetElevator() {
		Building a = Building.getInstance();
		try {
			a.getElevator(-1);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	

	@Test
	public void testGetFloor() {
		try {
			Building a = Building.getInstance();
			a.getFloor(-1);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Building a = Building.getInstance();
			a.setNumberOfFloors(12);
			a.getFloor(17);
			fail();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

}
