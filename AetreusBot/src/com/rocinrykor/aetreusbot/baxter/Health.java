package com.rocinrykor.aetreusbot.baxter;

public class Health extends Meter {

	private String name = "Health";
	private int meterLevel;
	private int decayRate = 0;
	private int regenRate = 0;
	private int regenAmount = 0;
	private boolean isRegenerating= false;
	
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
	public int getRegenAmount() {
		return regenAmount;
	}

	@Override
	public void setRegenAmount(int regenAmount) {
		this.regenAmount = regenAmount;
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
