package elevator;

import java.util.ArrayList;
import java.util.HashMap;

import exceptions.InvalidInputException;

public class Floor {
	
	private final int floorNumber;
	private ArrayList<Person> people = new ArrayList<Person>();
	private HashMap<Integer, int[]> finishedPeople = new HashMap<Integer, int[]>();
	private int upDownButton = 0;
	
	/**
	 * Constructor
	 * @param number
	 * @throws InvalidInputException
	 */
	public Floor(int number) throws InvalidInputException{
		if (number < 0){
			throw new InvalidInputException("Floor number needs to be a number greater than 0");
		}
		floorNumber = number;
	}
	
	/**
	 * Keeps a list of people who reached their destination.
	 * @param p Person, not null
	 * @throws InvalidInputException
	 */
	
	public void addFinishedRider(int personID, int waitTime, int rideTime) throws InvalidInputException{
		if ( personID < 0 || waitTime < 0 || rideTime < 0){
			throw new InvalidInputException("Input can not be negative");
		}
		synchronized (finishedPeople){
			finishedPeople.put(personID, new int[] {waitTime, rideTime});
		}
	}
	
	/**
	 * Adds a person (rider) to this floor.
	 * @param p Person, not null
	 * @throws InvalidInputException
	 */
	
	public void addRider(Person p) throws InvalidInputException{
		if (p == null){
			throw new InvalidInputException("Input can not be null");
		}
		synchronized (people){
			people.add(p);
		}
	}

	
	/**
	 * Removes a person (rider) from this floor.
	 * @param p Person, not null
	 * @throws InvalidInputException
	 */
	
	public void removeRider(Person p) throws InvalidInputException{
		if (p == null){
			throw new InvalidInputException("Input can not be null");
		}
		synchronized (people){
			people.remove(p);
		}
	}
	
	
	/**
	 * Gets the state of the call button at this floor.
	 * 
	 * @return 1 for up, -1 for down, 2 for up+down pressed, 0 for idle
	 */
	public int getFloorButtonStatus(){
		return upDownButton;
	}
	
	/**
	 * Requests an elevator from current floor.
	 * If button is already pressed in the desired direction, the request is ignored.<br>
	 * Options:  1 for up<br>
	 * 			-1 for down<br>
	 * 
	 * @param direction
	 * @throws InvalidInputException
	 */
	public void requestElevator(int direction) throws InvalidInputException{
		if (direction != -1 && direction != 1){
			throw new InvalidInputException("Invalid direction. Accepted values: 1 for up, -1 for down");
		}
		if ((getFloorButtonStatus() == 1 && direction == -1) || (getFloorButtonStatus() == -1 && direction == 1)){
			upDownButton = 2;
		} else if (getFloorButtonStatus() != 2){
			upDownButton = direction;
	}
		//System.out.printf("IN requestElevator, requesting floor number %d and destination %d\n", getFloorNumber(),getFloorButtonStatus());
		ElevatorController.getInstance().addRequest(getFloorNumber(), getFloorButtonStatus());
		
		//after sending the request to the elevator, set the button back to default
		upDownButton = 0;
}

	/**
	 * 
	 * @return ArrayList containing a copy of all persons on this floor
	 */

	public ArrayList<Person> getAllPeople() {
		ArrayList<Person> clone = new ArrayList<Person>(people);
		return clone;
	}
	
	/**
	 * @return ArrayList containing a copy of all persons on this floor who reached their destination
	 */

	public HashMap<Integer, int[]> getAllFinishedPeople() {
		HashMap<Integer, int[]> clone = new HashMap<Integer, int[]>(finishedPeople);
		return clone;
	}
	

	/**
	 * 
	 * @return The number of the current floor.
	 */
	public int getFloorNumber() {
		return floorNumber;
	}
}
