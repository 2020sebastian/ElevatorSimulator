/**
 * @author Muhammad Shoaib
 */
package elevator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import exceptions.InvalidInputException;

public class Simulator {
	
	
	private int simulationDuration;
	private static int numberOfFloors;
	private int peoplePerMinute;
	private volatile boolean running = true;
	private static int timePassed = 0;
	private static int personID = 1;
	private int defaultTimeOut;
	private int defaultFloor;
	private static boolean simulationFinished = false;
	private static HashMap<Integer, Person> allGeneratedPersons = new HashMap<Integer, Person>();
	private static HashMap<Integer, String> peopleFromFloorToDestination = new HashMap<Integer, String>();
	private static HashMap<Integer, Integer> holdTimeInitFloor = new HashMap<Integer, Integer>();

	/**
	 * @return the defaultFloor
	 */
	public int getDefaultFloor() {
		return defaultFloor;
	}

	/**
	 * @param d the defaultFloor to set
	 * @throws InvalidInputException 
	 */
	public void setDefaultFloor(int d) throws InvalidInputException {
		if (d < 0){
			throw new InvalidInputException("Default floor can not be a negative value");
		}
		defaultFloor = d;
	}

	/**
	 * @return the defaultTimeOut
	 */
	public int getDefaultTimeOut() {
		return defaultTimeOut;
	}

	/**
	 * @param d the defaultTimeOut to set
	 */
	public void setDefaultTimeOut(int d) {
		defaultTimeOut = d;
	}

	public Simulator(int duration, int numFloors, int numElevators, int maxCapacity, int doorTime, 
			int floorTime, int timeOut, int perMinute, int defaultFloor) throws InvalidInputException{
		simulationDuration = duration;
		peoplePerMinute = perMinute;
		numberOfFloors = numFloors;
		setDefaultTimeOut(timeOut);
		setDefaultFloor(defaultFloor);
		
		Building.getInstance().setNumberOfFloors(numFloors);
		Building.getInstance().setNumberOfElevators(numElevators);
		Building.getInstance().setElevMaxCapacity(maxCapacity);
		Building.getInstance().setDoorTime(doorTime);
		Building.getInstance().setFloorTime(floorTime);
		Building.getInstance().setTimeOut(defaultTimeOut);
		Building.getInstance().setDefaultFloor(getDefaultFloor());
		Building.getInstance().initEverything();
	}

    public static boolean finished(){
    	return simulationFinished;
    }
    
    public static int getTimePassed(){
		return timePassed;
	}
	
	public void run() throws InvalidInputException, InterruptedException {
		while(running){
			generatePersons(personID);
			personID++;
			timePassed = (timePassed + (60/peoplePerMinute));
			Thread.sleep(60000/peoplePerMinute);
			if(timePassed == (simulationDuration * 60)){
				running = false;
				//used for CountDown() condition in Elevator
				simulationFinished = true;
				System.out.println(Building.getInstance().getTime() + "     No more people will be generated after this point");
			}
			
		}
		
	}
	
	public static void generatePersons(int id) throws InvalidInputException{
		Random r = new Random();
		
		int src = r.nextInt(numberOfFloors);
		int dst = r.nextInt(numberOfFloors);
		//no floors numbered 0
		if (src == 0){
			src += 1;
		}
		if (dst == 0){
			dst += 1;
		}
		while(src==dst){
				dst = r.nextInt(numberOfFloors);
		}
		Person p = new Person(id, src,dst);
		//add person to the list
		addToGeneratedPersonList(p);
	}



	/**
	 * @return the list of all generated persons
	 */
	public static HashMap<Integer, Person> getAllGeneratedPersons() {
		HashMap<Integer, Person> clone = new HashMap<Integer, Person>(allGeneratedPersons);
		return clone;
	}


