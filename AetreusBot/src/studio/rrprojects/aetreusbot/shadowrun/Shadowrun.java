package studio.rrprojects.aetreusbot.shadowrun;

import java.util.ArrayList;
import java.util.HashMap;

import com.rocinrykor.aetreusbot.command.Roll;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.User;
import studio.rrprojects.aetreusbot.command.Command;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Shadowrun extends Command{
	HashMap<User, RollContainer> previousRollTable = new HashMap<>();

	@Override
	public String getName() {
		return "ShadowRun";
	}

	@Override
	public String getAlias() {
		return "SR";
	}

	@Override
	public String getHelpDescription() {
		return "Did you know that other every Tabletop RPG out there has their own rules for rolling dice? What dice they roll, how many they roll, and even what a roll represents? It is honestly quite staggering to think about sometimes. However, by the grace of my Master, I am now more familiar with the game of Shadowrun and have developed a specific rolling command for that game. Let’s go over the most basic of ground rules.\r\n\n" + 
				"In Shadowrun, the most commonly thrown dice is a D6, or more specifically, a pool of D6s. For most rolls we are concerned with how many hits the player has rolled, hits are the number of dice from the pool that have rolled as a 5 or a 6. The number of 1s a character has thrown in a set is also a factor for determine any negative outcomes while the rest are neutrals and are not often worried about.\r\n\n" + 
				"To deal with the rules when you use the command “&sr X” (with X being a number), I will roll that amount of dice and simply inform you of how many ones, neutrals, and hits were gotten from the set.\r\n" + 
				"";
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
	
	public Shadowrun() {
		BasicRoll.InitializeRollTables();
		FlagHandler.InitFlags();
	}
	
	public void executeMain(CommandContainer cmd) {
		String mainArg = cmd.MAIN_ARG;
		
		if (mainArg.equalsIgnoreCase("help")) {
			executeHelpFunction(cmd);
			return;
		} else if (mainArg.equalsIgnoreCase("reroll")){
			executeRerollFunction(cmd);
			return;
		}
		
		if (!CheckForValidDigit(mainArg)) {
			SendMessage("I'm sorry, but your main command does not include a valid command or define a dice pool. \n"
					+ "Please Try again.", cmd.DESTINATION);
			return;
		}
		
		RollContainer rollContainer = CreateBlankRollContainer();
		
		rollContainer = FlagHandler.ProcessFlags(rollContainer, cmd.SECONDARY_ARG);
		
		if (FlagHandler.CheckFlag("Prime")) {
			rollContainer.flagPrimeRunner = true;
		}
		
		rollContainer.dicePool = Integer.parseInt(mainArg);
		
		//Check for flags that require different rolling functions
		if (FlagHandler.CheckFlag("Initiative")) {
			String message = InitiativeRoller.Roll(rollContainer, cmd);
			SendMessage(message, cmd.DESTINATION);
			return;
		} else if (FlagHandler.CheckFlag("Extended")) {
			rollContainer = ExtendedRoller.Roll(rollContainer, cmd);
		} else {
			rollContainer = BasicRoll.Roll(rollContainer);
			if (FlagHandler.CheckFlag("Edge")) {
				rollContainer = BasicRoll.ExplodingSixesRoller(rollContainer);
			}
		}
		
		StoreResults(rollContainer, cmd);
		
		String message = BuildMessage(rollContainer, cmd);
		
		SendMessage(message, cmd.DESTINATION);
	}
		
	private void executeRerollFunction(CommandContainer cmd) {
		if (!previousRollTable.containsKey(cmd.AUTHOR)){
			String message = "Sorry, but you don't seem to have a revious roll for me to reroll";
			SendMessage(message, cmd.DESTINATION);
			return;
		} 
		
		RollContainer rollContainer = previousRollTable.get(cmd.AUTHOR);
		int tempDicePool = rollContainer.dicePool;
		rollContainer.dicePool = rollContainer.countMiss + rollContainer.countOne;
		System.out.println("Dice Pool: " + rollContainer.dicePool);
		rollContainer.countOne = 0;
		rollContainer.countMiss = 0;
		
		rollContainer = BasicRoll.Roll(rollContainer);
		
		rollContainer.dicePool = tempDicePool;
		
		String message = BuildMessage(rollContainer, cmd);
		SendMessage(message, cmd.DESTINATION);
	}

	private String BuildMessage(RollContainer rollContainer, CommandContainer cmd) {
		String message = String.format("Roll for %s: \n"
				+ "Hits: %s \n"
				+ "Misses: %s \n"
				+ "Ones: %s \n", cmd.AUTHOR.getName(), rollContainer.countHit, rollContainer.countMiss, rollContainer.countOne);
		
		if (FlagHandler.CheckFlag("Verbose")) {
			message += rollContainer.rollResults.toString();
		}
		
		return message;
	}

	private boolean CheckForValidDigit(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void StoreResults(RollContainer rollContainer, CommandContainer cmd) {
		if (previousRollTable.containsKey(cmd.AUTHOR)) {
			previousRollTable.replace(cmd.AUTHOR, rollContainer);
		} else {
			previousRollTable.put(cmd.AUTHOR, rollContainer);
		}
	}

	private void executeHelpFunction(CommandContainer cmd) {
		SendMessage(getHelpDescription(), cmd.DESTINATION);
	}

	private RollContainer CreateBlankRollContainer() {
		/*
		 * This will be used to store all the information about the rolls as the bot progresses along each step of the rolling function
		 * */
		return new RollContainer(0, 0, 0, 0, 0, null, false, false, false);
	}

	private void SendMessage(String message, User user) {
		NewMessage.send(message, user);
	}

	private void SendMessage(String message, Channel destination) {
		NewMessage.send(message, destination);
	}
	
	public static class RollContainer {
		public int dicePool;
		public int countOne;
		public int countMiss;
		public int countHit;
		public int explodingPool;
		public ArrayList<Integer> rollResults;
		public boolean isGlitch, isCritGlitch, flagPrimeRunner;
		
		public RollContainer(int dicePool, int countOne, int countMiss, int countHit, int explodingPool, ArrayList<Integer> rollResults, boolean isGlitch, boolean isCritGlitch, boolean flagPrimeRunner) {
			this.dicePool = dicePool;
			this.countOne = countOne;
			this.countMiss = countMiss;
			this.countHit = countHit;
			this.explodingPool = explodingPool;
			this.rollResults = rollResults;
			this.isGlitch = isGlitch;
			this.isCritGlitch = isCritGlitch;
			this.flagPrimeRunner = flagPrimeRunner;
		}
	}

}
