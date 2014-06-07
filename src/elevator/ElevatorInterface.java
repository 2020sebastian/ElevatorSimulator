package elevator;

import exceptions.InvalidInputException;


public interface ElevatorInterface {
	
	public int getElevatorID();
	public void setMaximumCapacity(int maxCapacity) throws InvalidInputException;
	public int getMaximumCapacity();
	public void move() throws InterruptedException, InvalidInputException;
	public void run();
	public void openDoor() throws InvalidInputException;
	public void closeDoor();
	public int getCurrentFloor();
	public int getDefaultFloor();
	public void setDefaultFloor(int floor) throws InvalidInputException;
	public boolean getDoorStatus();
	public void setDoorStatus(boolean status);
	public boolean isIdle();
	public void pushButton(int floorNum, int override) throws InvalidInputException;
}
