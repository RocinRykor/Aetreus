package studio.rrprojects.aetreusbot.command;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.User;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.Attributes;
import studio.rrprojects.aetreusbot.dungeonsanddragons.Skills;
import studio.rrprojects.aetreusbot.utils.ArgCountChecker;
import studio.rrprojects.aetreusbot.utils.MessageTools;
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
		return "Rolls a specified number of dice and calculates any given modifier";
				/* "Have you ever found yourself with the need to roll a die or even multiple dice, and yet your dice are not in reach or anywhere to be found? Well, by commanding me to do so I will roll dice on your behalf from a special bag given to me by my master. Within this bag are countless dice of all shapes and sizes, meaning, I can roll any number of dice that you want each with as many sides that you want.\r\n" + 
				"\r\n" + 
				"By default, I will roll a single 20-sided die.\r\n" + 
				"If you wish me to roll a certain number of die or a certain size, please specify the amount using the following notation after the roll command: “(X)d(Y)”\r\n" + 
				"For example, “&roll 2d6” will roll 2, 6-sided dice.\r\n" + 
				"You can also follow the command with any modifiers such as +3 or -1 and I will incorporate that information into the rolls.\r\n" + 
				"\r\n" + 
				"Please note, that while I am loyal to my master I assure you that all my rolls are completely fair for everyone.\r\n" + 
				"In fact, if you would like to see a list of your rolls, then just type \"&roll report\" at any time. \r\n"
				+ "";
				*/
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
	public boolean isAdultRestricted() {
		return false;
	}

	@Override
	public boolean deleteCallMessage() {
		return false;
	}
	
	public void executeMain(CommandContainer cmd) {
		
		RollContainer rollContainer = CreateBlankRollContainer();
		
		User user = cmd.AUTHOR;
		String mainArg = cmd.MAIN_ARG;
		
		if (mainArg.equalsIgnoreCase("help")) {
			SendMessage(getHelpDescription(), cmd.DESTINATION, cmd.AUTHOR);
			return;
		} 

		rollContainer = defaultRoll(rollContainer);
		
		/*
		 * Step 1: Check mainArg for a number, else check for stat or skill and import from character sheet as needed
		 * Step 1a: Roll mainDicePool
		 * Step 2: Check secondaryArgs for Advantage or Disadvantage and calculate as needed
		 * Step 3: Check secondaryArgs for additional roll modifiers, execute as needed
		 * Step 4: Check secondaryArgsd for flat modifiers, calculate total as needed
		 * Step 5: Build final results
		 * 
		 * */
		
		if (!mainArg.equalsIgnoreCase("")) {
			//Steam 1: Check mainArg for a number, else check for stat or skill and import from character sheet as needed
			
			if (StartsWithDigit(mainArg)) {
				rollContainer = mainDicePoolBreakdown(mainArg, rollContainer);
			} else if (StartsWithLetter(mainArg)) {
				/*
				 * Import from character sheet
				 * */
				rollContainer = CharacterSheetImport(cmd, rollContainer);
			} 
		}
		
		SecondaryPhase(rollContainer, cmd);
		
	}
	
	public void SecondaryPhase(RollContainer rollContainer, CommandContainer cmd) {
		SecondaryPhase(rollContainer, cmd, false);
	}
	
	public RollContainer SecondaryPhase(RollContainer rollContainer, CommandContainer cmd, Boolean isReturned) {
		//Takes the processed main argument and begins resolving any secondary arguments before finally building the completed roll.
		//This is its own function so that the attack and damage functions can use the same process
		
		String finalMessage = "";
		String[] secondaryArg = cmd.SECONDARY_ARG; 
		
		//Step 1a: Roll mainDicePool
		rollContainer.mainRollResults = StartRoller(rollContainer.mainDicePool, rollContainer.mainDiceSides);
		
		//Step 2: Check secondaryArgs for Advantage or Disadvantage and calculate as needed
		rollContainer = AdvantageHandler(rollContainer, secondaryArg);
		
		//Step 3: Check secondaryArgs for additional roll modifiers and flat modifiers
		rollContainer = ModChecker(rollContainer, secondaryArg);
		
		//Step 4: Resolve any roll modifiers
		rollContainer = ResolveRollModifiers(rollContainer);
		
		if (isReturned) {
			return rollContainer;
		}
		
		//Step 5: Build Results
		finalMessage = BuildResults(rollContainer, cmd);
		
		
		SendMessage(finalMessage, cmd.DESTINATION, cmd.AUTHOR);
		return null;
	}

	private RollContainer CharacterSheetImport(CommandContainer cmd, RollContainer rollContainer) {
		if(cmd.AUTHOR.isFake()) {
			return rollContainer;
		}
		
		String input = cmd.MAIN_ARG;
		String searchString = null;
		
		searchString = Attributes.getAttribute(input);
		
		if (searchString != null) {
			rollContainer = Attributes.ProcessRoll(searchString, cmd, rollContainer);
		} else {
			searchString = Skills.GetSkill(input);
			if (searchString != null) {
				rollContainer = Skills.ProcessRoll(searchString, cmd, rollContainer);
			}
		}
		
		System.out.println(searchString);
		
		
		return rollContainer;
	}

	private RollContainer ResolveRollModifiers(RollContainer rollContainer) {
		if (rollContainer.modDicePool != null) {
			for (int i = 0; i < rollContainer.modDicePool.size(); i++) {
				int dicePool = rollContainer.modDicePool.get(i);
				int dieSides = rollContainer.modDiceSide.get(i);
				rollContainer.modRollResults.addAll(StartRoller(dicePool, dieSides));
			}
		}
		
		return rollContainer;
	}

	private RollContainer ModChecker(RollContainer rollContainer, String[] secondaryArgs) {
		if (secondaryArgs != null) {
			if (ArgCountChecker.argChecker(secondaryArgs.length, 1)) {
				for (int i = 0; i < secondaryArgs.length; i++) {
					if (CheckModifiers(secondaryArgs[i])) {
						rollContainer = ProcessModifier(secondaryArgs, i, rollContainer);
					}
					
					if (secondaryArgs[i].toLowerCase().contains("note")) {
						rollContainer.showNotes = true;
					}
				}
			}
		}
		
		return rollContainer;
	}

	private RollContainer AdvantageHandler(RollContainer rollContainer, String[] secondaryArg) {
		int advantageValue = CheckForAdvantage(secondaryArg, rollContainer);
		
		if (advantageValue != 0) {
			rollContainer = RollForAdvantage(rollContainer, advantageValue);
		}
		
		return rollContainer;
	}

	private RollContainer RollForAdvantage(RollContainer rollContainer, int advantageValue) {
		ArrayList<Integer> initialRoll = rollContainer.mainRollResults;
		ArrayList<Integer> newRoll = StartRoller(rollContainer.mainDicePool, rollContainer.mainDiceSides);
		
		rollContainer.notes.add("(Advantage Handler) Initial Set: " + initialRoll.toString() + " | Reroll Set: " + newRoll.toString());
		
		ArrayList<Integer> finalList = new ArrayList<>();
		
		for (int i = 0; i < initialRoll.size(); i++) {
			int oldValue = initialRoll.get(i);
			int newValue = newRoll.get(i);
			int result = Compare(oldValue, newValue, advantageValue);
			finalList.add(result);
		}
		
		
		
		rollContainer.mainRollResults = finalList;
		
		return rollContainer;
	}

	private int Compare(int oldValue, int newValue, int advantageValue) {
		if (advantageValue < 0) {
			return Math.min(oldValue, newValue);		
		} else {
			return Math.max(oldValue, newValue);
		}
	}

	private int CheckForAdvantage(String[] input, RollContainer rollContainer) {
		int tmpValue = 0;
		
		if (input != null) {
			for (int i = 0; i < input.length; i++) {
				if (input[i].contains("dis")) {
					tmpValue -= 1;
				} else if (input[i].contains("adv")) {
					tmpValue += 1;
				}
			}
		}
		
		rollContainer.advValue += tmpValue;
		
		return tmpValue;
	}

	private boolean StartsWithLetter(String input) {
		return Character.isAlphabetic(input.charAt(0));
	}

	private boolean StartsWithDigit(String input) {
		return Character.isDigit(input.charAt(0));
	}

	public RollContainer ProcessModifier(String[] secondaryArgs, int index, RollContainer rollContainer) {
		String temp = secondaryArgs[index];
		
		if (secondaryArgs[index].length() == 1) {
			if (index + 1 < secondaryArgs.length) {
				temp += secondaryArgs[index+1];
			}
		}
		
		
		if(temp.contains("d")) {
			//Split the modifier back up
			String tmpPrefix = temp.substring(0, 1);
			String tmpSuffix = temp.substring(1);

			String[] splitArray = new String[2];
			
			String tmp = tmpSuffix.replace("d", " ");
			System.out.println(tmp);
			splitArray = tmp.split(" ");
			
			System.out.println(splitArray[0]);
			
			int modDicePool = Integer.parseInt(splitArray[0]);
			int modDiceSides = Integer.parseInt(splitArray[1]);
			
			rollContainer.modDicePool.add(modDicePool);
			rollContainer.modDiceSide.add(modDiceSides);
			
			return rollContainer;
		} 
		
		String calcString = "0" + temp;

		int result = 0;
		
		try {
			result = (int) engine.eval(calcString);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
		rollContainer.modValue += result;
		
		return rollContainer;
	}

	private boolean CheckModifiers(String input) {
		if (input.startsWith("+") || input.startsWith("-")) {
			return true;
		} else {
			return false;
		}	
	}

	private RollContainer defaultRoll(RollContainer rollContainer) {
		rollContainer.mainDicePool = 1;
		rollContainer.mainDiceSides = 20;
		
		rollContainer.title = "Rolling 1, 20-Sided dice";
		
		return rollContainer;
	}

	public RollContainer mainDicePoolBreakdown(String input, RollContainer rollContainer) {
		String[] strArray;
		
		if (input.contains("d")) {
			String temp = input.replace("d", " ");
			strArray = temp.split(" ");
			rollContainer.mainDicePool = (Integer)Integer.parseInt(strArray[0]);
			rollContainer.mainDiceSides = (Integer)Integer.parseInt(strArray[1]);
		} else {
			rollContainer.mainDicePool = 1;
			rollContainer.mainDiceSides = (Integer)Integer.parseInt(input);
		}
		
		rollContainer.title = "Rolling " + rollContainer.mainDicePool + ", " + rollContainer.mainDiceSides + "-Sided dice";
		
		return rollContainer;
	}

	public ArrayList<Integer> StartRoller(int dicePool, int dieSides) {
		
		ArrayList<Integer> rollResults = new ArrayList<>();
		
		for (int i = 0; i < dicePool; i++) {
			rollResults.add(RollDice(dieSides));
		}
		
		return rollResults;
	}

	public String BuildResults(RollContainer rollContainer, CommandContainer cmd) {
		int mainDiceResult = 0;
		int modDiceResult = 0;
		int modValue = rollContainer.modValue;
		
		for (int i = 0; i < rollContainer.mainRollResults.size(); i++) {
			mainDiceResult += rollContainer.mainRollResults.get(i);
		}
		
		if (rollContainer.modRollResults != null) {
			for (int i = 0; i < rollContainer.modRollResults.size(); i++) {
				modDiceResult += rollContainer.modRollResults.get(i);
			}
		}
		
		String advMessage = "";
		if (rollContainer.advValue < 0) {
			advMessage = " with disadvantage";
		} else if (rollContainer.advValue > 0) {
			advMessage = " with advantage";
		}
		
		int finalResult = mainDiceResult + modDiceResult + modValue;
		String message = "Roll by: " + cmd.AUTHOR.getName() + " \n"
				+ rollContainer.title + advMessage + "\n\n"
				+ "==BREAKDOWN==\n"
				+ "Main Rolls: " + rollContainer.mainRollResults.toString() + "\n"
				+ "Total Main Roll Value: " + mainDiceResult + "\n\n"
				+ "Mod Rolls: " + rollContainer.modRollResults.toString() + "\n"
				+ "Total Mod Roll Value: " + modDiceResult + "\n\n"
				+ "Flat Mod Value: " + modValue + "\n";
		
		if (rollContainer.showNotes) {
			message += "== NOTES ==\n";
			for (String note : rollContainer.notes) {
				message += note + "\n";
			}
		}
		
		message += "\nFinal Results : " + finalResult + " \n";
		
		message = MessageTools.BlockText(message, "css");
		
		return message; //FIX
	}

	private void SendMessage(String message, Channel DESTINATION, User user) {
		NewMessage.send(message, DESTINATION, user);
	}
	
	private int RollDice(int sides) {
		int dieValue = (int) ((Math.random() * sides) + 1);
		return dieValue;
	}
	
	public RollContainer CreateBlankRollContainer() {
		/*
		 * This will be used to store all the information about the rolls as the bot progresses along each step of the rolling function
		 * */
		return new RollContainer("", 1, 20, null, null, null, null, 0, 0, false, new ArrayList<>());
	}

	public static class RollContainer {
		public String title;
		
		public int mainDicePool;
		public int mainDiceSides;
		public ArrayList<Integer> mainRollResults;
		
		public ArrayList<Integer> modDicePool;
		public ArrayList<Integer> modDiceSide;
		public ArrayList<Integer> modRollResults;
		
		public int modValue;
		
		public int advValue;
		
		public Boolean showNotes;
		public ArrayList<String> notes;
		
		public RollContainer(String title, int mainDicePool, int mainDiceSides, ArrayList<Integer> mainRollResults, 
				ArrayList<Integer> modDicePool, ArrayList<Integer> modDiceSide, ArrayList<Integer> modRollResults, int modValue, int advValue, Boolean showNotes, ArrayList<String> notes) {
			this.title = title;
			
			this.mainDicePool = mainDicePool;
			this.mainDiceSides = mainDiceSides;
			this.mainRollResults = new ArrayList<>();
			
			this.modDicePool = new ArrayList<>();
			this.modDiceSide = new ArrayList<>();
			this.modRollResults = new ArrayList<>();
			
			this.modValue = modValue;
			this.advValue = advValue;
			
			this.showNotes = showNotes;
			this.notes = notes;
		}
	}

}