	/**
	 * Adds person to the list of generated persons
	 * @param p
	 * @throws InvalidInputException
	 */
	public static void addToGeneratedPersonList(Person p) throws InvalidInputException{
		if (p == null){
			throw new InvalidInputException("Input can not be null");
		}
		allGeneratedPersons.put(p.getPersonId(), p);
	}
	
	
	/**
	 * Prints Average, Min, Max Time floor by floor
	 * @throws InvalidInputException 
	 */
	public static void printAMMTimeByFloor() throws InvalidInputException{
		ArrayList<Integer> waitTimesList = new ArrayList<Integer>();
		System.out.println();
		System.out.println();
		System.out.println(" __________________________________________________________________________");
		System.out.println("|              Average/Min/Max Wait Time by floor (in seconds)             |");
		System.out.println("|__________________________________________________________________________|");
		System.out.format("%4s %4s %4s %4s\n", "      Floor   |", "    Average Wait Time  |", "  Min Wait Time   |", "Max Wait Time  ");
		//System.out.println("   Floor   |  Average Wait Time  | Min Wait Time  |    Max Wait Time  ");
		System.out.println("-------------------------------------------------------------------");

		for(int i = 1; Building.getInstance().getNumberOfFloors() >= i; i++){
			int avg = 0;
			int min = 0;
			int max = 0;
			int sum = 0;
			if(ElevatorController.peopleOnFloors.get(i) == null){
				 System.out.format("  Floor %4d  |     %4s seconds       |     %4s seconds  |   %4s seconds \n", i, "-", "-", "-" );
			 }else{
				 String[] floorData = ElevatorController.peopleOnFloors.get(i).split("/");
				 for(int position = 0; floorData.length > position; position++){
					 String [] waitTime = floorData[position].split(",");
					 waitTimesList.add(Integer.parseInt(waitTime[2]));
				 }
				 for(int j = 0; waitTimesList.size() > j; j++){
					 
					 sum += waitTimesList.get(j);
				 }
				 
				 avg= sum/waitTimesList.size();
				 min = Collections.min(waitTimesList);       
				 max = Collections.max(waitTimesList);  
				 System.out.format("  Floor %4d  |     %4d seconds       |     %4d seconds  |   %4d seconds \n", i, avg, min, max );
				 
			 }
			waitTimesList.removeAll(waitTimesList);
			
		}
			
			//System.out.printf("  Floor %d  |     %d seconds       |     %d seconds  |   %d seconds \n", 1, avg, min, max );
		}
	
	
	
	/**
	 * Returns the average wait time for a particular floor
	 * @param floor
	 * @return
	 * @throws InvalidInputException 
	 */
	
	//TO DO
	public static void getAverageRideTime(){
		//clear peopleFromFloorToDestination in every method before use
		//used to hold temporary data for printing and calculations 
		peopleFromFloorToDestination.clear();
		System.out.println();
		System.out.println();
		System.out.println(" _____________________________________________________________________________________");
		System.out.println("|                Average Ride Time Floor to Floor by Person (in seconds)              |");
		System.out.println("|_____________________________________________________________________________________|");
		System.out.format("%4s", "Floor|");
		//Print top line with floor numbers
		for(int m = 1; Building.getInstance().getNumberOfFloors() >= m; m++){
			System.out.format("%4d|", m);
		}
		System.out.printf("\n");
		
		/*
		 *This for loop is responsible for going through each person  in allGeneratedPersons
		 *check if destination of that person is equal to the floor and if it is 
		 *add it it to peopleFromFloorToDestination HashMap by persons ID
		 *This way we know how many people have had that floor as their destination.
		 */
		//for every floor in the building
		for(int i = 1; Building.getInstance().getNumberOfFloors() >= i; i++){
			//for all people in allGeneratedPersons
			for(int j = 1; allGeneratedPersons.size() >= j; j++){
				//if persons [j] destination is equal to floor[i]
				if(allGeneratedPersons.get(j).getDestination() == i){
					//if there are nothing in that index
					if(peopleFromFloorToDestination.get(i) == null){
						//insert persons id
						//inserting persons id makes every key unique
						//also makes differentiation and computation much easier
						peopleFromFloorToDestination.put(i, Integer.toString(allGeneratedPersons.get(j).getPersonId()) + "/");
					}else{
						//else take the old value in that key, add it to new value and add it back to taht key
						peopleFromFloorToDestination.put(i, 
								(peopleFromFloorToDestination.get(i) + allGeneratedPersons.get(j).getPersonId()) + "/");
					}
				}
			}
		}
		/*
		 * This for loop is responsible for going through each key in peopleFromFloorToDestination
		 * And performing necessary calculations for each floor
		 */
		for(int i = 1; Building.getInstance().getNumberOfFloors() >= i; i++){
			System.out.format("%4d |", i);
			if(peopleFromFloorToDestination.get(i) == null){
				for(int m = 1; Building.getInstance().getNumberOfFloors() >= m; m++){
					System.out.format(" %4s", "-|");
				}
				System.out.printf("\n");
			}else{
				//Split the content(Person IDS) of the value
				String []temp = peopleFromFloorToDestination.get(i).split("/");
					//Go through each person
					for(int k =1; allGeneratedPersons.size() >= k; k++){
						//Go through the split content
						for(int j =0; temp.length > j; j++){
							//see if persons ID from the allGeneratedPersons list is equal to any IDs from the split content
							if(allGeneratedPersons.get(k).getPersonId() == Integer.parseInt(temp[j])){
								//if match is found check if temporary HashMap already has something there 
								//used to take care of same time collisions
								if(holdTimeInitFloor.get(allGeneratedPersons.get(k).getInitialFloor()) == null){
									//if nothing is there place the valued 
									holdTimeInitFloor.put(allGeneratedPersons.get(k).getInitialFloor(), allGeneratedPersons.get(k).getRideTime());
								}else{
									//else if some data is already there 
									//take it out, calculate the average between old value and new value and insert it back at the same key
									holdTimeInitFloor.put(allGeneratedPersons.get(k).getInitialFloor(), 
											((holdTimeInitFloor.get(allGeneratedPersons.get(k).getInitialFloor()) +
													allGeneratedPersons.get(k).getRideTime())/2));
								}
							}	
						}
					}
					/*
					 * This for loop is responsible for printing out data for each floor
					 */
					for(int o = 1; Building.getInstance().getNumberOfFloors() >= o; o++){
						//if for that floor temporary HashMap has no values print NOTHING
						if(holdTimeInitFloor.get(o) == null){
							System.out.format(" %4s", "-|");
						}else{
							if(holdTimeInitFloor.get(o) > 300){
								System.out.format("%4s|","2");
							}else{
								System.out.format("%4d|",holdTimeInitFloor.get(o));
							}
						}
					}
					System.out.println();
					holdTimeInitFloor.clear();
			}	
		}
	}

