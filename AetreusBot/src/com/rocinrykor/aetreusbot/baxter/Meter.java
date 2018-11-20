package com.rocinrykor.aetreusbot.baxter;

import java.util.ArrayList;

import com.rocinrykor.aetreusbot.command.Baxter;

public abstract class Meter {

	public static ArrayList<Meter> meters;
	
	public static void Init() {
		meters = new ArrayList<>();
		
		meters.add(new Hunger());
		meters.add(new Sleep());
		meters.add (new Hygiene());
		meters.add(new Playfulness());
		
		meters.add(new Health()); //Don't add meters after this one, ensure that it is last.
		
		InitMeter();
		Baxter.InitCommands();
	}
	
	public abstract String getName();
	
	public abstract int getMeterLevel();
	public abstract void setMeterLevel(int meterLevel);
	
	public abstract int getDecayRate();
	public abstract void setDecayRate(int decayRate);
	
	public abstract int getRegenRate();
	public abstract void setRegenRate(int regenRate);
	
	public abstract int getRegenAmount();
	public abstract void setRegenAmount(int regenAmount);
	
	public abstract boolean getIsRegenerating();
	public abstract void setIsRegenerating(boolean isRegenerating);
	
	public static void InitMeter() {
		for (Meter meter : Meter.meters) {
			meter.setMeterLevel(StartingValue());
			System.out.println("Meter: " + meter.getName() + " | Level: " + meter.getMeterLevel());
		}
	}

	private static int StartingValue() {
		int baseValue = (int) ((Math.random() * 10 ) + 1);
		int totalValue = (baseValue * 50) + 500;
		
		System.out.println("Base: " + baseValue + " | Total: " + totalValue);
		
		return totalValue;
	}

	public static void Update() {
		for (Meter meter : Meter.meters) {
			if (meter.getName().equalsIgnoreCase("Health")) {
				CalculateHealth(meter);
			} else {
				meter.setMeterLevel(MeterDecay(meter));
				
				if (meter.getIsRegenerating()) {
					int tmpRegenRate = CalcRegenRate(meter);
					meter.setMeterLevel(MeterRegen(meter, tmpRegenRate));
					CalcRemainingRegen(meter, tmpRegenRate);
				}
			}
		}
	}

	private static void CalculateHealth(Meter meter) {
		int totalMeterLevel = 0;
		for (int i = 0; i < meters.size() - 1; i++) {
			totalMeterLevel += meters.get(i).getMeterLevel();
		}
		
		int avgMeterLevel = (totalMeterLevel / (meters.size() - 1));
		
		meter.setMeterLevel(avgMeterLevel);
	}

	private static void CalcRemainingRegen(Meter meter, int tmpRegenRate) {
		meter.setRegenAmount(meter.getRegenAmount() - tmpRegenRate);
		if (meter.getRegenAmount() <= 0) {
			meter.setIsRegenerating(false);
		}
	}

	private static int CalcRegenRate(Meter meter) {
		int regenRate = (int) Math.max(meter.getRegenAmount() * .2, 15);
		return Math.min(regenRate, meter.getRegenAmount());
	}

	private static int MeterRegen(Meter meter, int tmpRegenRate) {
		return Math.min(meter.getMeterLevel() + tmpRegenRate, 1000);
	}

	private static int MeterDecay(Meter meter) {
		int meterLevel = meter.getMeterLevel();
		int decayRate = meter.getDecayRate();
		
		int totalDecayRate = decayRate * BoolToBinary(!meter.getIsRegenerating());
		
		int newValue = (Math.max(meterLevel - totalDecayRate, 0));
		
		if (newValue <= 250 && !meter.getIsRegenerating()) {
			if ((Math.random() * 10) <= 1) {
				meter.setRegenAmount(500);
				meter.setIsRegenerating(true);
			}
		}
		
		return newValue;
	}

	private static int BoolToBinary(boolean input) {
		if (input) {
			return 1;
		} else {
			return 0;
		}
	}
}
