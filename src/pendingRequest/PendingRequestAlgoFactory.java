/**
 * @author Sebastian Demian
 * @author Sergie Zorin
 */
package pendingRequest;
import exceptions.InvalidInputException;

public class PendingRequestAlgoFactory {
	/**
	 * @param number
	 * @return Different algorithms based on the input
	 * @throws InvalidInputException 
	 */
	public static PendingRequestAlgorithm getAlgorithm(int number) {
		if (number == 1){
			return new PendingRequestAlgo1();
		}else{
			return null;
		}
	}
}
