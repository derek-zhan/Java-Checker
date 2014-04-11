package com.checkers.gameboard;


/**
 * myTimer is a timer class used to limit AI movements and set
 * proper time limits on moves. This class should be reworked to become
 * more efficient and to allow pause/resume as well as the loading of saved times
 * from previous game state.
 * 
 * TODO:
 * -allow pause/resume
 * -load saved times
 * 
 * @author SONY
 * @version 0.3
 * @since 06-04-2014
 * 
 * */
public class myTimer {
	private long startTime = 0;
	private double durationMin = 1;
	private int durationSec = (int) (60 * durationMin);
	private boolean running = false;

	public myTimer(double durationMin) {
		this.durationMin = durationMin;
		this.durationSec = (int) (60 * durationMin);
	}

	public void setStart() {
		this.startTime = System.currentTimeMillis();
		this.running = true;
	}

	public boolean isTimerFinished() {
		if (running && (System.currentTimeMillis() - startTime) / 1000 > durationMin * 60) {
			return true;
		} else
			return false;
	}

	public int getTimeDifference() {
		if (running) {
			return durationSec - (int) ( System.currentTimeMillis() - startTime) / 1000;
		} else
			return durationSec;
	}

	public void setDuration(double durationMin) {
		this.durationMin = durationMin;
		this.durationSec = (int) (60 * durationMin);
	}

	public boolean isRunning() {
		return running;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
}
