package com.rocinrykor.aetreusbot.command;

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

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;
import com.rocinrykor.aetreusbot.discord.DiscordUtil;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
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
	public String getHomeChannel() {
		return "rolling";
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
	public boolean isAdultResricted() {
		return false;
	}

	@Override
	public boolean deleteCallMessage() {
		return false;
	}
	
	// Message related variables
	String title, message;
	
	//All rules that will be handled by settings
	boolean ruleGreaterThanHalf = true;
	boolean ruleAlwaysVerbose = false;
	boolean ruleAutomaticGlitchPenalty = true;
	
	boolean userAlwaysVerbose = false;
	boolean userAlwaysPrime = false;
	
	//Flags for rolling
	Object[][] flagTable;
	boolean flagVerbose, flagInitiative, flagExtended, flagEdgePushTheLimit, flagPrimeRunner, flagThresholdTest, flagGremlins, flagVersus;
	
	//Modifiers for various flags
	int modifierInitiative, modifierExtended, modifierEdgeRating, modifierThreshold, modifierGremlins;
	
	//Booleans for error checking
	boolean passModifierCheck;
	
	//Glitch Checks
	boolean isGlitch, isCritGlitch;
	
	// Previous Roll Table
	HashMap<User, RollContainer> previousRollTable;
	
	// Roll Tables
	HashMap<Integer, String> basicRollTable = new HashMap<>();
	HashMap<Integer, String> primeRollTable = new HashMap<>();
	
	// Roll Tracking
	ArrayList<Integer> rollResults = new ArrayList<>();
	int countOne = 0;
	int countMiss = 0;
	int countHit = 0;
	
	//User Settings
	static List<Member> memberList = null;
	static HashMap<String, Boolean> userSettingsTable = new HashMap<>();
	
	
	Color color;
	
	public Shadowrun() {
		InitializeRollTables();
		InitializeFlagTables();
		InitializePreviousRollsTable();
	}
	
	//This is a function that I need to init after the bot is loaded so this function will be called after the onReady event
	public static void ShadowrunPost() {
		InitUserSettings();
	}
	
	private static void InitUserSettings() {
		File file;
		Properties prop;
		FileReader reader;
		
		String configFile;
		
		Guild guild = BotController.getGuild();
		
		memberList = guild.getMembersWithRoles(guild.getRolesByName("Tabletop RPG", true));
		
		//Initializes the file location of the config file set to C://User/USERNAME/Documents/Aetreus Bot/BotInfo.cfg"
		String fileName = "Shadowrun User Settings.cfg";
		String configDir = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Aetreus Bot";
		configFile = configDir + File.separator + fileName;
		
		//Initializes Properties and FileReader
		prop = new Properties();
		reader = null;
		
		//Checks if directories of config file exist and create them if necessary.
		File dir = new File(configDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		//Checks if the actual config files exists, creates if necessary.
		file = new File(configFile);
		
		if (!file.exists()) {
			try {
				file.createNewFile();
				PopulateNewConfig(configFile, prop, reader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			reader = new FileReader(configFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			prop.load(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Member member : memberList) {
			String userID = member.getUser().getId();
			
			userSettingsTable.put(userID + "-AlwaysVerbose", Boolean.valueOf(prop.getProperty(userID + "-AlwaysVerbose")));
			userSettingsTable.put(userID + "-AlwaysPrime", Boolean.valueOf(prop.getProperty(userID + "-AlwaysPrime")));
		}
		
	}

	private static void PopulateNewConfig(String configFile, Properties prop, FileReader reader) {
		//If file was created, sets the properties
		for (Member member : memberList) {
			String userID = member.getUser().getId();
			
			prop.setProperty(userID + "-AlwaysVerbose", "false");
			prop.setProperty(userID + "-AlwaysPrime", "false");
			
		}
		
		//Writes properties to the newly created file
		try {
			prop.store(new FileOutputStream(configFile), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("User Settings Loaded");
	}


	private void InitializePreviousRollsTable() {
		previousRollTable = new HashMap<>();
	}

	private void InitializeFlagTables() {
		flagTable = new Object[][] {
			{"Verbose", "v", "Show individual dice rolls", false},
			{"Initiative", "i", "Combat initiative", false, 0},
			{"Extended", "x", "Multiple dice rolls over an extended period", false, 0},
			{"Threshold", "t", "Automatic test against the threshold", false, 0},
			{"Prime", "p", "Prime Runner Quality (4s are hits)", false},
			{"Push", "l", "Spend Edge to add edge rating to roll", false, 0},
			{"Gremlins", "g", "Gremlins Quality, reduces dice pool for glitch calculation", false, 0},
			{"Versus", "vs", "Check agasint a second set of rolls, calculate net hits", false} //Not Implemented yet
		};
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
			CommandContainer cmd, MessageChannel channel) {
		
		color = Color.GRAY;
		
		if (primaryArg.equalsIgnoreCase("help")) {
			sendMessage(helpMessage(), channel);
			return;
		} else if (primaryArg.equalsIgnoreCase("settings")) {
			ManageSettings(secondaryArg, event);
			return;
		} else if (primaryArg.equalsIgnoreCase("reroll")) {
			SecondChance(event, channel);
			return;
		} else if (primaryArg.equalsIgnoreCase("limit")) {
			PushTheLimit(secondaryArg, event);
			return;
		}
		
		SetAllFlagsFalse();
		
		CheckFlags(secondaryArg);
		LoadUserSettings(event);
		OverwriteFlags();
		
		if (CheckDigit(primaryArg)) {
			BeginRoller(primaryArg, event, channel);
		} else {
			message = "ERROR: Unable to roll, dice pool not defined \n"
					+ "Please use \"&sr help\" for more info";
			sendMessage(message, channel);
		}
	}

	private void OverwriteFlags() {
		if (userAlwaysPrime) {
			flagPrimeRunner = true;
		}
		
		if(userAlwaysVerbose) {
			flagVerbose = true;
		}
	}

	private void LoadUserSettings(MessageReceivedEvent event) {
		String userID = event.getAuthor().getId();
		
		String keyVerbose = userID + "-AlwaysVerbose";
		String keyPrime = userID + "-AlwaysPrime";
		
		userAlwaysVerbose = userSettingsTable.get(keyVerbose);
		userAlwaysPrime = userSettingsTable.get(keyPrime);
	}

	private void BeginRoller(String primaryArg, MessageReceivedEvent event, MessageChannel channel) {
		message = "";
		
		EmbedBuilder builder = new EmbedBuilder();
		
		if (passModifierCheck) {
			
			int dicePool = 0;
			
			if(CheckDigit(primaryArg)) {
				dicePool = Integer.parseInt(primaryArg);
			} else {
				message = "Looks like there is no dice pool specified, please try again.";
				sendMessage(message, channel);
				return;
			}
			
			if (flagInitiative) {
				title = "InitiativeRoll | Initiative Modifer: " + modifierInitiative;
				InitiativeRoller(dicePool);
			} else if (flagExtended) {
				title = "Extended Roll | Extended Test Dice Pool: " + dicePool + " vs Threshold of " + modifierExtended;
				ExtendedRoller(dicePool, event);
			} else {
				title = "Success Roll | Dice Pool: " + dicePool;
				BasicRoll(dicePool, false, event);
			}
			
		} else {
			//fail
			message = "Oops, looks like there is an error with one of the flags, please check them and try again \n"
					+ "Please use \"&sr help\" for more info";
			sendMessage(message, channel);
			return;
		}
		
		builder.setColor(color);
		builder.addField(title, DiscordUtil.MessageToCode(message), true);
		
		sendMessage(builder, channel);
	}

	private void BasicRoll(int dicePool, boolean inline, MessageReceivedEvent event) {
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
		
		StoreResults(dicePool, event);
	}

	private Integer RollDice() {
		int dieValue = (int) ((Math.random() * 6) + 1);
		return dieValue;
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

	private void GlitchCheck(int dicePool) {
		
		float glitchBreakPoint = ((float) dicePool / 2);
		
		if (flagGremlins) {
			message += "Gremlins! Glitch threshold reduced by: " + modifierGremlins + "\n\n";
			glitchBreakPoint = ((float) dicePool / 2) - modifierGremlins;
		}

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

	private void ExtendedRoller(int dicePool, MessageReceivedEvent event) {
		int remainingDice = dicePool;
		int totalHits = 0;
		int attempts = 0;
		
		boolean glitchStopped = false;
		
		while (remainingDice > 0 && totalHits < modifierExtended) {
			BasicRoll(remainingDice, true, event);
			
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

	private void CheckFlags(String[] secondaryArg) {
		passModifierCheck = true;
		
		if (secondaryArg != null) {
			for (int i = 0; i < secondaryArg.length; i++) {
				
				for (int j = 0; j < flagTable.length; j++) {
					if (secondaryArg[i].equalsIgnoreCase(flagTable[j][0].toString()) || secondaryArg[i].equalsIgnoreCase(flagTable[j][1].toString())) {
						flagTable[j][3] = true;
						if (flagTable[j].length > 4) {
							flagTable[j][4] = IsolateDigit(secondaryArg, i);
						}
					}
				}
				
			}
		}
		
		flagVerbose	= (boolean) flagTable[0][3];
		
		flagInitiative = (boolean) flagTable[1][3];
		modifierInitiative = (int) flagTable[1][4];
		
		flagExtended = (boolean) flagTable[2][3];
		modifierExtended = (int) flagTable[2][4];
		
		flagThresholdTest = (boolean) flagTable[3][3];
		modifierThreshold = (int) flagTable[3][4];
		
		flagPrimeRunner = (boolean) flagTable[4][3];
		
		flagEdgePushTheLimit = (boolean) flagTable[5][3];
		modifierEdgeRating = (int) flagTable[5][4];
		
		flagGremlins = (boolean) flagTable[6][3];
		modifierGremlins = (int) flagTable[6][4];
		
		flagVersus = (boolean) flagTable[7][3];
		
	}

	private Object IsolateDigit(String[] secondaryArg, int i) {
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

	private boolean CheckDigit(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void SetAllFlagsFalse() {
		for (int i = 0; i < flagTable.length; i++) {
			flagTable[i][3] = false;
		}
		
		userAlwaysPrime = false;
		userAlwaysVerbose = false;
	}

	private void PushTheLimit(String[] secondaryArg, MessageReceivedEvent event) {
	}

	private void SecondChance(MessageReceivedEvent event, MessageChannel channel) {
		EmbedBuilder builder = new EmbedBuilder();
		
		title = "Previous Roll for " + event.getAuthor().getName() + ": ";
		
		User user = event.getAuthor();
		if (!previousRollTable.containsKey(user)) {
			message = "Sorry, looks like you dont have a previous roll for Second Chance to apply.";
			sendMessage(message, channel);
			return;
		}
		
		RollContainer previous = previousRollTable.get(user);
		
		message = "Hits: " + previous.countHit + "\n"
				+ "Misses: " + previous.countMiss + "\n"
				+ "Ones: " + previous.countOne + "\n\n";
		
		VerboseMode(previous.rollResults);
		
		if (previous.isGlitch) {
			message += "Glitch! \n";
		} else if (previous.isCritGlitch) {
			message += "Critical Glitch! \n";
		}
		
		int dicePool = previous.countMiss + previous.countOne;
		
		builder.addBlankField(true);
		builder.addField(title, DiscordUtil.MessageToCode(message), true);
		
		title = "New Roll Results| Dice Pool: " + dicePool;
		
		message = "";
		
		rollResults.clear();
		
		countOne = 0;
		countMiss = 0;
		countHit = previous.countHit;
		
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
		
		message += "Hits: " + countHit + "\n"
				+ "Misses: " + countMiss + "\n"
				+ "Ones: " + countOne + "\n\n";
		
		VerboseMode(rollResults);
		
		GlitchCheck(dicePool);
		
		builder.addField(title, DiscordUtil.MessageToCode(message), true);
		
		sendMessage(builder, channel);
	}

	private void ManageSettings(String[] secondaryArg, MessageReceivedEvent event) {
		CheckFlags(secondaryArg);
		String userID = event.getAuthor().getId();
		
		if (flagVerbose) {
			InvertSetting(userID, "-AlwaysVerbose");
		}
		
		if (flagPrimeRunner) {
			InvertSetting(userID, "-AlwaysPrime");
		}
		
	}
	
	private void InvertSetting(String userID, String string) {
		String key = userID + string;
		boolean temp = userSettingsTable.get(key);
		
		userSettingsTable.replace(key, !temp);
	}

	private void StoreResults(int dicePool, MessageReceivedEvent event) {
		User user = event.getAuthor();
		
		RollContainer currentRoll = new RollContainer(dicePool, countOne, countMiss, countHit, rollResults, isGlitch, isCritGlitch, flagPrimeRunner);
		
		if(previousRollTable.containsKey(user)) {
			previousRollTable.replace(user, currentRoll);
		} else {
			previousRollTable.put(user, currentRoll);
		}
	}
	
	public static class RollContainer {
		int dicePool, countOne, countMiss, countHit;
		ArrayList<Integer> rollResults;
		boolean isGlitch, isCritGlitch, flagPrimeRunner;
		
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
	
	public void sendMessage(EmbedBuilder builder, MessageChannel channel) {
		BotController.sendMessage(builder, channel);
	}

	public void sendMessage(String message, MessageChannel channel) {
		BotController.sendMessage(message, channel);
	}
}
