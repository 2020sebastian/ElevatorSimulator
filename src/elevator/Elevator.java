package elevator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

import exceptions.ElevatorFullException;
import exceptions.InvalidInputException;

/**
 * @author Sebastian Demian
 * @author Sergie Zorin
 * @see java.util.ArrayList 
 * @see java.util.Collections
 */

public class Elevator implements ElevatorInterface, Runnable {
	
	private int elevatorID;				
	private int currentFloor = 1;		//defaults to Floor 1
	private int defaultFloor = 1;	
	private int destination;
	private volatile boolean running = true;	//elevator thread running condition
	private boolean doorStatus = false; //false for closed, true for open
	private int direction = 0; 			//0 for idle, -1 for down, 1 for up
	private int floorTime; 				// time between floors
	private int doorTime; 				// time for opening/closing doors
	private int maxCapacity;
	private int timeOut;
	private int nextDirection;
	private CountDownLatch countDown;
	private ArrayList<Person> riders = new ArrayList<Person>(); //people riding in elevator
	private ArrayList<Integer> requests = new ArrayList<Integer>();	//array to hold requests for an elevator
	
	/**
	 * Indicates is the elevator has any requests in the queue.
	 * @return boolean
	 */
	public boolean isRequestsEmpty(){
		if (requests.size() == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the next direction of the elevator
	 * @param next
	 * @throws InvalidInputException
	 */
	public void setNextDirection(int next) throws InvalidInputException{
		if (next < -1 || next > 1){
			throw new InvalidInputException("Invalid next direction");
		}
		nextDirection = next;
	}
	
	/**
	 * 
	 * @return next direction of the elevator
	 */
	public int getNextDirection(){
		return nextDirection;
	}
	
	/**
	 * Gets a copy-list of the current riders in the elevator.
	 * @return clone of the riders in the elevator.
	 */
	public ArrayList<Person> getAllRiders(){
		ArrayList<Person> clone = new ArrayList<Person>(riders);
		return clone;
	}
	
	
	/**
	 * Constructor.
	 * @param id ID of the elevator
	 * @param floorDelay Time between floors
	 * @param doorDelay  Time for opening/closing doors.
	 * @param floor Default floor of this elevator
	 * @param capacity The capacity of the elevator
	 * 
	 * @throws InvalidInputException
	 */
	public Elevator(int id, int floorDelay, int doorDelay, int floor, int capacity, int timeOut, CountDownLatch cdl) throws InvalidInputException{
		setElevatorID(id);
		setFloorDelay(floorDelay);
		setDoorDelay(doorDelay);
		setDefaultFloor(floor);
		setMaximumCapacity(capacity);
		setTimeOut(timeOut);
		setCountDown(cdl);
	}
	
	/**
	 * @return the timeOut
	 */
	public int getTimeOut() {
		return timeOut;
	}


	/**
	 * @param timeOut the timeOut to set
	 * @throws InvalidInputException 
	 */
	public void setTimeOut(int t) throws InvalidInputException {
		if (t < 0){
			throw new InvalidInputException("Time can not be negative");
		}
		timeOut = t;
	}


	private void setCountDown(CountDownLatch cdl){
		countDown = cdl;
	}
	
	
	/**
	 * Sets the delay time for traveling between floors
	 * @param delay
	 * @throws InvalidInputException
	 */
	public void setFloorDelay(int delay) throws InvalidInputException{
		if (delay < 0){
			throw new InvalidInputException("Input can not be negative");
		}
		floorTime = delay;
	}
	
	/**
	 * Sets the delay time for opening doors
	 * @param delay
	 * @throws InvalidInputException
	 */
	public void setDoorDelay(int delay) throws InvalidInputException{
		if (delay < 0){
			throw new InvalidInputException("Input can not be negative");
		}
		doorTime = delay;
	}
	
	/**
	 * Adds a person (rider) to this elevator.
	 * @param p Person, not null
	 * @throws InvalidInputException
	 * @throws ElevatorFullException 
	 */
	public void addRider(Person p) throws InvalidInputException, ElevatorFullException{
		if (p == null){
			throw new InvalidInputException("Input can not be null");
		
		} else if (riders.size() > getMaximumCapacity()){
			throw new ElevatorFullException("Elevator is full");
		}
		riders.add(p);
	}
	
	
	/**
	 * Removes a person (rider) from this elevator.
	 * @param p Person, not null
	 * @throws InvalidInputException
	 */
	public void removeRider(Person p) throws InvalidInputException{
		if (p == null){
			throw new InvalidInputException("Input can not be null");
		}
		riders.remove(p);
	}
	
	
	
	/**
	 * 
	 * Sets the id of the elevator.
	 * The input value has to be between 1 and 100 inclusive.
	 * 
	 * @param id
	 * @throws InvalidInputException
	 */
	private void setElevatorID(int id) throws InvalidInputException{
		if (id < 1 || id > 100){
			throw new InvalidInputException("Elevator id has to be between 1 and 100 inclusive");
		}
		elevatorID = id;
	}
	
	/**
	 * Gets the id of the elevator.
	 * @return elevatorID
	 */
	public int getElevatorID() {
		return elevatorID;
	}
	/**
	 * 
	 * @return maximum capacity of this elevator
	 */
	public int getMaximumCapacity() {
		return maxCapacity;
	}

	/**
	 * Sets the maximum capacity for this elevator.
	 * @param maxCapacity
	 * @throws InvalidInputException
	 */
	public void setMaximumCapacity(int maxCapacity) throws InvalidInputException {
		if (maxCapacity < 0){
			throw new InvalidInputException("Capacity cannot be negative");
		}
		this.maxCapacity = maxCapacity;
	}
	
	/**
	 * Moves elevator from floor to floor upwards
	 * 
	 * @throws InterruptedException
	 * @throws InvalidInputException
	 */
	private void elevatorMoveUp() throws InterruptedException, InvalidInputException{
		//System.out.println("Request in elevator " + requests.get(0));
		for(getCurrentFloor(); requests.get(0) > getCurrentFloor(); currentFloor++){
			Thread.sleep(floorTime);
			System.out.printf(Building.getInstance().getTime() + "     Elevator %d moving from Floor %d to Floor %d\n", 
					getElevatorID(), getCurrentFloor(), getCurrentFloor()+1);
		}
		//System.out.println("Requests before removal moveup"+ requests.toString());
		requests.remove(0);
		//System.out.println("Requests before removal moveup"+ requests.toString());
		openDoor();
		closeDoor();
	}
	
	/**
	 * Moves elevator from floor to floor downwards
	 * 
	 * @throws InterruptedException
	 * @throws InvalidInputException
	 */
	private void elevatorMoveDown() throws InterruptedException, InvalidInputException{
		for(getCurrentFloor(); requests.get(requests.size()-1) < getCurrentFloor();currentFloor--){
			Thread.sleep(floorTime);
			System.out.printf(Building.getInstance().getTime() + "     Elevator %d moving from Floor %d to Floor %d\n",
					getElevatorID(), getCurrentFloor(), getCurrentFloor()-1);			
		}
		//System.out.println("Requests before removal movedown"+ requests.toString());
		requests.remove(requests.size()-1);
		//System.out.println("Requests after removal movedown"+ requests.toString());
		openDoor();
		closeDoor();
	}
	
	
	/**
	 * 
	 * @throws InterruptedException
	 * @throws InvalidInputException
	 */
	public void move() throws InterruptedException, InvalidInputException {
		synchronized (this) {
			//start ride time as soon as move() gets called
            while (!requests.isEmpty()) {	
            	Collections.sort(requests);
            	//System.out.printf(Building.getInstance().getTime() + "     Elevator %d going to Floor %d %s\n", getElevatorID(), requests.get(0), getEachRider());
            	if(requests.get(0) > getCurrentFloor()){
            		setDirection(1);
            		destination = requests.get(0);
            		elevatorMoveUp();	
            	}else if(requests.get(0) < getCurrentFloor()) {
            		setDirection(-1);
            		destination = requests.get(0);
            		elevatorMoveDown();
            	}else if(requests.get(0) == getDefaultFloor() && getDirection() == 0){
            		destination = requests.get(0);
            		requests.remove(0);
            		openDoor();
            		setDirection(0);
            		closeDoor();
            	}
            }
        }
	} 
         		
	/**
	 * The openDoor method for the elevator.
	 * Simulates the opening of the door with time = doorTime<br>
	 * Sets the door status to true<br>
	 * Sets the Direction to 0 (Idle)
	 * 
	 * @throws InvalidInputException 
	 */
	public void openDoor() throws InvalidInputException{
		if (requests.isEmpty()){
			setDirection(0);
		}
		try {
			Thread.sleep(doorTime);
			System.out.printf(Building.getInstance().getTime() + "     Elevator " + elevatorID + " doors open at Floor %d\n", getCurrentFloor());
			//System.out.println("Direction of the elevator is " + getDirection());
			setDoorStatus(true);
			//System.out.printf("Sending id %d to arrived method\n", getElevatorID());
			ElevatorController.getInstance().arrived(getElevatorID());
			
			//if there are no more requests to process
			if (requests.isEmpty()){
				//check pending requests
				ArrayList<Integer> pendingRequests = ElevatorController.getInstance().checkPendingRequests(getCurrentFloor());
				
				//if there are pending requests, add them to the elevator's list of requests
				if (pendingRequests != null){
					for (Integer i: pendingRequests){
						pushButton(i, 1);
					}
					//if there are no pending requests, set the elevator to idle
				} else {
				setDirection(0);
				}
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Simulates the closing of the door with time = doorTime
	 * Sets the door status to true
	 */
	public void closeDoor(){	
		try {
			Thread.sleep(doorTime);
			System.out.printf(Building.getInstance().getTime() + "     Elevator " + elevatorID + " doors closed\n");
			setDoorStatus(false);		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the current floor of the elevator
	 * @return  number of the floor that the Elevator is currently on
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public void elevatorShutDown(boolean command){
		synchronized(requests){
		running = command;
		requests.notifyAll();
		System.out.printf(Building.getInstance().getTime() + "     Elevator %d is OFF\n", getElevatorID());
		}
	}
	
	/**
	 * Gets the default floor of the elevator.
	 * @return  number of default floor that was set
	 */
	public int getDefaultFloor() {
		return defaultFloor;
	}

	/**
	 * Sets the default floor of the elevator.
	 * @param floor number of default floor to be set
	 * @throws InvalidInputException 
	 */
	public void setDefaultFloor(int floor) throws InvalidInputException {
		if (floor < 0 || floor > Building.getInstance().getNumberOfFloors()){
			throw new InvalidInputException("Default floor must be an actual floor of this building.");
		}
		defaultFloor = floor;
	}
	
	/**
	 * Used to check the status of the door.<br>
	 * Door open = True, door closed = False
	 * 
	 * @return Boolean that indicates whether the door is open or closed
	 */
	public boolean getDoorStatus() {
		return doorStatus;
	}
	
	/**
	 * Used to set the status of the door. 
	 * 
	 * @param status Boolean
	 * @return status of the door True = open, False = closed
	 */
	public void setDoorStatus(boolean status) {
		doorStatus = status;
	}
	
	/**
	 * The setDirection method for the elevator.<br>
	 * Used to set the direction of the elevator<br>
	 *      0: Idle<br>
	 * 		1: Up<br>
	 * 	   -1: Down<br>
	 * 
	 *@param direct direction of the elevator
	 *@throws InvalidInputException 	
	 */
	private void setDirection(int direct) throws InvalidInputException{
		if (direct < -1 || direct > 1){
			throw new InvalidInputException("Direction can only be 0 (Idle), 1 (Up) or -1 (Down)");
		}
		direction = direct;
	}
	
	/**
	 * 
	 * @return direction of this elevator
	 */
	public int getDirection(){
		return direction;
	}
	
	/**
	 * Checks if the Elevator is in Idle state.<br>
	 * True = elevator is idle<br>
	 * False = elevator is not idle (it is in moving state)
	 * 
	 * @return boolean Current state of the elevator. 
	 * 
	 */
	public boolean isIdle(){
		if (direction == 0){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @return
	 */
	public int getDestination() {
		return destination;
	}
	
	/**
	 * Pushes a specific button on the button panel inside the elevator. 
	 *  If condition:<br>
	 *  	floorNum > getCurrentFloor() && direction >= 0)<br>
	 *  	floorNum < getCurrentFloor() && direction <= 0)<br>
	 *	is satisfied, proceeds to adding floorNum to an array.<br>
	 *	It then notifies the thread. 
	 * 
	 *  @param floorNum	- the floor to be added to an array
	 *  @param override should be 1 if the input is called from the outside of the elevator
	 *           0 otherwise.
	 * @throws InvalidInputException 
	 */
	
	public void pushButton(int floorNum, int override) throws InvalidInputException {
		
		if (override < 0 || override > 1){
			throw new InvalidInputException("Override can only be 0 or 1");
		}
		if (floorNum < 0 || floorNum > Building.getInstance().getNumberOfFloors()){
			throw new InvalidInputException("floorNum needs to be an actual floor number of this building.");
		}
		

		//if requested floor if not current floor and is not already a destination
		//System.out.println("Requests contains " + requests.contains(floorNum));
		if (floorNum != getCurrentFloor() && (!requests.contains(floorNum))) { 
			
		
			//if elevator is idle or going up and the request floor is higher than current floor
			// OR the request floor is lower than current floor and elevator is idle or going down
			// accept request.
		if (override == 1 || ((floorNum > getCurrentFloor() && direction >= 0) || 
				(floorNum < getCurrentFloor() && direction <= 0))) {
			
		 Thread.yield();
		 synchronized (requests) { 
	        requests.add(floorNum);
	        //System.out.printf("######In pushButton: received floorNum %d. ElevatorID %d\n ", floorNum, getElevatorID());
	        requests.notifyAll(); 			
	        Thread.yield();
	       }
		 }
	}else if( (floorNum == 1 && direction == 0 && requests.isEmpty() == true)){
		Thread.yield();
		 synchronized (requests) { 
			// System.out.printf("Received request from elevator controller with floor number %d and override %d\n", floorNum, override);
	        requests.add(floorNum);
	        //System.out.printf("######In pushButton: received floorNum %d. ElevatorID %d\n ", floorNum, getElevatorID());
	        requests.notifyAll(); 			
	        Thread.yield();
	       }
	}
		
	}
	
	/**
	 * run() method is responsible for starting an elevator thread.
	 * Keeps it on wait until requests array gets notified
	 * After change to rider request calls method move()
	 */
	public void run() {	
		long t = getTimeOut();
		while (this.running) {
			long t1 = 0;
			Thread.yield();
			try {
				synchronized (requests) {
					t1 = System.currentTimeMillis();
					requests.wait(t);
				}
			} catch (InterruptedException ex) {
			}
			long t2 = System.currentTimeMillis()-t1;
			
			if (t2 >= t) {
				t = getTimeOut();
				
				/*
				 * Before doing countDown() check:
				 * 		if requests are empty
				 * 		if simulation is still in progress
				 * 		if this elevator already performed countDown()
				 * If all conditions are satisfied perform countDown() and remove that elevator
				 * from the list of active elevators
				*/
				
				if(requests.isEmpty() && getCurrentFloor() == 1 && Simulator.finished() == true
						&& Building.getInstance().checkElevators().contains(getElevatorID())){
					countDown.countDown();
					Building.getInstance().checkElevators().remove(Building.getInstance().checkElevators().indexOf(getElevatorID()));
				}
				
				if(getCurrentFloor() != 1){
					System.out.printf(Building.getInstance().getTime() + "     Elevator %d going to default floor.\n", getElevatorID());
					try {
						pushButton(getDefaultFloor(), 1);
					} catch (InvalidInputException e) {}
				}

			} else {
				if (requests.isEmpty()){
					t = t - t2;
					continue;
				}
			} 
			t = getTimeOut();
			try {
				move();
			}
			catch (InterruptedException e) {
				System.out.printf("Elevator %d is stoped\n" , getElevatorID());
			} 
		    catch (InvalidInputException e) {}

		}
	}


	public String getEachRider() {
		String result = " [Riders: ";
		
		if (getAllRiders().size() == 0){
			return "[Riders: None]";
		}
		for (Person p : getAllRiders()){
			result += "P"+p.getPersonId() + ", ";
		}
		//cut the last comma out and add the end bracket
		result = result.substring(0, result.length()-2) + "]";
		return result;
	}
}
