/**
 * @author Sebastian Demian
 * @author Sergie Zorin
 */
package specificRequest;

import exceptions.InvalidInputException;

public class SpecificRequestAlgoFactory {

	/**
	 * 
	 * @param number
	 * @return Different algorithms based on the input
	 * @throws InvalidInputException 
	 */
	public static SpecificRequestAlgorithm getAlgorithm(int number) {		
		if (number == 1){
			return new SpecificRequestAlgo1();
		}else {
			return null;
		}
	}
}