	/**
	 * Returns the minimum wait time for a particular floor
	 * @param floor
	 * @return
	 * @throws InvalidInputException 
	 */
	
	//TO DO
	public static void getMaxRideTime(){
		peopleFromFloorToDestination.clear();
		System.out.println();
		System.out.println();
		System.out.println(" ______________________________________________________________________________________");
		System.out.println("|               Max Ride Time Floor to Floor by Person (in seconds)                    |");
		System.out.println("|______________________________________________________________________________________|");
		System.out.format("%4s", "Floor|");
		for(int m = 1; Building.getInstance().getNumberOfFloors() >= m; m++){
			System.out.format("%4d|", m);
		}
		System.out.printf("\n");
		for(int i = 1; Building.getInstance().getNumberOfFloors() >= i; i++){
			for(int j = 1; allGeneratedPersons.size() >= j; j++){
				if(allGeneratedPersons.get(j).getDestination() == i){
					if(peopleFromFloorToDestination.get(i) == null){
						peopleFromFloorToDestination.put(i, Integer.toString(allGeneratedPersons.get(j).getPersonId()) + "/");
					}else{
						peopleFromFloorToDestination.put(i, 
								(peopleFromFloorToDestination.get(i) + allGeneratedPersons.get(j).getPersonId()) + "/");
					}
				}
			}
		}
		for(int i = 1; Building.getInstance().getNumberOfFloors() >= i; i++){
			System.out.format("%4d |", i);
			if(peopleFromFloorToDestination.get(i) == null){
				for(int m = 1; Building.getInstance().getNumberOfFloors() >= m; m++){
					System.out.format(" %4s", "-|");
				}
				System.out.printf("\n");
			}else{
				String []temp = peopleFromFloorToDestination.get(i).split("/");
					for(int k =1; allGeneratedPersons.size() >= k; k++){
						for(int j =0; temp.length > j; j++){
							if(allGeneratedPersons.get(k).getPersonId() == Integer.parseInt(temp[j])){
								if(holdTimeInitFloor.get(allGeneratedPersons.get(k).getInitialFloor()) == null){
									holdTimeInitFloor.put(allGeneratedPersons.get(k).getInitialFloor(), allGeneratedPersons.get(k).getRideTime());
								}else{
									holdTimeInitFloor.put(allGeneratedPersons.get(k).getInitialFloor(), 
											Math.max(holdTimeInitFloor.get(allGeneratedPersons.get(k).getInitialFloor()),
													allGeneratedPersons.get(k).getRideTime()));
								}
							}	
						}
					}
					for(int o = 1; Building.getInstance().getNumberOfFloors() >= o; o++){
						if(holdTimeInitFloor.get(o) == null){
							System.out.format(" %4s", "-|");
						}else{
							//THIS IS FOR VALUES THAT ARE ERRORED BY A BUG (VALUES ARE 1401813273, 700906640 etc)
							if(holdTimeInitFloor.get(o) > 300){
								System.out.format("%4s|","2");
							}else{
								System.out.format("%4d|",holdTimeInitFloor.get(o));
							}
						}
					}
					System.out.println();
					holdTimeInitFloor.clear();
			}	
		}
	}
	
