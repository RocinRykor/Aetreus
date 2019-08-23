package studio.rrprojects.aetreusbot.dungeonsanddragons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import studio.rrprojects.aetreusbot.command.Command;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.command.Roll.RollContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterLoader;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.CharacterContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.ContainerTools.AttributeContainer;
import studio.rrprojects.aetreusbot.utils.MessageBuilder;
import studio.rrprojects.aetreusbot.utils.MessageTools;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Attributes extends Command{

	@Override
	public String getName() {
		return "Attributes";
	}

	@Override
	public String getAlias() {
		return "attr";
	}

	@Override
	public String getHelpDescription() {
		return "D&D Manages characters attribute's and saving throws";
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
	
	static HashMap<String, String> attrMap = new HashMap<>();
		
	public void executeMain(CommandContainer cmd) {
		if (cmd.AUTHOR.isFake()) {
			return;
		}
		
		CharacterContainer character = CharacterLoader.GetPlayerData(cmd.AUTHOR.getName());
		
		String title = "Character Name: " + character.characterInfo.characterName;
		
		String header = "Character's Attributes:";
		
		String message = "";
		
		ArrayList<AttributeContainer> attributes = character.attributes;
		
		for (AttributeContainer attribute : attributes) {
			message += MessageTools.CapatalizeFirst(attribute.attributeName) + ": " + attribute.value + "\n";
		}
		
		EmbedBuilder finalMessage = MessageBuilder.BuildMessage(title, header, message, Color.BLUE);
		
		SendMessage(finalMessage, cmd.DESTINATION);
	}
	
	private void SendMessage(EmbedBuilder message, Channel DESDESTINATION) {
		NewMessage.send(message, DESDESTINATION);
	}
	
	private void SendMessage(String message, Channel DESTINATION) {
		NewMessage.send(message, DESTINATION);
	}
	
	public static String getAttribute(String input) {
		String searchValue = input.toLowerCase();
		
		String key = null;
		
		key = attrMap.get(searchValue);
		
		return key;
	}

	public static RollContainer ProcessRoll(String searchString, CommandContainer cmd, RollContainer rollContainer) {
		CharacterContainer character = CharacterLoader.GetPlayerData(cmd.AUTHOR.getName());
		int baseValue = 10, modValue, profBonus = 0;
		Boolean isProf = false;
		
		for (AttributeContainer attribute : character.attributes) {
			if(attribute.attributeName.equalsIgnoreCase(searchString)) {
				baseValue = attribute.value;
				isProf = attribute.proficient;
			}
		}
		
		modValue = (int) Math.floor((float)((baseValue-10))/2);
		
		rollContainer.notes.add("Mod Value: " + modValue);
		
		boolean isSave = false; //Check for saving throw
		
		if(cmd.SECONDARY_ARG != null) {
			for (String argument : cmd.SECONDARY_ARG) {
				if(argument.equalsIgnoreCase("save")) {
					isSave = true;
				}
			}
		}
		
		if (isProf && isSave) {
			profBonus = Proficiency.GetProfValue(character);
			rollContainer.notes.add("Profciency Bonus: " + profBonus);
		};
		
		String saveText = "";
		if(isSave) {
			saveText = " as a Saving Throw";
		}
		
		rollContainer.title = "Rolling: " + searchString + saveText;
		
		rollContainer.mainDiceSides = 20;
		rollContainer.mainDicePool = 1;
		rollContainer.modValue = modValue + profBonus;
		
		return rollContainer;
	}

	public static void initTable() {
		System.out.println("Populating Attribute Table");
		
		//Strength
		attrMap.put("strength", "Strength");
		attrMap.put("str", "Strength");
		
		//Dexterity
		attrMap.put("dexterity", "Dexterity");
		attrMap.put("dex", "Dexterity");
		
		//Constitution
		attrMap.put("constitution", "Constitution");
		attrMap.put("con", "Constitution");
		
		//Intelligence
		attrMap.put("intelligence", "Intelligence");
		attrMap.put("int", "Intelligence");
		
		//Wisdom
		attrMap.put("wisdom", "Wisdom");
		attrMap.put("wis", "Wisdom");
		
		//Charisma
		attrMap.put("charisma", "Charisma");
		attrMap.put("cha", "Charisma");
	}

	public static int GetCharacterAttributeValue(CharacterContainer character, String input) {
		String searchTerm = getAttribute(input);
		
		for (AttributeContainer attribute : character.attributes) {
			if (attribute.attributeName.equalsIgnoreCase(searchTerm)) {
				return attribute.value;
			}
		}
		
		return 10;
	}

}
