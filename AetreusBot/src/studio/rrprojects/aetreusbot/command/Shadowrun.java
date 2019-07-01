package studio.rrprojects.aetreusbot.command;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.rocinrykor.aetreusbot.command.Shadowrun.RollContainer;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import studio.rrprojects.aetreusbot.Controller;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.shadowrun.BasicRoll;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Shadowrun extends Command{

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
	}
	
	public void executeMain(CommandContainer cmd) {
		String mainArg = cmd.MAIN_ARG;
		
		if (mainArg.equalsIgnoreCase("help")) {
			executeHelpFunction(cmd);
			return;
		}
		
		if (!CheckForValidDigit(mainArg)) {
			SendMessage("I'm sorry, but your main command does not include a valid command or define a dice pool. \n"
					+ "Please Try again.", cmd.DESTINATION);
			return;
		}
		
		RollContainer rollContainer = CreateBlankRollContainer();
		
		rollContainer.dicePool = Integer.parseInt(mainArg);
		
		rollContainer = BasicRoll.Roll(rollContainer);
		
		String message = String.format("Roll for %s: \n"
				+ "Ones: %s \n"
				+ "Misses: %s \n"
				+ "Hits: %s", cmd.AUTHOR.getName(), rollContainer.countOne, rollContainer.countMiss, rollContainer.countHit);
		
		SendMessage(message, cmd.DESTINATION);
	}
		
	private boolean CheckForValidDigit(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void executeHelpFunction(CommandContainer cmd) {
		SendMessage(getHelpDescription(), cmd.DESTINATION);
	}

	private RollContainer CreateBlankRollContainer() {
		/*
		 * This will be used to store all the information about the rolls as the bot progresses along each step of the rolling function
		 * */
		return new RollContainer(0, 0, 0, 0, null, false, false, false);
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
		public ArrayList<Integer> rollResults;
		public boolean isGlitch, isCritGlitch, flagPrimeRunner;
		
		public RollContainer(int dicePool, int countOne, int countMiss, int countHit, ArrayList<Integer> rollResults, boolean isGlitch, boolean isCritGlitch, boolean flagPrimeRunner) {
			this.dicePool = dicePool;
			this.countOne = countOne;
			this.countMiss = countMiss;
			this.countHit = countHit;
			this.rollResults = rollResults;
			this.isGlitch = isGlitch;
			this.isCritGlitch = isCritGlitch;
			this.flagPrimeRunner = flagPrimeRunner;
		}
	}

}
