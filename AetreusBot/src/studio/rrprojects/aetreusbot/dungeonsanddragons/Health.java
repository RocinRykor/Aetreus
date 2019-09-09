package studio.rrprojects.aetreusbot.dungeonsanddragons;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import studio.rrprojects.aetreusbot.command.Command;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.command.Roll;
import studio.rrprojects.aetreusbot.command.Roll.RollContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterLoader;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.CharacterContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.StatInfoContainer;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Health extends Command{

	@Override
	public String getName() {
		return "Health";
	}

	@Override
	public String getAlias() {
		return "Health";
	}

	@Override
	public String getHelpDescription() {
		return "D&D - Manages characters HP and Death Saving Throw";
	}

	@Override
	public String getHomeChannel() {
		return "BotTesting";
	}

	@Override
	public boolean isChannelRestricted() {
		return false;
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
		if (cmd.AUTHOR.isFake()) {
			return;
		}
		
		CharacterContainer character = CharacterLoader.GetPlayerData(cmd.AUTHOR.getName());
		
		HashMap<String, Runnable> subCommands = new HashMap<>();
		subCommands.put("help", () -> HelpCommand(cmd, subCommands));
		subCommands.put("add", () -> AddHealth(cmd, character));
		subCommands.put("sub", () -> SubHealth(cmd, character));
		subCommands.put("save", () -> HealthSave(cmd, character));
		//subCommands.put("set", () -> HealthSet(cmd, character));
		
		if (subCommands.containsKey(cmd.MAIN_ARG.toLowerCase())) {
			subCommands.get(cmd.MAIN_ARG.toLowerCase()).run();
			return;
		}
		
		String message = "";
		
		ListHealth(character, cmd, message);
	}
	
	private void AddHealth(CommandContainer cmd, CharacterContainer character) {
		/*
		 * Check secondary args for either flat value or roll formula
		 * Apply modifiers as needed
		 * Ensure max Health does not go over max HP listed
		 * -> Look for tmp argument and if present add to tmpHP instead 
		 * */
		
		Boolean isTmpHP = false;
		
		if (!(cmd.SECONDARY_ARG.length > 0)) {
			SendMessage("I'm sorry I need a vlaue to add to your hp pool", cmd.DESTINATION);
			return;
		}
		
		for (String argument : cmd.SECONDARY_ARG) {
			if (argument.equalsIgnoreCase("temp") || argument.equalsIgnoreCase("tmp") ) {
				isTmpHP = true;
			}
		}
		
		String healthMod = cmd.SECONDARY_ARG[0];
		if (Character.isDigit(healthMod.charAt(0)) && healthMod.contains("d")) {
			Roll roll = new Roll();
			RollContainer healthRoll = roll.CreateBlankRollContainer();
			healthRoll = roll.mainDicePoolBreakdown(healthMod, healthRoll);
			healthRoll = roll.SecondaryPhase(healthRoll, cmd, true);
			int healthModValue = buildResult(healthRoll);
			ChangeHealth (healthModValue, character, cmd, isTmpHP);
		} else if (Character.isDigit(healthMod.charAt(0))) {
			int healthModValue = Integer.parseInt(healthMod);
			ChangeHealth (healthModValue, character, cmd, isTmpHP);
		} else {
			SendMessage("I'm sorry I don't understand that value", cmd.DESTINATION);
		}
	}

	private int buildResult(RollContainer healthRoll) {
		int mainDiceResult = 0;
		int modDiceResult = 0;
		int modValue = healthRoll.modValue;
		
		for (int i = 0; i < healthRoll.mainRollResults.size(); i++) {
			mainDiceResult += healthRoll.mainRollResults.get(i);
		}
		
		if (healthRoll.modRollResults != null) {
			for (int i = 0; i < healthRoll.modRollResults.size(); i++) {
				modDiceResult += healthRoll.modRollResults.get(i);
			}
		}
		
		int finalResult = mainDiceResult + modDiceResult + modValue;
		System.out.println("mainDiceResult: " + mainDiceResult);
		System.out.println("modDiceResult: " + modDiceResult);
		System.out.println("modValue: " + modValue);
		
		
		return finalResult;
	}

	private void ChangeHealth(int healthModValue, CharacterContainer character, CommandContainer cmd, Boolean isTmpHP) {
		/*
		 * If subtracting, remove from tmp health first if possible
		 * if adding, check that player was not unconscious before
		 * Yes -> Stabilize
		 * No -> Death Saving Throw Failure
		 * */
		
		String message = "";
		
		if (healthModValue < 0) {
			int remainingAmt = healthModValue;
			if (character.statInfo.tmpHP <= Math.abs(remainingAmt)) {
				remainingAmt += character.statInfo.tmpHP;
				healthModValue = remainingAmt;
				character.statInfo.tmpHP = 0;
			} else {
				character.statInfo.tmpHP += remainingAmt;
				healthModValue = 0;
			}
		}
		
		StatInfoContainer characterStat = character.statInfo;
		
		boolean isUnconscious = (characterStat.curHP <= 0);
		
		if (isUnconscious) {
			if (healthModValue < 0) {
				int failureCount = characterStat.deathSaves.get("failures") + 1;
				characterStat.deathSaves.replace("failures", failureCount);
				
				if (failureCount >= 3) {
					CharacterDied(character, characterStat, cmd);
				}
			} else if (healthModValue > 0) {
				message = "Character Stabalized! \n\n";
				for (Entry<String, Integer> save : characterStat.deathSaves.entrySet()) {
					save.setValue(0);
				}
			}
		}
		
		if (isTmpHP) {
			characterStat.tmpHP += healthModValue;
		} else {
			characterStat.curHP += healthModValue;
		}
		
		if (characterStat.curHP <= 0) {
			if (Math.abs(characterStat.curHP) >= characterStat.maxHP) {
				CharacterDied(character, characterStat, cmd);
			}
			characterStat.curHP = 0;
		} else if (characterStat.curHP >= characterStat.maxHP){
			characterStat.curHP = characterStat.maxHP;
		}
		
		character.statInfo = characterStat;
		ListHealth(character, cmd, message);
	}

	private void CharacterDied(CharacterContainer character, StatInfoContainer characterStat, CommandContainer cmd) {
		SendMessage("I'm sorry your character has died.", cmd.DESTINATION);
		character.statInfo = characterStat;
		return;
	}

	private void ListHealth(CharacterContainer character, CommandContainer cmd, String message) {
		message += "Character Name: " + character.characterInfo.characterName + "\n";
		message += "Character's Health:\n\n";
		message += "Max Health: " + character.statInfo.maxHP + "\n"
				+ "Current Health: " + character.statInfo.curHP + "\n"
				+ "Temp Health: " + character.statInfo.tmpHP + "";
		
		SendMessage(message, cmd.DESTINATION);
	}

	private void SubHealth(CommandContainer cmd, CharacterContainer character) {
		if (!(cmd.SECONDARY_ARG.length > 0)) {
			SendMessage("I'm sorry I need a vlaue to remove from your hp pool", cmd.DESTINATION);
			return;
		}
		
		String healthMod = cmd.SECONDARY_ARG[0];
		if (Character.isDigit(healthMod.charAt(0)) && healthMod.contains("d")) {
			Roll roll = new Roll();
			RollContainer healthRoll = roll.CreateBlankRollContainer();
			healthRoll = roll.mainDicePoolBreakdown(healthMod, healthRoll);
			healthRoll = roll.SecondaryPhase(healthRoll, cmd, true);
			
			int healthModValue = buildResult(healthRoll);
			healthModValue = -healthModValue; //Reversed for subtracting health
			ChangeHealth (healthModValue, character, cmd, false);
		} else if (Character.isDigit(healthMod.charAt(0))) {
			int healthModValue = Integer.parseInt(healthMod);
			healthModValue = -healthModValue; ////Reversed for subtracting health
			ChangeHealth (healthModValue, character, cmd, false);
		} else {
			SendMessage("I'm sorry I don't understand that value", cmd.DESTINATION);
		}
	}

	private void HealthSave(CommandContainer cmd, CharacterContainer character) {
		
	}

	private void SendMessage(EmbedBuilder message, Channel DESDESTINATION) {
		NewMessage.send(message, DESDESTINATION);
	}
	
	private void SendMessage(String message, Channel DESTINATION) {
		NewMessage.send(message, DESTINATION);
	}
	
	private void HelpCommand(CommandContainer cmd, HashMap<String, Runnable> subCommands) {
		String message = "Template Command: Lorem ipsum dolor sit amet";
		
		for (Map.Entry<String, Runnable> command: subCommands.entrySet()) {
			message += command.getKey() + "\n";
		}
		
		SendMessage(message, cmd.DESTINATION);
	}

}
