package studio.rrprojects.aetreusbot.dungeonsanddragons.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;

import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.CharacterContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.CharacterDescriptionContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.CharacterInfoContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.InventoryContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.SpellContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.StatInfoContainer;
import studio.rrprojects.aetreusbot.utils.JsonTools;

public class ContainerToJsonWriter {
	
	public static void ParseContainer(CommandContainer cmd, CharacterContainer container)  {
		JsonObject mainObj = new JsonObject();
		
		//Basic Info
		CharacterInfoContainer info = container.characterInfo;
		mainObj.add("character_name", info.characterName);
		mainObj.add("character_class", ReverseSIToDict(info.characterClass));
		mainObj.add("background", info.background);
		mainObj.add("player_name", info.playerName);
		mainObj.add("race", info.race);
		mainObj.add("alignment", info.alignment);
		mainObj.add("experience", info.experience);
		
		//Basic Stat Info
		StatInfoContainer statInfo = container.statInfo;
		mainObj.add("proficiency", statInfo.proficiency);
		mainObj.add("armor_class", statInfo.armorClass);
		mainObj.add("initiative", statInfo.initiative);
		mainObj.add("speed", statInfo.speed);
		mainObj.add("max_hp", statInfo.maxHP);
		mainObj.add("cur_hp", statInfo.curHP);
		mainObj.add("tmp_hp", statInfo.tmpHP);
		mainObj.add("hit_dice", ContainerTools.ReverseHitDice(statInfo.hitDice));
		mainObj.add("death_saves", ReverseSIToDict(statInfo.deathSaves));
		
		//Character Description
		CharacterDescriptionContainer charInfo = container.characterDescriptionInfo;
		mainObj.add("personality_traits", JsonTools.ReverseArray(charInfo.personalityTraits));
		mainObj.add("ideals", JsonTools.ReverseArray(charInfo.ideals));
		mainObj.add("bonds", JsonTools.ReverseArray(charInfo.bonds));
		mainObj.add("flaws", JsonTools.ReverseArray(charInfo.flaws));
		mainObj.add("proficiencies", JsonTools.ReverseArray(charInfo.proficiencies));
		mainObj.add("features", ReverseSSToDict(charInfo.features));
		mainObj.add("character_description", ReverseSSToDict(charInfo.characterDescription));
		
		//Inventory
		InventoryContainer inventory = container.characterInventory;
		mainObj.add("weapons", ContainerTools.ReverseWeapons(inventory.weapons));
		mainObj.add("armor", ContainerTools.ReverseArmor(inventory.armor));
		mainObj.add("items", ReverseSIToDict(inventory.items));
		mainObj.add("money", ReverseSIToDict(inventory.money));
		
		//Attribute
		mainObj.add("attributes", ContainerTools.ReverseAttributes(container.attributes));
		
		//Skills
		mainObj.add("skills", ContainerTools.ReverseSkills(container.skills));
		
		//Spells
		SpellContainer spell = container.spells;
		mainObj.add("spellcasting_class", ReverseSSToDict(spell.spellCastingClass));
		mainObj.add("spell_slots", spell.spellSlots);
		
		WriteFile(cmd, mainObj);
	}

	private static JsonObject ReverseSSToDict(HashMap<String, String> input) {
		JsonObject tmpObject = new JsonObject();
		for (Map.Entry<String, String> entry: input.entrySet()) {
			tmpObject.add(entry.getKey(), entry.getValue());
		}
		
		return tmpObject;
	}

	private static JsonObject ReverseSIToDict(HashMap<String, Integer> input) {
		JsonObject tmpObject = new JsonObject();
		for (Map.Entry<String, Integer> entry: input.entrySet()) {
			tmpObject.add(entry.getKey(), entry.getValue());
		}
		
		return tmpObject;
	}

	private static void WriteFile(CommandContainer cmd, JsonObject mainObj) {
		File file = CharacterPrefFileLoader.LoadFile(cmd.AUTHOR);
		
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			mainObj.writeTo(writer, WriterConfig.PRETTY_PRINT);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