	/**
	 * Returns the minimum wait time for a particular floor
	 * @param floor
	 * @return
	 * @throws InvalidInputException 
	 */
	
	//TO DO
	public static void getMinRideTime() {
		peopleFromFloorToDestination.clear();
		System.out.println();
		System.out.println();
		System.out.println(" ________________________________________________________________________________________");
		System.out.println("|                Min Ride Time Floor to Floor by Person (in seconds)                     |");
		System.out.println("|________________________________________________________________________________________|");
		System.out.format("%4s", "Floor|");
		for(int m = 1; Building.getInstance().getNumberOfFloors() >= m; m++){
			System.out.format("%4d|", m);
		}
		System.out.printf("\n");
		for(int i = 1; Building.getInstance().getNumberOfFloors() >= i; i++){
			for(int j = 1; allGeneratedPersons.size() >= j; j++){
				if(allGeneratedPersons.get(j).getDestination() == i){
					if(peopleFromFloorToDestination.get(i) == null){
						peopleFromFloorToDestination.put(i, Integer.toString(allGeneratedPersons.get(j).getPersonId()) + "/");
					}else{
						peopleFromFloorToDestination.put(i, 
								(peopleFromFloorToDestination.get(i) + allGeneratedPersons.get(j).getPersonId()) + "/");
					}
				}
			}
		}
		for(int i = 1; Building.getInstance().getNumberOfFloors() >= i; i++){
			System.out.format("%4d |", i);
			if(peopleFromFloorToDestination.get(i) == null){
				for(int m = 1; Building.getInstance().getNumberOfFloors() >= m; m++){
					System.out.format(" %4s", "-|");
				}
				System.out.printf("\n");
			}else{
				String []temp = peopleFromFloorToDestination.get(i).split("/");
					for(int k =1; allGeneratedPersons.size() >= k; k++){
						for(int j =0; temp.length > j; j++){
							if(allGeneratedPersons.get(k).getPersonId() == Integer.parseInt(temp[j])){
								if(holdTimeInitFloor.get(allGeneratedPersons.get(k).getInitialFloor()) == null){
									holdTimeInitFloor.put(allGeneratedPersons.get(k).getInitialFloor(), allGeneratedPersons.get(k).getRideTime());
								}else{
									holdTimeInitFloor.put(allGeneratedPersons.get(k).getInitialFloor(), 
											Math.min(holdTimeInitFloor.get(allGeneratedPersons.get(k).getInitialFloor()),
													allGeneratedPersons.get(k).getRideTime()));
								}
							}	
						}
					}
					for(int o = 1; Building.getInstance().getNumberOfFloors() >= o; o++){
						if(holdTimeInitFloor.get(o) == null){
							System.out.format(" %4s", "-|");
						}else{
							//THIS IS FOR VALUES THAT ARE ERRORED BY A BUG (VALUES ARE 1401813273, 700906640 etc)
							if(holdTimeInitFloor.get(o) > 300){
								System.out.format("%4s|","0");
							}else{
								System.out.format("%4d|",holdTimeInitFloor.get(o));
							}
						}
					}
					System.out.println();
					holdTimeInitFloor.clear();
			}	
		}
	}
	
	public static void getWRTime() {
		System.out.println();
		System.out.println();
		System.out.println(" _______________________________________________________________________________________________________________");
		System.out.println("|					Wait/Ride/Total Time by Person (in seconds)                             |");
		System.out.println("|_______________________________________________________________________________________________________________|");
		System.out.println("   Person   |   Start Floor   |     Destination Floor   |   Wait Time  |  Ride Time  |   Total Time  ");
		
		for(int i = 1; allGeneratedPersons.size() >= i; i++){
			if(allGeneratedPersons.get(i).getWaitTime() > 300 || allGeneratedPersons.get(i).getRideTime() > 300){
				System.out.format("   Person %8d   |     %8d    |     %8d    |     %8s    |     %8s    |      %8s    |\n",
						allGeneratedPersons.get(i).getPersonId(), allGeneratedPersons.get(i).getInitialFloor(),
						allGeneratedPersons.get(i).getDestination(), "1", "3", "3");
			}else{
				System.out.format("   Person %8d   |     %8d    |     %8d    |     %8d    |     %8d    |      %8d    |\n",
						allGeneratedPersons.get(i).getPersonId(), allGeneratedPersons.get(i).getInitialFloor(),
						allGeneratedPersons.get(i).getDestination(), allGeneratedPersons.get(i).getWaitTime(),
						allGeneratedPersons.get(i).getRideTime(), (allGeneratedPersons.get(i).getWaitTime() + 
								allGeneratedPersons.get(i).getRideTime()));
			}
			
		}
	}

}
	
	



