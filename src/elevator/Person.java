/**
 * @author Sebastian Demian
 * @author Sergie Zorin
 */

package elevator;
import exceptions.InvalidInputException;

public class Person {
	
	private int personId;
	private int destinationFloor;
	private int initialFloor;
	private int direction;
	private CustomTimer waitTime = new CustomTimer();
	private CustomTimer rideTime = new CustomTimer();
	
	/**
	 * @return the id of this Person
	 */
	public int getPersonId() {
		return personId;
	}
	
	/**
	 * @param id
	 * @return
	 * @throws InvalidInputException
	 */
	private void setPersonId(int id) throws InvalidInputException {
		if (id < 0 ){
			throw new InvalidInputException("Person's ID can not be negative");
		}
		personId = id;
	}
	
	/**
	 * @return the initialFloor
	 */
	public int getInitialFloor() {
		return initialFloor;
	}
	
	/**
	 * @param initialFloor the initialFloor to set
	 * @throws InvalidInputException 
	 */
	public void setInitialFloor(int initial) throws InvalidInputException {
		if (initial < 1 || initial > Building.getInstance().getNumberOfFloors()){
			throw new InvalidInputException("Initial floor needs to be an actual floor number");
		}
		initialFloor = initial;
	}

	/**
	 * Constructor
	 * @param id ID of the Person
	 * @param initial the floor this person is currently located
	 * @param destination the destination floor
	 * @throws InvalidInputException
	 */
	public Person(int id, int initial, int destination) throws InvalidInputException{
		
		if (initial == destination){
			throw new InvalidInputException("Destination can not be the same as current location");
		}
		setPersonId(id);
		setInitialFloor(initial);
		setDestination(destination);
		setDirection();
		
		//add this person to the list of the current floor
		Building.getInstance().getFloor(getInitialFloor()).addRider(this);
		//push call button from the current floor, going in the desired direction
		Building.getInstance().getFloor(getInitialFloor()).requestElevator(direction);
		startWaitTime();
		System.out.println(Building.getInstance().getTime() + "     Person P"+ getPersonId() + " pressed " + getDirectionForPrint() + " on Floor " + getInitialFloor());
	}
	
	public String getDirectionForPrint(){
		if (getDirection() == 1){
			return "UP";
		} else if (getDirection() == -1){
			return "DOWN";
		}
		return "Idle";
	}

	/**
	 * Starts the waiting timer of this person
	 */
	public void startWaitTime(){
		waitTime.start();
	}
	
	/**
	 * Stops the waiting timer of this person
	 */
	public void stopWaitTime(){
		waitTime.stop();
	}
	
	/**
	 * @return the wait time of this person
	 */
	public int getWaitTime(){
		return waitTime.getTime();
	}
	
	/**
	 * Starts the riding timer of this person
	 */
	public void startRideTime(){
		rideTime.start();
	}
	
	
	/**
	 * Stops the riding timer of this person
	 */
	public void stopRideTime(){
		rideTime.stop();
	}
	
	/**
	 * @return the ride time of this person
	 */
	public int getRideTime(){
		return rideTime.getTime();
	}
	
	/**
	 * @return The destination floor of this person
	 */
	public int getDestination() {
		return destinationFloor;
	}

	/**
	 * Sets the destination floor of this person.
	 * @param destination one of the floor numbers of the building
	 * @throws InvalidInputException
	 */

	private void setDestination(int destination) throws InvalidInputException {
		if (destination < 0){
			throw new InvalidInputException("Destination must be an actual floor number");
		}
		destinationFloor = destination;
	}

	/**
	 * Gets the direction of this person.
	 * Possible value:
	 *      0: Idle
	 * 		1: Up
	 * 	   -1: Down
	 * @return direction in which this person is currently traveling
	 */
	public int getDirection() {
		return direction;
	}
	
	/**
	 * Sets the direction of the Person.
	 */
	private void setDirection() {
		if (destinationFloor > initialFloor){
			direction = 1;
		} else {
			direction = -1;
		}
	}
}
