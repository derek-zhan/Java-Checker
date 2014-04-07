package com.checkers.gameboard;

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
			this.running = false;
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
