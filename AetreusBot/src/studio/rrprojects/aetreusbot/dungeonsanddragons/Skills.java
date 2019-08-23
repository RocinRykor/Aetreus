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
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.ContainerTools.SkillContainer;
import studio.rrprojects.aetreusbot.utils.MessageBuilder;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Skills extends Command{

	@Override
	public String getName() {
		return "Skills";
	}

	@Override
	public String getAlias() {
		return "Skill";
	}

	@Override
	public String getHelpDescription() {
		return "D&D - Gives information about character's current skills";
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
	
	static HashMap<String, String> skillsTable = new HashMap<>();
	
	public void executeMain(CommandContainer cmd) {
		if (cmd.AUTHOR.isFake()) {
			return;
		}
		
		CharacterContainer character = CharacterLoader.GetPlayerData(cmd.AUTHOR.getName());
		
		String title = "Character Name: " + character.characterInfo.characterName;
		
		String header = "Character's Skills:";
		
		String message = "";
		
		ArrayList<SkillContainer> skills = character.skills;
		
		for (SkillContainer skill : skills) {
			message += "Skill: " + skill.skillName + ": " + skill.proficient + "\n";
		}
		
		EmbedBuilder finalMessage = MessageBuilder.BuildMessage(title, header, message, Color.BLUE);
		
		SendMessage(finalMessage, cmd.DESTINATION);
	}
	
	public static RollContainer ProcessRoll(String searchString, CommandContainer cmd, RollContainer rollContainer) {
		CharacterContainer character = CharacterLoader.GetPlayerData(cmd.AUTHOR.getName());
		int baseValue = 10, modValue, profBonus = 0;
		Boolean isProf = false;
		String attribute = "";
		
		for (SkillContainer skill : character.skills) {
			if(skill.skillName.equalsIgnoreCase(searchString)) {
				attribute = skill.attribute;
				isProf = skill.proficient;
			}
		}
		
		for (AttributeContainer attributeContainer : character.attributes) {
			if(attributeContainer.attributeName.equalsIgnoreCase(attribute)) {
				baseValue = attributeContainer.value;
			}
		}
		
		modValue = (int) Math.floor((float)((baseValue-10))/2);
		
		System.out.println("ModValue: " + modValue);
			
		if (isProf) {
			profBonus = Proficiency.GetProfValue(character);
		};
		
		rollContainer.mainDiceSides = 20;
		rollContainer.mainDicePool = 1;
		rollContainer.modValue = modValue + profBonus;
		
		return rollContainer;
	}
	
	private void SendMessage(EmbedBuilder message, Channel DESDESTINATION) {
		NewMessage.send(message, DESDESTINATION);
	}
	
	private void SendMessage(String message, Channel DESTINATION) {
		NewMessage.send(message, DESTINATION);
	}

	public static void initTable() {
		//Acrobatics
		skillsTable.put("acrobatics", "Acrobatics");
		skillsTable.put("acro", "Acrobatics");
		
		//Animal Handling
		skillsTable.put("animal", "Animal Handling");
		skillsTable.put("ah", "Animal Handling");
		
		//Arcana
		skillsTable.put("arcana", "Arcana");
		skillsTable.put("arc", "Arcana");
		
		//Athletics
		skillsTable.put("athletics", "Athletics");
		skillsTable.put("ath", "Athletics");
		
		//Deception
		skillsTable.put("deception", "Deception");
		skillsTable.put("dec", "Deception");
		
		//History
		skillsTable.put("history", "History");
		skillsTable.put("his", "History");
		
		//Insight
		skillsTable.put("insight", "Insight");
		skillsTable.put("ins", "Insight");
		
		//Intimidation
		skillsTable.put("intimidation", "Intimidation");
		skillsTable.put("int", "Intimidation");
		
		//Investigation
		skillsTable.put("investigation", "Investigation");
		skillsTable.put("inv", "Investigation");
		
		//Medicine
		skillsTable.put("medicine", "Medicine");
		skillsTable.put("med", "Medicine");
		
		//Nature
		skillsTable.put("nature", "Nature");
		skillsTable.put("nat", "Nature");
		
		//Perception
		skillsTable.put("perception", "Perception");
		skillsTable.put("perc", "Perception");
		
		//Performance
		skillsTable.put("performance", "Performance");
		skillsTable.put("perf", "Performance");
		
		//Persuasion  
		skillsTable.put("persuasion", "Persuasion");
		skillsTable.put("pers", "Persuasion");
		
		//Religion
		skillsTable.put("religion", "Religion");
		skillsTable.put("rel", "Religion");
		
		//Sleight of Hand
		skillsTable.put("sleight", "Sleight of Hand");
		skillsTable.put("sle", "Sleight of Hand");
		
		//Stealth
		skillsTable.put("stealth", "Stealth");
		skillsTable.put("ste", "Stealth");
		
		//Survival
		skillsTable.put("survival", "Survival");
		skillsTable.put("sur", "Survival");
	}

	public static String GetSkill(String input) {
		String searchValue = input.toLowerCase();
		String key = null;
		key = skillsTable.get(searchValue);
		return key;
	}

}
