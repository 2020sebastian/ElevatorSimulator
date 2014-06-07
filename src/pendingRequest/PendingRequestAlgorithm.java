/**
 * Interface for all pending request algorithms
 * @author Sebastian Demian
 * @author Sergie Zorin
 */
package pendingRequest;
import java.util.ArrayList;

import elevator.Request;
import exceptions.InvalidInputException;

public interface PendingRequestAlgorithm {
	
	public ArrayList<Integer> checkPendingRequests(int currentFloor);
	public void addToPendingList(Request r) throws InvalidInputException;
	
}
