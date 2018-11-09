package com.rocinrykor.aetreusbot.utils;

public class StatTracker {

	public long startTime = System.currentTimeMillis();
	public int messageNumber;
	public int commandNumber;
	public long timeOfLastUpdate = startTime; 
	public long timeSinceLastUpdate;
	
	 public void UpdateMessageCounter() {
		 Update();
		 messageNumber++;
	}
	
	public void UpdateCommandCounter() {
		Update();
		commandNumber++;
	}
	
	public void Update() {
		long currentTime = System.currentTimeMillis();
		timeOfLastUpdate = currentTime - timeOfLastUpdate;
		timeOfLastUpdate = currentTime;
	}
}
