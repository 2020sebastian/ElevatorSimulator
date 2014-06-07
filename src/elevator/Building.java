/**
 * @author Sebastian Demian
 * @author Sergie Zorin
 */

package elevator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import exceptions.InvalidInputException;

public final class Building {
	
	public volatile static Building instance;
	private int nFloors; 
	private int nElevators;
	private int floorTime; // time between floors
	private int doorTime; // time for opening/closing doors
	private int elevMaxCapacity;
	private int timeOut;
	private int defaultFloor;
	public ElevatorController controller;
	java.util.Date date;
	public CountDownLatch latch;
	private static ArrayList<Floor> floors = new ArrayList<Floor>();
	private static ArrayList<Elevator> elevators = new ArrayList<Elevator>();
	private static ArrayList<Integer> elevatorSimulationCheck = new ArrayList<Integer>();
	/**
	 * @return the elevMaxCapacity
	 */
	public int getElevMaxCapacity() {
		return elevMaxCapacity;
	}

	/**
	 * @param elevMaxCapacity the elevMaxCapacity to set
	 * @throws InvalidInputException 
	 */
	public void setElevMaxCapacity(int capacity) throws InvalidInputException {
		if (capacity < 0){
			throw new InvalidInputException("Capacity can not be negative");
		}
		elevMaxCapacity = capacity;
	}

	/**
	 * 
	 * @return Array of active elevators
	 */
	public ArrayList<Integer> checkElevators(){
		return elevatorSimulationCheck;
	}
	
	/**
	 * @return a copy of all elevators in building
	 */
	public ArrayList<Elevator> getAllElevators(){
		return new ArrayList<Elevator>(elevators);
	}

	/**
	 * 
	 * @throws InvalidInputException
	 */
	private void initFloors() throws InvalidInputException{
		for (int i = 0; i < nFloors; i++){
			floors.add(new Floor(i));
			}
		System.out.println(getTime() + "     Initialized Floors...");
		}
	
	/**
	 * Initialize all elevators in their own thread and start the threads
	 * @throws InvalidInputException
	 */
	private void initElevators() throws InvalidInputException{
		latch = new CountDownLatch(nElevators);
		for (int i = 1; i <= nElevators; i++){
			//initialize each elevator starting at floor 1
			Elevator a = new Elevator(i, floorTime, doorTime, getDefaultFloor(), getElevMaxCapacity(), getTimeOut(), latch);
			Building.elevators.add(a);
			Building.elevatorSimulationCheck.add(i);
			new Thread(a).start();
		}
		System.out.println(getTime() + "     Initialized Elevators...");
	}
	
	/**
	 * @return the default floor of the elevators
	 */
	private int getDefaultFloor() {
		return defaultFloor;
	}

	/**
	 * Initialize the Elevator Controller
	 * @throws InvalidInputException
	 */
	private void initElevatorController() throws InvalidInputException {
		controller = ElevatorController.getInstance();
		new Thread(controller).start();
	}
	/**
	 * Sets the number of floors.
	 * @param floors needs to be greater than 1
	 * @throws InvalidInputException
	 */
	public void setNumberOfFloors(int floors) throws InvalidInputException {
		if (floors < 1){
			throw new InvalidInputException("Number of floors should be greater than 1");
		}
		nFloors = floors;
	}
	
	public int getNumberOfFloors(){
		return nFloors;
	} 
	
	/**
	 * Sets the number of elevators
	 * @param elevators needs to be greater then 1
	 * @throws InvalidInputException
	 */
	public void setNumberOfElevators(int elevators) throws InvalidInputException{
		if (elevators < 1){
			throw new InvalidInputException("Number of elevators should be greater than 1");
		}
		nElevators = elevators;
	}
	
	/**
	 * @return number of elevators in this building
	 */
	public int getNumberOfElevators(){
		return nElevators;
	} 
	
	/**
	 * @return time between floors
	 */
	public int getFloorTime() {
		return floorTime;
	}
	/**
	 * Sets time between floors
	 * @param time
	 * @throws InvalidInputException 
	 */
	public void setFloorTime(int time) throws InvalidInputException{
		if (time < 0){
			throw new InvalidInputException("Time value cannot be negative");
		}
		floorTime = time;
	}
	/**
	 * Sets the time for opening/closing doors.
	 * @param time
	 * @throws InvalidInputException 
	 */
	public void setDoorTime(int time) throws InvalidInputException{
		if (time < 0){
			throw new InvalidInputException("Time value cannot be negative");
		}
		doorTime = time;
	}
	
	/**
	 * @return the time delay for opening/closing doors
	 */
	public int getDoorTime(){
		return doorTime;
	}
	
	/**
	 * Gets a reference to an elevator. 
	 * @param id the ID of the elevator
	 * @return the instance of the requested elevator.
	 * @throws InvalidInputException 
	 */
	public Elevator getElevator(int id) throws InvalidInputException{
		if (id < 1){
			throw new InvalidInputException("The IDs of the elevators start from 1");
		}
		return elevators.get(id-1);
	}

	/**
	 * Gets a specific floor.
	 * @param number the number of the floor you are requesting
	 * @return the instance of the requested floor
	 * @throws InvalidInputException 
	 */
	public Floor getFloor(int number) throws InvalidInputException{
		if (number < 0 || number > Building.getInstance().getNumberOfFloors()){
			throw new InvalidInputException("Floor number needs to be an actual floor number");
		}
		synchronized (floors){
			return floors.get(number);
		}
	}

	/**
	 * @return singleton instance of this building
	 */
	public static Building getInstance() {
        if (instance == null) {
            synchronized(Building.class) {
                if (instance == null)
                    instance = new Building();
            }
        }
        return instance;
    }
	
	/**
	 * private constructor
	 */
	private Building(){
        System.out.println(getTime() + "     Created Building...");
	}
	/**
	 * Initialize floors, elevators and elevator controller
	 * @throws InvalidInputException 
	 */
	public void initEverything() throws InvalidInputException{
		initFloors();
		initElevators();
		initElevatorController();
	}
	
	/**
	 * Keeps track of time elapsed. This is used as a reference in all relevant classes.
	 * @return
	 */
	public String getTime(){
		SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss:SS");
		Calendar timestamp = Calendar.getInstance();
		String time = format.format(timestamp.getTime());
		return time;
	}


	/**
	 * @return the time after which the elevators go to their default floor
	 */
	public int getTimeOut() {
		return timeOut;
	}

	/**
	 * Set the time after which the elevators go to their default floor
	 * @param t
	 * @throws InvalidInputException 
	 */
	public void setTimeOut(int t) throws InvalidInputException {
		if (t < 0){
			throw new InvalidInputException("Time can not be negative");
		}
		timeOut = t;
	}

	/**
	 * @param d the defaultFloor to set
	 * @throws InvalidInputException 
	 */
	public void setDefaultFloor(int d) throws InvalidInputException {
		if (d < 0 || d > Building.getInstance().getNumberOfFloors()){
			throw new InvalidInputException("Default floor must be an actual floor number");
		}
		defaultFloor = d;
	}
}
	