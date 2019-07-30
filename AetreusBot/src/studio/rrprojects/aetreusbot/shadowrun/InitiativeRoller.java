package studio.rrprojects.aetreusbot.shadowrun;

import java.util.ArrayList;

import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.shadowrun.Shadowrun.RollContainer;

public class InitiativeRoller {
	static ArrayList<Integer> rollResults = new ArrayList<>();
	
	public static String Roll(RollContainer rollContainer, CommandContainer cmd) {
		int dicePool = rollContainer.dicePool;
		rollResults.clear();
		int valueTotal = 0;
		
		for (int i = 0; i < dicePool; i++) {
			int dieValue = RollDice();
			valueTotal += dieValue;
			rollResults.add(i, dieValue);
		}
			
		String message = "Your total initiative is: " + (valueTotal + FlagHandler.GetModifier("Initiative") + "\n");
		message += rollResults.toString();
		
		return message;
	}
	
	private static Integer RollDice() {
		int dieValue = (int) ((Math.random() * 6) + 1);
		return dieValue;
	}
}
