package studio.rrprojects.aetreusbot.command;

import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.User;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.utils.ArgCountChecker;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Roll extends Command {
	
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
	public String getHelpDescription() {
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
	public String getHomeChannel() {
		return "rolling";
	}

	@Override
	public boolean isChannelRestricted() {
		return true;
	}

	@Override
	public boolean isAdminOnly() {
		return false;
	}

	@Override
	public boolean deleteCallMessage() {
		return false;
	}
	
	public void executeMain(CommandContainer cmd) {
		
		RollContainer rollContainer = CreateBlankRollContainer();
		
		String finalMessage = "";
		
		User user = cmd.AUTHOR;
		String mainArg = cmd.MAIN_ARG;
		String[] secondaryArg = cmd.SECONDARY_ARG; 
		
		if (mainArg.equalsIgnoreCase("help")) {
			SendMessage(getHelpDescription(), cmd.DESTINATION);
			return;
		} else if (mainArg.equalsIgnoreCase("report")) {
			//ReportRolls(event, channel);
			return;
		} 

		rollContainer = defaultRoll(rollContainer);
		
		if (!mainArg.equalsIgnoreCase("")) {
			/*
			 * Begin by looking at mainArg, does it start with a number or a letter
			 * 
			 * If number attempt to determine if number by itself or in standard format (1d6)
			 * roll as needed
			 * 
			 * if letter, being character sheet import, roll as needed
			 * */
			
			if (Character.isDigit(mainArg.charAt(0))) {
				rollContainer = breakApart(mainArg, rollContainer);
			}
			
		}
		
		rollContainer = StartRoller(rollContainer);
		
		rollContainer = updateModValue(cmd.SECONDARY_ARG, rollContainer);
		
		String rollResults = BuildResults(rollContainer);
		
		SendMessage(rollResults, cmd.DESTINATION);
	}
	
	private RollContainer updateModValue(String[] secondaryArgs, RollContainer rollContainer) {
		int modValue = rollContainer.modValue;
		
		if (secondaryArgs != null) {
			if (ArgCountChecker.argChecker(secondaryArgs.length, 1)) {
				for (int i = 0; i < secondaryArgs.length; i++) {
					if (CheckModifiers(secondaryArgs[i])) {
						modValue += ProcessModifier(secondaryArgs, i);
					}
				}
			}	
		}
		
		rollContainer.modValue = modValue;
		
		return rollContainer;
	}

	public int ProcessModifier(String[] secondaryArgs, int index) {
		String temp = secondaryArgs[index];
		
		if (secondaryArgs[index].length() == 1) {
			if (index + 1 < secondaryArgs.length) {
				temp += secondaryArgs[index+1];
			}
		}
		
		String calcString = "0" + temp;
		
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
			return true;
		} else {
			return false;
		}	
	}

	private RollContainer defaultRoll(RollContainer rollContainer) {
		rollContainer.dicePool = 1;
		rollContainer.dieSides = 20;
		
		return rollContainer;
	}

	private RollContainer breakApart(String mainArg, RollContainer rollContainer) {
		String[] strArray;
		
		if (mainArg.contains("d")) {
			String temp = mainArg.replace("d", " ");
			strArray = temp.split(" ");
			rollContainer.dicePool = (Integer)Integer.parseInt(strArray[0]);
			rollContainer.dieSides = (Integer)Integer.parseInt(strArray[1]);
		} else {
			rollContainer.dicePool = 1;
			rollContainer.dieSides = (Integer)Integer.parseInt(mainArg);
		}
		
		return rollContainer;
	}

	public RollContainer StartRoller(RollContainer rollContainer) {
		int dicePool = rollContainer.dicePool;
		int dieSides = rollContainer.dieSides;
		
		ArrayList<Integer> rollResults = new ArrayList<>();
		
		for (int i = 0; i < dicePool; i++) {
			rollResults.add(RollDice(dieSides));
		}
		
		rollContainer.rollResults = rollResults;
		
		return rollContainer;
	}

	public String BuildResults(RollContainer rollContainer) {
		int dicePool = rollContainer.dicePool;
		int dieSides = rollContainer.dieSides;
		
		int totalValue = 0;
		
		for (int i = 0; i < rollContainer.rollResults.size(); i++) {
			totalValue += rollContainer.rollResults.get(i);
		}
		
		String verboseMode = rollContainer.rollResults.toString();
		
		
		String result = "Rolling " + dicePool + ", " + dieSides + "-sided Dice \n"
				+ "Total Value: "  + totalValue + "\n";
		
		if (rollContainer.modValue != 0) {
			int modValue = rollContainer.modValue;
			result += "\nModifier: " + modValue + "\n"
					+ "Total Value: " + (modValue + totalValue) + "\n";
		}
		
		result += "\n" + verboseMode;
		
		return result;
	}

	private void SendMessage(String message, Channel DESTINATION) {
		NewMessage.send(message, DESTINATION);
	}
	
	private int RollDice(int sides) {
		int dieValue = (int) ((Math.random() * sides) + 1);
		return dieValue;
	}
	
	public RollContainer CreateBlankRollContainer() {
		/*
		 * This will be used to store all the information about the rolls as the bot progresses along each step of the rolling function
		 * */
		return new RollContainer(0, 0, 0, null);
	}

	public static class RollContainer {
		public int dicePool;
		public int dieSides;
		public int modValue;
		public ArrayList<Integer> rollResults;
		
		public RollContainer(int dicePool, int dieSides, int modValue, ArrayList<Integer> rollResults) {
			this.dicePool = dicePool;
			this.dieSides = dieSides;
			this.modValue = modValue;
			this.rollResults = rollResults;
		}
	}

}
