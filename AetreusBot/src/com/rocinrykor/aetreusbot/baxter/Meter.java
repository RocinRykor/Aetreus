package com.rocinrykor.aetreusbot.baxter;

import java.util.ArrayList;

import com.rocinrykor.aetreusbot.command.Command;

public abstract class Meter {

	public static ArrayList<Meter> meters;
	
	public static void Init() {
		meters = new ArrayList<>();
		
		meters.add(new Hunger());
		meters.add(new Sleep());
		meters.add (new Hygiene());
		meters.add(new Playfulness());
		meters.add(new Health());
		
		InitMeter();
	}
	
	public abstract String getName();
	
	public abstract int getMeterLevel();
	public abstract void setMeterLevel(int meterLevel);
	
	public abstract int getDecayRate();
	public abstract void setDecayRate(int decayRate);
	
	public abstract int getRegenRate();
	public abstract void setRegenRate(int regenRate);
	
	public abstract boolean getIsRegenerating();
	public abstract void setIsRegenerating(boolean isRegenerating);
	
	public static void InitMeter() {
		for (Meter meter : Meter.meters) {
			meter.setMeterLevel(StartingValue());
			System.out.println("Meter: " + meter.getName() + " | Level: " + meter.getMeterLevel());
		}
	}

	private static int StartingValue() {
		return (int) (((Math.random() * 10) + 1) * 50) + 500;
	}

	public static void Update() {
		System.out.println("Update");
		for (Meter meter : Meter.meters) {
			if (meter.getName().equalsIgnoreCase("Health")) {
				//Calculate Total Health
			} else {
				meter.setMeterLevel(MeterDecay(meter));
				System.out.println("Meter: " + meter.getName() + " | Level: " + meter.getMeterLevel());
				//Regen Check
			}
		}
	}

	private static int MeterDecay(Meter meter) {
		int meterLevel = meter.getMeterLevel();
		int decayRate = meter.getDecayRate();
		
		int totalDecayRate = decayRate * BoolToBinary(!meter.getIsRegenerating());
		
		return (Math.max(meterLevel - totalDecayRate, 0));
	}

	private static int BoolToBinary(boolean input) {
		if (input) {
			return 1;
		} else {
			return 0;
		}
	}
}
