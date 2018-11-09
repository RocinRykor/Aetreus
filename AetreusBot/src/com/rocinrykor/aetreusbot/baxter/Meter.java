package com.rocinrykor.aetreusbot.baxter;

import java.util.ArrayList;

import com.rocinrykor.aetreusbot.command.Command;

public abstract class Meter {

	public static ArrayList<Meter> meters;
	
	public static void Init() {
		meters = new ArrayList<>();
		
		meters.add(new Hunger());
	}
	
	public abstract String getName();
	
	public int MeterLevel;
	
	public int DecayRate;
	
	public boolean IsRegenerating;
	
	public int RegenRate;
	
	public abstract void InitMeter();
}
