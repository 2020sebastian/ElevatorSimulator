/**
 * @author Sebastian Demian
 *
 */

package tests;
import elevator.Timer;

import static org.junit.Assert.*;

import org.junit.Test;



public class TimerTest {
	

	@Test
	public void getAccurateTime() {
		Timer a = new Timer();
		
		double test = System.currentTimeMillis();
		a.start();
		
		assertEquals(a.getAccurateTime(), test, 0.01);
	}
	
	@Test
	public void getTime() {
		Timer a = new Timer();
		
		double test = System.currentTimeMillis()/1000;
		a.start();
		
		assertEquals(a.getTime(), test, 0.01);
		
	}

}
