/**
 * @author Sebastian Demian
 * @author Sergie Zorin
 *
 */
package elevator;
import java.util.ArrayList;
import java.util.HashMap;

import pendingRequest.PendingRequestAlgoFactory;
import pendingRequest.PendingRequestAlgorithm;
import specificRequest.SpecificRequestAlgoFactory;
import specificRequest.SpecificRequestAlgorithm;
import exceptions.ElevatorFullException;
import exceptions.InvalidInputException;

public class ElevatorController implements Runnable {

	private volatile boolean running = true;	
	private static ElevatorController instance;
	private PendingRequestAlgorithm pendingAlgo = PendingRequestAlgoFactory.getAlgorithm(1); // elevator pending delegate
	private SpecificRequestAlgorithm specificAlgo = SpecificRequestAlgoFactory.getAlgorithm(1); // elevator call delegate
	private ArrayList<Request> floorRequests = new ArrayList<Request>();
	public static HashMap<Integer, String> peopleOnFloors = new HashMap<Integer, String>();
	public static HashMap<Integer, String> allgeneratedRiders = new HashMap<Integer, String>();
	
	
	public void run() {	
		while(running){
			if(Building.getInstance().latch.getCount() == 0){
				running = false;
				break;
			}
			synchronized(floorRequests){
				try {
				
					floorRequests.wait();
			
					while (hasFloorRequests()){
						//pop the request from the queue and process it
						Request currentRequest = floorRequests.remove(0);
						

						//retrieve an appropriate elevator to handle to request
						Elevator selectedElevator = specificAlgo.selectElevator(currentRequest);

						//if specific algo returned something
						if (selectedElevator != null){
							
							//add the request to that elevator
							selectedElevator.pushButton(currentRequest.getFloor(), 1);
							selectedElevator.setNextDirection(currentRequest.getDirection());
							

						} else if (selectedElevator == null){
							//if there are no available elevators, add to pending
							try {
								pendingAlgo.addToPendingList(currentRequest);
							} catch (InvalidInputException e) {
								e.printStackTrace();
							} 
						}
					}
				} catch (InterruptedException e1) {
					this.running = false;
					System.out.printf("Elevator Controller is stoped\n");
				
				} catch (InvalidInputException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ArrayList<Integer> checkPendingRequests(int currentFloor) throws InvalidInputException{
		if (currentFloor < 0 || currentFloor > Building.getInstance().getNumberOfFloors()){
			throw new InvalidInputException("currentFloor must be an actual floor of this building.");
		}
		return pendingAlgo.checkPendingRequests(currentFloor);
	}

	/**
	 * Add the request to the pending list
	 * @param r
	 * @throws InvalidInputException
	 */
	public void addToPendingList(Request r) throws InvalidInputException{
		if (r == null){
			throw new InvalidInputException("Input can not be null");
		}
		pendingAlgo.addToPendingList(r);
	}

	/**
	 * Adds the request to the list of floor requests
	 * @param floorNumber
	 * @param direction
	 * @throws InvalidInputException 
	 */
	public void addRequest(int floorNumber, int direction) throws InvalidInputException{
		if ( floorNumber < 0 || floorNumber > Building.getInstance().getNumberOfFloors()){
			throw new InvalidInputException("Input can not be null");
		}
		synchronized(floorRequests){
		floorRequests.add(new Request(floorNumber, direction));
		floorRequests.notifyAll();
		}
	}

	/**
	 * Removes the request that is next in line to be served.
	 */
	public void removeRequest(){
		if (!floorRequests.isEmpty()){
			floorRequests.remove(0);
		}
	}

	/**
	 * Checks to see if there are any floor requests left in the queue
	 * @return boolean
	 */
	private boolean hasFloorRequests(){
		return !floorRequests.isEmpty();
	}

	public void moveToFloor(int elevatorID) throws InvalidInputException{
	
		//gets the number of the current floor
		int currentFloor = Building.getInstance().getElevator(elevatorID).getCurrentFloor();

		//get a copy of the array containing all passengers in the elevator
		ArrayList<Person> people = Building.getInstance().getElevator(elevatorID).getAllRiders();

		//move each person from the elevator to the current floor, if this is their destination
		for (Person p: people){
			if (p.getDestination() == currentFloor){
				
				//Add to floor, mark as finished
				p.stopRideTime();
				Building.getInstance().getFloor(currentFloor).addFinishedRider(p.getPersonId(), p.getWaitTime(), p.getRideTime());
				//Remove from elevator
				Building.getInstance().getElevator(elevatorID).removeRider(p);
				System.out.println(Building.getInstance().getTime() + "     Person P"+ p.getPersonId() +" exits Elevator "+ 
						elevatorID);
			}
		}
	}

	public void moveToElevator(int elevatorID) throws InvalidInputException{

		//get a reference to the elevator
		Elevator currentElevator = Building.getInstance().getElevator(elevatorID);

		int currentFloor = currentElevator.getCurrentFloor();

		//Get a copy of the array containing all the people on that floor
		ArrayList<Person> people = Building.getInstance().getFloor(currentFloor).getAllPeople();
		
		//move each person from the current floor to the specified elevator, if the elevator is going in the same direction.
		//After the person is inside, push button on the button panel
		
		for (Person p: people){

			if ((p.getDirection() == currentElevator.getDirection() || currentElevator.getDirection() == 0) && 
				(p.getDestination() != currentElevator.getCurrentFloor())){
				try {
					
					
					p.stopWaitTime();
					
					p.startRideTime();
					currentElevator.addRider(p);
					System.out.println(Building.getInstance().getTime() + "     Person P"+ p.getPersonId() +" enters Elevator "+ 
					currentElevator.getElevatorID() + currentElevator.getEachRider());

					Building.getInstance().getFloor(currentFloor).removeRider(p);
				
					currentElevator.pushButton(p.getDestination(), 0);
		
					
					System.out.println(Building.getInstance().getTime() + "     Person P"+ p.getPersonId() + " pushed button " + p.getDestination() + 
							" inside elevator " + currentElevator.getElevatorID());
					
				} catch (ElevatorFullException e){
					//if the elevator is full, stop adding persons
					break;
				}
			}else if(p.getDirection() == 1 && currentElevator.getDirection() == 0){
				try {
					currentElevator.addRider(p);
					System.out.println(Building.getInstance().getTime() + "     Person P"+ p.getPersonId() +" enters Elevator "+ 
							currentElevator.getElevatorID() + currentElevator.getEachRider());
					Building.getInstance().getFloor(currentFloor).removeRider(p);
					currentElevator.pushButton(p.getDestination(), 1);
				} catch (ElevatorFullException e) {
					break;
				}
			}
		}
	}
	
	/**
	 * 
	 * @return singleton instance of this building
	 */
	public static ElevatorController getInstance() {
		if (instance == null) {
			instance = new ElevatorController();	
		}
		return instance;
	}

	public void shotDown(boolean command){
		synchronized(floorRequests){
		running = command;
		floorRequests.notifyAll();
		System.out.println(Building.getInstance().getTime() + "     Elevator contoller is OFF");
		
		try {
			Simulator.printAMMTimeByFloor();
			Simulator.getAverageRideTime();
			Simulator.getMaxRideTime();
			Simulator.getMinRideTime();
			Simulator.getWRTime();
			//Simulator.printPersonsOnFloor(1);
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		}
	}
	/**
	 * private constructor
	 */
	private ElevatorController(){
	}

	/**
	 * Lets the controller know that elevator has arrived at the floor
	 * @param id elevator ID
	 * @throws InvalidInputException
	 */
	public void arrived(int elevatorID) throws InvalidInputException {	
		moveToFloor(elevatorID);
		moveToElevator(elevatorID);
	}

}