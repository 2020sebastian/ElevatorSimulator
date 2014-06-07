/**
 * @author Sebastian Demian
 */
package elevator;

import exceptions.InvalidInputException;

public class Request {
	private int floor;
	private int direction;
	
	/**
	 * Constructor
	 * @param f
	 * @param d
	 * @throws InvalidInputException 
	 */
	public Request(int f, int d) throws InvalidInputException{
		setFloor(f);
		setDirection(d);
	}

	/**
	 * @return the floor 
	 */
	public int getFloor() {
		return floor;
	}

	/**
	 * @param f the floor to be set
	 * @throws InvalidInputException 
	 */
	private void setFloor(int f) throws InvalidInputException {
		if (f < 0 || f > Building.getInstance().getNumberOfFloors()){
			throw new InvalidInputException("Default floor must be an actual floor of this building.");
		}
		floor = f;
	}

	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 * @throws InvalidInputException 
	 */
	private void setDirection(int d) throws InvalidInputException {
		if (d < -1 || d > 1){
			throw new InvalidInputException("Invalid Input direction");
		}
		direction = d;
	}

}
