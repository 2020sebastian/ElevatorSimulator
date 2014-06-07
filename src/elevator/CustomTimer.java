/**
 * @author Sebastian Demian
 */

package elevator;

public class CustomTimer {

	private long time;
	
	/**
	 * Starts the current timer instance.
	 */
	public void start(){
		time = System.currentTimeMillis();
	}
	
	/**
	 * Stops the current timer instance and resets the timer to 0.
	 */
	public void stop(){
		time = (System.currentTimeMillis() - time);
	}
	
	/**
	 * Gets the time elapsed since the timer was started.
	 * @return time as an integer
	 */
	public int getTime(){
		 return (int) (time/1000);
	}
	
	/**
	 * Gets the time elapsed since the timer was started.
	 * @return time as long
	 */
	public long getAccurateTime(){
		 return time;
	}
}
