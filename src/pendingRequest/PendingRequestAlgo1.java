/**
 * @author Sebastian Demian
 * @author Sergie Zorin
 */
package pendingRequest;
import java.util.ArrayList;
import java.util.PriorityQueue;

import elevator.Request;
import exceptions.InvalidInputException;

public class PendingRequestAlgo1 implements PendingRequestAlgorithm {

	private ArrayList<Request> pendingFloorRequests = new ArrayList<Request>();

	public ArrayList<Integer> checkPendingRequests(int currentFloor) {

		//if no pending requests, return null;
		if (pendingFloorRequests.isEmpty()){
			return null;
		}
		ArrayList<Integer> selectedPendingRequests = new ArrayList<Integer>();

		//get the first element in the queue
		Request initialRequest = pendingFloorRequests.remove(0);
		
		//add it to the result list
		selectedPendingRequests.add(initialRequest.getFloor());


		//if there are any remaining pending requests
		for (Request r : pendingFloorRequests){

			//if the directions are the same
			if (r.getDirection() == initialRequest.getDirection()){
				//if the direction of the initial request is UP
				if (initialRequest.getDirection() == 1){
					//if the direction from the initial request floor to the
					// Next request (r) floor is UP
					// In other words, if the next floor is higher than the initial request floor
					if (r.getFloor() > initialRequest.getFloor()){
						//add it to the list
						selectedPendingRequests.add(r.getFloor());
					}
					//if the direction of the initial request is DOWN
				} else if (initialRequest.getDirection() == -1){
					if (r.getFloor() < initialRequest.getFloor()){
						//add it to the result list
						selectedPendingRequests.add(r.getFloor());
					}
				}
			}
		}
		return selectedPendingRequests;
	}

	/**
	 * Add the request to the elevator controller's pending request list
	 * @param r an elevator request
	 * @throws InvalidInputException
	 */
	public void addToPendingList(Request r) throws InvalidInputException{
		if (r == null){
			throw new InvalidInputException("Input can not be null");
		}
		pendingFloorRequests.add(r);
	}

}
