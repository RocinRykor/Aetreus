package com.rocinrykor.aetreusbot.baxter;

public class Sleep extends Meter {

	private static String name = "Sleep";
	private static int meterLevel;
	private static int decayRate = 15;
	private static int regenRate = 0;
	private static boolean isRegenerating= false;
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public int getMeterLevel() {
		return meterLevel;
	}
	
	@Override
	public void setMeterLevel(int meterLevel) {
		this.meterLevel = meterLevel;
	}
	
	@Override
	public int getDecayRate() {
		return decayRate;
	}
	
	@Override
	public void setDecayRate(int decayRate) {
		this.decayRate = decayRate;
	}
	
	@Override
	public int getRegenRate() {
		return regenRate;
	}
	
	@Override
	public void setRegenRate(int regenRate) {
		this.regenRate = regenRate;
	}
	
	@Override
	public boolean getIsRegenerating() {
		return isRegenerating;
	}
	
	@Override
	public void setIsRegenerating(boolean isRegenerating) {
		this.isRegenerating = isRegenerating;
	}

}
