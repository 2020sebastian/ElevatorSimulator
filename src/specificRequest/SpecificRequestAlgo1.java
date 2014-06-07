/**
 * @author Sebastian Demian
 * @author Sergie Zorin
 */
package specificRequest;
import elevator.Building;
import elevator.Elevator;
import elevator.Request;
import exceptions.InvalidInputException;

public class SpecificRequestAlgo1 implements SpecificRequestAlgorithm {

/**
 * Selects the most suitable elevator for a given request
 * @param r Request
 */
	public Elevator selectElevator(Request r){


		int elevatorID = -1; 
		try {
			elevatorID = isElevatorOnFloor(r);
		} catch (InvalidInputException e) {}

		//if there is an elevator on this floor
		if (elevatorID != -1){

			try {
				//get that specific elevator
				
				Elevator currentElevator = Building.getInstance().getElevator(elevatorID);
			
				//if idle or going in the desired direction
				if (currentElevator.isIdle() || (currentElevator.getDirection() == r.getDirection())){

					//add the request to that elevator
					//currentElevator.pushButton(r.getFloor()); // in order of arrival ?
		
					return currentElevator;
					
				}
			} catch (InvalidInputException e) {
				e.printStackTrace();
			}
		} 

		
		//if there is no elevator on this floor or
		// there is one on this floor but is not idle or is not going in the desired direction
		else {
			//System.out.printf("NEVER MADE IT HERE\n");
			//is there an elevator already moving?
			for (Elevator e: Building.getInstance().getAllElevators()){

				if (!e.isIdle()){
					
					//is the elevator going into the desired direction?
					try {
						//if it is going in the desired direction
						if (isElevatorDesiredDirection(e, r)){ 
							return e;
						}
					} catch (InvalidInputException e1) {
						e1.printStackTrace();
					}
				} 
				//if there is an idle elevator
				if (e.isIdle() && e.isRequestsEmpty()) {
				
					return e;
				}
			}
		}
		//If there are no available elevators, return null
		return null;
	}
	/**
	 * Sub-diagram A algorithm.
	 * We are not differentiating between elevator requests originating from floors and requests originating from the
	 * button panel inside the elevator. We have adapted the sub-diagram A algorithm to match that.
	 * 
	 * @param e Elevator
	 * @param r request
	 * @return boolean
	 * @throws InvalidInputException
	 */

	public boolean isElevatorDesiredDirection(Elevator e, Request r) throws InvalidInputException{
		
		if (e == null || r == null){
			throw new InvalidInputException("Input can not be null");
		}
		//if the requesting floor is greater than current floor AND direction of elevator is up AND the next direction is up
		if ( (r.getFloor() > e.getCurrentFloor() && e.getDirection() == 1 && e.getNextDirection() == 1) ||
				//if the requesting floor is less than than current floor AND direction of elevator is down
				(r.getFloor() < e.getCurrentFloor() && e.getDirection() == -1 && e.getNextDirection() == -1)){
			return true;
		}
		return false;
	}
	
	/**
	 * If there is an elevator on that floor, returns the id of the elevator, otherwise it returns -1.
	 * @param r
	 * @return
	 * @throws InvalidInputException 
	 */
	private int isElevatorOnFloor(Request r) throws InvalidInputException{

		for (Elevator e: Building.getInstance().getAllElevators()){
			//if there is an elevator on the requested floor
			if (e.getCurrentFloor() == r.getFloor()){
				//return the elevator id
				return e.getElevatorID();
			}
		}
		//if there are no elevators on the requests floor return -1
		return -1;
	}


}
