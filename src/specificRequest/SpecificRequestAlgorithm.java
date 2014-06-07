/**
 * Interface for all specific request algorithms
 * @author Sebastian Demian
 * @author Sergie Zorin
 */
package specificRequest;
import elevator.Elevator;
import elevator.Request;


public interface SpecificRequestAlgorithm {

	public Elevator selectElevator(Request r);
}
