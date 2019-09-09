package studio.rrprojects.aetreusbot.dungeonsanddragons;

import java.util.ArrayList;
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
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.ContainerTools.WeaponContainer;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Attack extends Command {

	@Override
	public String getName() {
		return "Attack";
	}

	@Override
	public String getAlias() {
		return "atk";
	}

	@Override
	public String getHelpDescription() {
		return "D&D - Rolls your characters attack based on givin weapon";
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
		//subCommands.put("list", () -> TemplateList(cmd, character));
		//subCommands.put("add", () -> TemplateAdd(cmd, character));
		//put("remove", () -> TemplateRemove(cmd, character));
		//subCommands.put("set", () -> TemplateSet(cmd, character));
		
		if (subCommands.containsKey(cmd.MAIN_ARG.toLowerCase())) {
			subCommands.get(cmd.MAIN_ARG.toLowerCase()).run();
			return;
		}
		
		Boolean foundWeapon = false;
		ArrayList<WeaponContainer> weapons = character.characterInventory.weapons;
		WeaponContainer activeWeapon = null;
		String searchTerm = cmd.MAIN_ARG;
		
		for (WeaponContainer weapon : weapons) {
			if (weapon.weaponName.toLowerCase().contains(searchTerm.toLowerCase())) {
				foundWeapon = true;
				activeWeapon = weapon;
			}
		}
		
		Roll roll = new Roll();
		
		RollContainer rollContainer = roll.CreateBlankRollContainer();
		
		if (foundWeapon) {
			rollContainer = ParseWeapon(activeWeapon, character, rollContainer, cmd);
		} else {
			SendMessage("I'm sorry I cannot find a matching weapon. Falling back on a default roll", cmd.DESTINATION);
			rollContainer.title = "Default Roll: 1, 20-sided die";
		}
		
		roll.SecondaryPhase(rollContainer, cmd);
		
	}
	
	private RollContainer ParseWeapon(WeaponContainer weapon, CharacterContainer character, RollContainer rollContainer, CommandContainer cmd) {
		
		
		rollContainer.title = "Rolling for Attack - Weapon: " + weapon.weaponName;
		
		String baseAttribute = "Strength";
		
		//Check Finesse
		Boolean applyFinesse = (weapon.finesse && (Attributes.GetCharacterAttributeValue(character, "Dex") >= Attributes.GetCharacterAttributeValue(character, "Strength")));
		Boolean isRanged = CheckRanged(weapon);
		
		if (isRanged || applyFinesse) {
			baseAttribute = "Dex";
		}
		
		//Check for "Hex Warrior" Feat
		if(IsHexWarrior(character)) {
			baseAttribute = "CHA";
		}
		
		
		
		//Calculate Attribute Score
		int baseValue = Attributes.GetCharacterAttributeValue(character, baseAttribute);
		int modValue = (int) Math.floor((float)((baseValue-10))/2);
		rollContainer.modValue += modValue;
		rollContainer.notes.add("Using Attribute: " + Attributes.getAttribute(baseAttribute) + " | Mod Value: " + modValue);
		
		//Check Proficiency
		int profValue = ParseProficiency(weapon, character);
		if (profValue > 0) { rollContainer.notes.add("Profciency Bonus: " + profValue);}
		rollContainer.modValue += profValue;
		
		
		return rollContainer;
	}

	private boolean IsHexWarrior(CharacterContainer character) {
		HashMap<String, String> feats = character.characterDescriptionInfo.features;
		
		return feats.containsKey("Hex Warrior");
	}

	private int ParseProficiency(WeaponContainer weapon, CharacterContainer character) {
		String weapontype = weapon.type;
		String weaponName = weapon.weaponName;
		Boolean isProficient = false;
		
		for (String proficiency: character.characterDescriptionInfo.proficiencies) {
			if (proficiency.toLowerCase().contains(weapontype.toLowerCase())) {
				isProficient = true;
			} else if (proficiency.toLowerCase().contains(weaponName.toLowerCase())) {
				isProficient = true;
			}
		}
		
		if (isProficient) {
			return Proficiency.GetProfValue(character);
		} else {
			return 0;
		}
	}

	/*
	 * private int[] ParseDamage(String damage) {
		int[] tmpInt = new int[2];
		
		String[] tmpStringArray = null;
		
		if (damage.contains("d")) {
			String tmpString = damage.replace("d", " ");
			tmpStringArray = tmpString.split(" ");
			for (int i = 0; i < tmpInt.length; i++) {
				tmpInt[i] = Integer.parseInt(tmpStringArray[i]);
			}
		} else {
			tmpInt[0] = 1;
			tmpInt[1] = 4;
		}
		
		return tmpInt;
	}
	 * */

	private Boolean CheckRanged(WeaponContainer weapon) {
		return weapon.type.toLowerCase().contains("ranged");
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
