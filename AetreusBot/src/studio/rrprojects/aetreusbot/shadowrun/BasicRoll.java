package studio.rrprojects.aetreusbot.shadowrun;

import java.util.ArrayList;
import java.util.HashMap;

import studio.rrprojects.aetreusbot.command.Shadowrun.RollContainer;

public class BasicRoll {

	// Roll Tables
	static HashMap<Integer, String> basicRollTable = new HashMap<>();
	static HashMap<Integer, String> primeRollTable = new HashMap<>();
	
	// Roll Tracking
	static ArrayList<Integer> rollResults = new ArrayList<>();
	
	public BasicRoll() {
		InitializeRollTables();
	}
	
	public static void InitializeRollTables() {
		basicRollTable.put(1, "One");
		basicRollTable.put(2, "Miss");
		basicRollTable.put(3, "Miss");
		basicRollTable.put(4, "Miss");
		basicRollTable.put(5, "Hit");
		basicRollTable.put(6, "Hit");
		
		primeRollTable.put(1, "One");
		primeRollTable.put(2, "Miss");
		primeRollTable.put(3, "Miss");
		primeRollTable.put(4, "Hit");
		primeRollTable.put(5, "Hit");
		primeRollTable.put(6, "Hit");
	}
	
	public static RollContainer Roll(RollContainer rollContainer) {
		int dicePool = rollContainer.dicePool;
		
		boolean isGlitch = false;
		boolean isCritGlitch = false;
		
		int countOne = 0;
		int countMiss = 0;
		int countHit = 0;
		
		String parseRoll;
		
		for (int i = 0; i < dicePool; i++) {
			rollResults.add(i, RollDice());
			
			/*
			 * if (flagPrimeRunner) {
				parseRoll = primeRollTable.get(rollResults.get(i));
			} else {
				parseRoll = basicRollTable.get(rollResults.get(i));
			}
			 * */
			
			parseRoll = basicRollTable.get(rollResults.get(i));
			
			if (parseRoll.equals("One")) {
				countOne += 1;
			} else if (parseRoll.equals("Miss")) {
				countMiss += 1;
			} else if (parseRoll.equals("Hit")) {
				countHit += 1;
			}
		}
		
		rollContainer.countOne = countOne;
		rollContainer.countMiss = countMiss;
		rollContainer.countHit = countHit;
		
		rollContainer.rollResults = rollResults;
		
		return rollContainer;
	}
	
	private static Integer RollDice() {
		int dieValue = (int) ((Math.random() * 6) + 1);
		return dieValue;
	}

}
