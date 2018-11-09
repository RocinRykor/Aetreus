package com.rocinrykor.aetreusbot.command;

import java.util.HashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;
import com.rocinrykor.aetreusbot.utils.ArgCountChecker;
import com.rocinrykor.aetreusbot.utils.RollTracker;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Roll extends Command {

	HashMap<String, String> argMap = new HashMap<>();
	public String argValue;
	boolean isModded = false;
	
	ScriptEngineManager manager = new ScriptEngineManager();
	ScriptEngine engine = manager.getEngineByName("js");
	
	@Override
	public String getName() {
		return "Roll";
	}
	
	@Override
	public String getAlias() {
		return "r";
	}

	@Override
	public String getDescription() {
		return "Rolls a specified number of dice";
	}
	
	@Override
	public String helpMessage() {
		return "Have you ever found yourself with the need to roll a die or even multiple dice, and yet your dice are not in reach or anywhere to be found? Well, by commanding me to do so I will roll dice on your behalf from a special bag given to me by my master. Within this bag are countless dice of all shapes and sizes, meaning, I can roll any number of dice that you want each with as many sides that you want.\r\n" + 
				"\r\n" + 
				"By default, I will roll a single 20-sided die.\r\n" + 
				"If you wish me to roll a certain number of die or a certain size, please specify the amount using the following notation after the roll command: “(X)d(Y)”\r\n" + 
				"For example, “&roll 2d6” will roll 2, 6-sided dice.\r\n" + 
				"You can also follow the command with any modifiers such as +3 or -1 and I will incorporate that information into the rolls.\r\n" + 
				"\r\n" + 
				"Please note, that while I am loyal to my master I assure you that all my rolls are completely fair for everyone.\r\n" + 
				"In fact, if you would like to see a list of your rolls, then just type \"&roll report\" at any time. \r\n"
				+ "";
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

	@Override
	public void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event,
			CommandContainer cmd) {
		
		int totalValue = 0;
		int dieValue;
		
		int diceNumber;
		int dieSides;
		
		int moddedValue = 0;
		isModded = false;
		

		String finalMessage = "";
		String userID = event.getAuthor().getId();
		
		if (primaryArg.equalsIgnoreCase("help")) {
			sendMessage(helpMessage(), event);
			return;
		} else if (primaryArg.equalsIgnoreCase("report")) {
			ReportRolls(event);
			return;
		} 
		
		if (primaryArg.equalsIgnoreCase("none") || CheckModifiers(primaryArg)) {
			dieSides = 20;
			dieValue = RollDice(dieSides);
			totalValue += dieValue;
			finalMessage += "Result: " + totalValue + "\n";
			
			if (dieSides == 20) {
				RollTracker.TrackRoll(userID, dieValue);
			}
			
			if(CheckModifiers(primaryArg)) {
				moddedValue += ProcessModifier(primaryArg);
			}
			
		} else if (Character.isDigit(primaryArg.charAt(0))) {
			
			String primeBreak = primaryArg.replace("d", " ");
			String[] primeSplit = primeBreak.split("[^0-9]");
			
			if (ArgCountChecker.argChecker(primeSplit.length, 2)) {
				diceNumber = Integer.parseInt(primeSplit[0]);
				dieSides = Integer.parseInt(primeSplit[1]);
				
				finalMessage += "Rolling " + diceNumber +  ", " + dieSides + "-sided dice: \n";
				
				finalMessage += "Results: {";
				for (int i = 0; i < diceNumber; i++) {
					dieValue = RollDice(dieSides);
					totalValue += dieValue;
					if (i==0) {
						finalMessage += dieValue;
					} else {
						finalMessage += ", " + dieValue;
					}
					
					if (dieSides == 20) {
						RollTracker.TrackRoll(userID, dieValue);
					}
				}
				
				finalMessage += "} \n"
					+ "Total Value: " + totalValue + "\n";
				
			} 
		} else {
			finalMessage += "Error!";
		}
				
		if (secondaryArg != null) {
			if (ArgCountChecker.argChecker(secondaryArg.length, 1)) {
				for (int i = 0; i < secondaryArg.length; i++) {
					if (CheckModifiers(secondaryArg[i])) {
						moddedValue += ProcessModifier(secondaryArg[i]);
					}
				}
			}	
		}
		
		if (isModded) {
			finalMessage += "Modifier: " + moddedValue + "\n"
				+ "Final Result: " + (moddedValue + totalValue);
		}
		
		sendMessage(finalMessage, event);
	}
	
	private void ReportRolls(MessageReceivedEvent event) {
		String userID = event.getAuthor().getId();
		
		String message = RollTracker.ReportStats(userID, event);
		
		sendMessage(message, event);
	}

	private int ProcessModifier(String input) {
		String calcString = "0" + input;
		
		int result;
		
		try {
			result = (int) engine.eval(calcString);
			return result;
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	private boolean CheckModifiers(String input) {
		if (input.startsWith("+") || input.startsWith("-")) {
			isModded = true;
			return true;
		} else {
			return false;
		}
		
	}

	private int RollDice(int sides) {
		int dieValue = (int) ((Math.random() * sides) + 1);
		return dieValue;
	}

	@Override
	public void sendMessage(String message, MessageReceivedEvent event) {
		BotController.sendMessage(message, event);
	}

}
