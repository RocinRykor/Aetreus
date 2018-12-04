package com.rocinrykor.aetreusbot.command;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;
import com.rocinrykor.aetreusbot.discord.DiscordUtil;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Shadowrun extends Command {

	@Override
	public String getName() {
		return "ShadowRun";
	}
	
	@Override
	public String getAlias() {
		return "SR";
	}

	@Override
	public String getDescription() {
		return "A dice roller made specifically for ShadowRun";
	}

	@Override
	public String helpMessage() {
		return "Did you know that other every Tabletop RPG out there has their own rules for rolling dice? What dice they roll, how many they roll, and even what a roll represents? It is honestly quite staggering to think about sometimes. However, by the grace of my Master, I am now more familiar with the game of Shadowrun and have developed a specific rolling command for that game. Let’s go over the most basic of ground rules.\r\n\n" + 
				"In Shadowrun, the most commonly thrown dice is a D6, or more specifically, a pool of D6s. For most rolls we are concerned with how many hits the player has rolled, hits are the number of dice from the pool that have rolled as a 5 or a 6. The number of 1s a character has thrown in a set is also a factor for determine any negative outcomes while the rest are neutrals and are not often worried about.\r\n\n" + 
				"To deal with the rules when you use the command “&sr X” (with X being a number), I will roll that amount of dice and simply inform you of how many ones, neutrals, and hits were gotten from the set.\r\n" + 
				"";
	}

	@Override
	public boolean isAdminOnly() {
		return false;
	}

	@Override
	public boolean isChannelRestricted() {
		return true;
	}

	@Override
	public boolean deleteCallMessage() {
		return false;
	}
	
	String title, header, message;
	
	//All rules that will be handled by settings
	boolean ruleGreaterThanHalf = true;
	boolean ruleAlwaysVerbose = false;
	boolean ruleAutomaticGlitchPenalty = true;
	
	
	//Flags for rolling
	boolean flagVerbose, flagInitiative, flagExtended, flagRerollDice, flagExplodingDice, flagPrimeRunner, flagThresholdTest;
	
	//Modifiers for various flags
	int modifierInitiative, modifierExtended, modifierRerollDice, modifierThreshold;
	
	//Booleans for error checking
	boolean passModifierCheck;
	
	//Glitch Checks
	boolean isGlitch, isCritGlitch;
	
	HashMap<Integer, String> basicRollTable = new HashMap<>();
	HashMap<Integer, String> primeRollTable = new HashMap<>();
	
	Color color;
	
	ArrayList<Integer> rollResults = new ArrayList<>();
	int countOne = 0;
	int countMiss = 0;
	int countHit = 0;
	
	public Shadowrun() {
		InitializeRollTables();
	}
	
	private void InitializeRollTables() {
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

	@Override
	public void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event,
			CommandContainer cmd) {
		
		color = Color.GRAY;
		
		if (primaryArg.equalsIgnoreCase("help")) {
			sendMessage(helpMessage(), event);
			return;
		} else if (primaryArg.equalsIgnoreCase("settings")) {
			ManageSettings(event);
			return;
		}
		
		SetAllFlagsFalse();
		
		CheckFlags(secondaryArg);
		
		if (CheckDigit(primaryArg)) {
			BeginRoller(primaryArg, event);
		} else {
			message = "ERROR: Unable to roll, dice pool not defined \n"
					+ "Please use \"&sr help\" for more info";
			sendMessage(message, event);
		}
		
	}
	
	private void ManageSettings(MessageReceivedEvent event) {
	}

	private void SetAllFlagsFalse() {
		flagExplodingDice = false;
		flagExtended = false;
		flagInitiative = false;
		flagPrimeRunner = false;
		flagRerollDice = false;
		flagThresholdTest = false;
		flagVerbose = false;
	}

	private void BeginRoller(String primaryArg, MessageReceivedEvent event) {
		message = "";
		
		EmbedBuilder builder = new EmbedBuilder();
		
		if (passModifierCheck) {
			int dicePool = Integer.parseInt(primaryArg);
			
			if (flagInitiative) {
				title = "Initiative Roll:";
				header = "Initiative Modifer: " + modifierInitiative;
				InitiativeRoller(dicePool);
			} else if (flagExtended) {
				title = "Extended Roll:";
				header = "Extended Test Dice Pool: " + dicePool + " vs Threshold of " + modifierExtended;
				ExtendedRoller(dicePool);
			} else {
				title = "Success Roll:";
				header = "Dice Pool: " + dicePool;
				BasicRoll(dicePool, false);
			}
			
		} else {
			//fail
			message = "Oops, looks like there is an error with one of the flags, please check them and try again \n"
					+ "Please use \"&sr help\" for more info";
			sendMessage(message, event);
			return;
		}
		
		builder.setColor(color);
		builder.setTitle(title);
		builder.addField(header, DiscordUtil.MessageToCode(message), true);
		
		sendMessage(builder, event);
	}

	private void ExtendedRoller(int dicePool) {
		int remainingDice = dicePool;
		int totalHits = 0;
		int attempts = 0;
		
		boolean glitchStopped = false;
		
		while (remainingDice > 0 && totalHits < modifierExtended) {
			BasicRoll(remainingDice, true);
			
			totalHits += countHit;
			remainingDice -= 1;
			attempts += 1;
			
			if (isGlitch || isCritGlitch) {
				if (isCritGlitch) {
					remainingDice = 0;
					message += "Critical Glitch Detected! Automatic Loss! \n";
					glitchStopped = true;
				} else {
					int dieValue = RollDice();
					message += "Glitch Detected! Automatic Penalty, Losing " + dieValue + " Hits! \n";
					totalHits -= dieValue;
					
					if (totalHits <= 0) {
						remainingDice = 0;
						message += "Glitch Penalty brought Total Hits to 0, Automatic Loss! \n";
						glitchStopped = true;
					}
				}
			}
		}
		
		if (glitchStopped) {
			message += "Result: Failure!";
			color = Color.RED;
			
		} else {
			message += "\nTotal Hits: " + totalHits + "\n";
			
			if (totalHits >= modifierExtended) {
				message += "Result: Success! You passed the threshold of " + modifierExtended + " in " + attempts + " attempts!";
				color = Color.GREEN;
			} else {
				message += "Result: Failure! You failed to pass the threshold of " + modifierExtended + "!";
				color = Color.RED;
			}
		}
		
	}

	private void InitiativeRoller(int dicePool) {
		rollResults.clear();
		int valueTotal = 0;
		
		for (int i = 0; i < dicePool; i++) {
			int dieValue = RollDice();
			valueTotal += dieValue;
			rollResults.add(i, dieValue);
		}
		
		VerboseMode(rollResults);
		
		message += "Your total initiative is: " + (valueTotal + modifierInitiative);
	}

	private void BasicRoll(int dicePool, boolean inline) {
		rollResults.clear();
		
		isGlitch = false;
		isCritGlitch = false;
		
		countOne = 0;
		countMiss = 0;
		countHit = 0;
		
		String parseRoll;
		
		for (int i = 0; i < dicePool; i++) {
			rollResults.add(i, RollDice());
			
			if (flagPrimeRunner) {
				parseRoll = primeRollTable.get(rollResults.get(i));
			} else {
				parseRoll = basicRollTable.get(rollResults.get(i));
			}
			
			if (parseRoll.equals("One")) {
				countOne += 1;
			} else if (parseRoll.equals("Miss")) {
				countMiss += 1;
			} else if (parseRoll.equals("Hit")) {
				countHit += 1;
			}
		}
		
		if (inline) {
			message += "Dice Pool: " + dicePool + " | " 
					+ "Hits: " + countHit + " | "
					+ "Misses: " + countMiss + " | "
					+ "Ones: " + countOne + "\n";
		} else {
			message += "Hits: " + countHit + "\n"
					+ "Misses: " + countMiss + "\n"
					+ "Ones: " + countOne + "\n\n";
		}
		
		color = new Color(DiscordUtil.SlidingColorScale(countHit, dicePool), 255, DiscordUtil.SlidingColorScale(countHit, dicePool));
		
		
		if (flagVerbose || ruleAlwaysVerbose) {
			VerboseMode(rollResults);
		}
		
		GlitchCheck(dicePool);
		
		if (flagThresholdTest) {	
			ThresholdTest();	
		}
		
	}
	
	private void VerboseMode(ArrayList<Integer> rollResults) {
		message += "[";
		
		for (int i = 0; i < rollResults.size(); i++) {
			if (i==0) {
				message += rollResults.get(i);
			} else {
				message += ", " + rollResults.get(i);
			}
		}
		
		message += "] \n";
	}

	private void GlitchCheck(int dicePool) {
		float glitchBreakPoint = ((float) dicePool / 2);

		if ((ruleGreaterThanHalf && countOne > glitchBreakPoint) || (!ruleGreaterThanHalf && countOne >= glitchBreakPoint))  {
			if (countHit == 0) {
				message += "Critical Glitch! \n";
				isCritGlitch = true;
				color = Color.RED;
			} else {
				message += "Glitch! \n";
				isGlitch = true;
				color = Color.ORANGE;
			}
		} 
	}

	private void ThresholdTest() {
		String resultThreshold;
		
		if (countHit > modifierThreshold) {
			resultThreshold = "Success!";
			color = Color.GREEN;
		} else if (countHit == modifierThreshold) {
			resultThreshold = "Tie!";
			color = Color.GRAY;
		} else {
			resultThreshold = "Failure!";
			color = Color.RED;
		}
		
		message += "Automatic Test vs Threshold of " + modifierThreshold + ": " + resultThreshold + "\n";
	}

	private int RollDice() {
		int dieValue = (int) ((Math.random() * 6) + 1);
		return dieValue;
	}

	private boolean CheckDigit(String input) {

		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	private void CheckFlags(String[] secondaryArg) {
		// TODO Auto-generated method stub
		
		passModifierCheck = true;
		
		if (secondaryArg != null) {
			for (int i = 0; i < secondaryArg.length; i++) {
				
				if (secondaryArg[i].startsWith("-v") || secondaryArg[i].startsWith("-V")) {
					flagVerbose = true;
				} else if (secondaryArg[i].startsWith("-i") || secondaryArg[i].startsWith("-I")) {
					flagInitiative = true;
					modifierInitiative = IsolateDigit(secondaryArg, i);
				} else if (secondaryArg[i].startsWith("-x") || secondaryArg[i].startsWith("-X")) {
					flagExtended = true;
					modifierExtended = IsolateDigit(secondaryArg, i);
				} else if (secondaryArg[i].startsWith("-p") || secondaryArg[i].startsWith("-P")) {
					flagPrimeRunner = true;
				} else if (secondaryArg[i].startsWith("-t") || secondaryArg[i].startsWith("-T")) {
					flagThresholdTest = true;
					modifierThreshold= IsolateDigit(secondaryArg, i);
				}
			}
		}
	}

	private int IsolateDigit(String[] secondaryArg, int i) {
		String trimmedString = secondaryArg[i].replaceAll("[\\D]", "");
		
		try {
			return Integer.parseInt(trimmedString);
		} catch (Exception e) {
			if (i < secondaryArg.length) {
				
				try {
					return Integer.parseInt(secondaryArg[i+1]);
				} catch (Exception e2) {
					passModifierCheck = false;
					return 0;
				}
				
			} else {
				passModifierCheck = false;
				return 0;
			}
		}
		
		
	}
	
	

	@Override
	public void sendMessage(String message, MessageReceivedEvent event) {
		BotController.sendMessage(message, event);
	}
	
	public void sendMessage(EmbedBuilder builder, MessageReceivedEvent event) {
		BotController.sendMessage(builder, event);
	}

}
