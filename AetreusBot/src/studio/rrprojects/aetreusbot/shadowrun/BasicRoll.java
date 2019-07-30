package studio.rrprojects.aetreusbot.shadowrun;

import java.util.ArrayList;
import java.util.HashMap;

import studio.rrprojects.aetreusbot.shadowrun.Shadowrun.RollContainer;

public class BasicRoll {

	// Roll Tables
	static HashMap<Integer, String> basicRollTable = new HashMap<>();
	static HashMap<Integer, String> primeRollTable = new HashMap<>();
	
	// Roll Tracking
	static ArrayList<Integer> rollResults = new ArrayList<>();
	
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
		int countOne = rollContainer.countOne;
		int countMiss = rollContainer.countMiss;
		int countHit = rollContainer.countHit;
		int explodingPool = rollContainer.explodingPool;
		
		boolean flagPrimeRunner = rollContainer.flagPrimeRunner;
		
		rollResults.clear();
		
		String parseRoll;
		
		for (int i = 0; i < dicePool; i++) {
			rollResults.add(i, RollDice());
			
			int currentRoll = rollResults.get(i);
			
			if (currentRoll == 6) {
				explodingPool += 1;
			}
			
			if (flagPrimeRunner) {
				parseRoll = primeRollTable.get(currentRoll);
			} else {
				parseRoll = basicRollTable.get(currentRoll);
			}
			
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
		rollContainer.explodingPool = explodingPool;
		
		rollContainer.rollResults = rollResults;
		
		return rollContainer;
	}
	
	
	
	private static Integer RollDice() {
		int dieValue = (int) ((Math.random() * 6) + 1);
		return dieValue;
	}

	public static RollContainer ExplodingSixesRoller(RollContainer rollContainer) {
		int dicePool = rollContainer.explodingPool;
		System.out.println("Exploding Dice:" + dicePool);
		boolean flagPrimeRunner = rollContainer.flagPrimeRunner;
		int countHit = 0;
		
		String parseRoll;
		
		for (int i = 0; i < dicePool; i++) {
			rollResults.add(i, RollDice());
			
			int currentRoll = rollResults.get(i);
			System.out.println(currentRoll);
			
			if (currentRoll == 6) {
				dicePool += 1;
			}
			
			if (flagPrimeRunner) {
				parseRoll = primeRollTable.get(currentRoll);
			} else {
				parseRoll = basicRollTable.get(currentRoll);
			}
			
			if (parseRoll.equals("Hit")) {
				countHit += 1;
			}
		}
		
		rollContainer.countHit += countHit;
		rollContainer.rollResults = rollResults;
		
		return rollContainer;
	}

}
