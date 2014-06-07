/**
 * @author Muhammad Shoaib
 */
package elevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import exceptions.InvalidInputException;

public class MainStart {
	

	public static void main(String[] args) throws FileNotFoundException, InvalidInputException, InterruptedException {
	        Scanner scanner = new Scanner(new File("input.csv"));
	        scanner.useDelimiter(",");
	       
	     
	        int simulationDuration = Integer.parseInt(scanner.next());
	
	    	int numberOfFloors = Integer.parseInt(scanner.next()); 
	    
	    	int numberOfElevators = Integer.parseInt(scanner.next());
	    
	    	int maxElevatorCapacity = Integer.parseInt(scanner.next());
	    
	    	int doorTime = Integer.parseInt(scanner.next());
	   
	    	int floorTime = Integer.parseInt(scanner.next());
	    
	    	int peoplePerMinute = Integer.parseInt(scanner.next());
	    	
	    	int timeOut = Integer.parseInt(scanner.next());
	    	
	    	int defaultFloor = Integer.parseInt(scanner.next());
	  
	    
	    	
	        scanner.close();
	        
	     
	        System.out.println("Running simulation");
	        Simulator simulator = new Simulator(simulationDuration, numberOfFloors, numberOfElevators, 
	        		maxElevatorCapacity, doorTime, floorTime, timeOut, peoplePerMinute, defaultFloor);
	        simulator.run();
	   
	        try{
				synchronized(Building.getInstance().latch){
				Building.getInstance().latch.await();
				if(Building.getInstance().latch.getCount() == 0 && Simulator.getTimePassed() >= (simulationDuration*60)){
					for(Elevator e: Building.getInstance().getAllElevators()){
						System.out.printf(Building.getInstance().getTime() + "     Shuting down Elevator %d\n", e.getElevatorID());
						e.elevatorShutDown(false);
					}
					System.out.printf(Building.getInstance().getTime() + "     Shuting down Elevator Controller\n");
					ElevatorController.getInstance().shotDown(false);
				}
				
				}
				
			}catch(InterruptedException e){
				
			}     
	        
	        

	    
	}
}
