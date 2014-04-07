package com.checkers.gameboard;
import java.util.Timer;
import java.util.TimerTask;

public class TimerClass {
	Timer timer;
	
	public TimerClass(int durationSec) {
		timer = new Timer();
	}
	
	class Waiting extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//timer.wait(timeout)
		}
		
	}
	
	


}
