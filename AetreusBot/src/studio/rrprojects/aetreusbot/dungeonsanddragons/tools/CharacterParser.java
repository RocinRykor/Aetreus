package studio.rrprojects.aetreusbot.dungeonsanddragons.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.ContainerTools.ArmorContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.ContainerTools.AttributeContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.ContainerTools.HitDiceContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.ContainerTools.SkillContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.ContainerTools.WeaponContainer;
import studio.rrprojects.aetreusbot.utils.JsonTools;

public class CharacterParser {	
	
	public static CharacterContainer ParseCharacter(File characterFile) {
		JsonObject mainObj = null;
		
		try {
			mainObj = Json.parse(new FileReader(characterFile)).asObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		//Basic Character Info
		String 							characterName = 			mainObj.get("character_name").asString();
		HashMap<String, Integer> 		characterClass = 			JsonTools.ConvertJsonDictToSIHashMap(mainObj.get("character_class"));
		String 							background = 				mainObj.get("background").asString();
		String 							playerName = 				mainObj.get("player_name").asString();
		String 							race = 						mainObj.get("race").asString();
		String 							alignment = 				mainObj.get("alignment").asString();
		int 							experience = 				mainObj.get("experience").asInt();
		
		CharacterInfoContainer characterInfo = new CharacterInfoContainer(characterName, characterClass, background, playerName, race, alignment, experience);
		
		//Basic Stat Info
		int 								proficiency = 			mainObj.get("proficiency").asInt();
		int 								armorClass = 			mainObj.get("armor_class").asInt();
		int 								initiative = 			mainObj.get("initiative").asInt();		
		int 								speed = 				mainObj.get("speed").asInt();
		int 								maxHP = 				mainObj.get("max_hp").asInt();
		int 								curHP = 				mainObj.get("cur_hp").asInt();
		int 								tmpHP = 				mainObj.get("tmp_hp").asInt();
		ArrayList<HitDiceContainer> 		hitDice = 				ContainerTools.GetHitDice(mainObj.get("hit_dice").asObject());
		HashMap<String, Integer> 			deathSaves = 			JsonTools.ConvertJsonDictToSIHashMap(mainObj.get("death_saves"));
		
		StatInfoContainer statInfo = new StatInfoContainer(proficiency, armorClass, initiative, speed, maxHP, curHP, tmpHP, hitDice, deathSaves);
		
		//Character Description
		ArrayList<String>					personalityTraits = 	JsonTools.ConvertArray(mainObj.get("personality_traits").asArray());
		ArrayList<String> 					ideals =				JsonTools.ConvertArray(mainObj.get("ideals").asArray());
		ArrayList<String> 					bonds = 				JsonTools.ConvertArray(mainObj.get("bonds").asArray());
		ArrayList<String> 					flaws = 				JsonTools.ConvertArray(mainObj.get("flaws").asArray());
		ArrayList<String> 					proficiencies = 		JsonTools.ConvertArray(mainObj.get("proficiencies").asArray());
		LinkedHashMap<String, String> 		features = 				JsonTools.ConvertJsonDictToSSHashMap(mainObj.get("features"));
		LinkedHashMap<String, String> 		characterDiscription = 	JsonTools.ConvertJsonDictToSSHashMap(mainObj.get("character_description"));
		
		CharacterDescriptionContainer characterDescriptionInfo = new CharacterDescriptionContainer(personalityTraits, ideals, bonds, flaws, proficiencies, features, characterDiscription);
		
		//Inventory
		ArrayList<WeaponContainer> 			weapons = 				ContainerTools.GetWeapons(mainObj.get("weapons").asObject());
		ArrayList<ArmorContainer> 			armor = 				ContainerTools.GetArmor(mainObj.get("armor").asObject());
		LinkedHashMap<String, Integer> 		items = 				JsonTools.ConvertJsonDictToSIHashMap(mainObj.get("items"));
		LinkedHashMap<String, Integer> 		money = 				JsonTools.ConvertJsonDictToSIHashMap(mainObj.get("money"));
		
		InventoryContainer characterInventory = new InventoryContainer(weapons, armor, items, money);

		//Attribute
		ArrayList<AttributeContainer> 		attributes = 			ContainerTools.GetAttributes(mainObj.get("attributes").asObject());
		
		//Skills
		ArrayList<SkillContainer> 			skills = 				ContainerTools.GetSkills(mainObj.get("skills").asObject());
		
		//Spells
		LinkedHashMap<String, String> 		spellCastingClass = 	JsonTools.ConvertJsonDictToSSHashMap(mainObj.get("spellcasting_class"));
		//To Be Completed Later
		JsonObject 								spellSlots = 		mainObj.get("spell_slots").asObject();
		
		SpellContainer spells = new SpellContainer(spellCastingClass, spellSlots);
		
		return new CharacterContainer(characterInfo, statInfo, characterDescriptionInfo, characterInventory, attributes, skills, spells) ;
	}
	
	
	public static class CharacterInfoContainer{
		public String characterName;
		public HashMap<String, Integer> characterClass;
		public String background;
		public String playerName;
		public String race;
		public String alignment;
		public int experience;
		
		public CharacterInfoContainer(String characterName, HashMap<String, Integer> characterClass, String background, String playerName, String race, String alignment, int experience) {
			this.characterName = characterName;
			this.characterClass = characterClass;
			this.background = background;
			this.playerName = playerName;
			this.race = race;
			this.alignment = alignment;
			this.experience = experience;
		}
	}
	
	public static class StatInfoContainer {
		public int proficiency, armorClass, initiative, speed, maxHP, curHP, tmpHP;
		public ArrayList<HitDiceContainer> hitDice;
		public HashMap<String, Integer> deathSaves;
		
		public StatInfoContainer(int proficiency, int armorClass, int initiative, int speed, int maxHP, int curHP, int tmpHP, ArrayList<HitDiceContainer> hitDice, HashMap<String, Integer> deathSaves) {
			this.proficiency = proficiency;
			this.armorClass = armorClass;
			this.initiative = initiative;
			this.speed = speed;
			this.maxHP = maxHP;
			this.curHP = curHP;
			this.tmpHP = tmpHP;
			this.hitDice = hitDice;
			this.deathSaves = deathSaves;
		}
	}
	
	public static class CharacterDescriptionContainer{
		public ArrayList<String> personalityTraits, ideals, bonds, flaws, proficiencies;
		public HashMap<String, String> features, characterDescription;
		
		public CharacterDescriptionContainer(ArrayList<String> personalityTraits, ArrayList<String> ideals, ArrayList<String> bonds, ArrayList<String> flaws, ArrayList<String> proficiencies, 
				HashMap<String, String> features, HashMap<String, String> characterDiscription) {
			this.personalityTraits = personalityTraits;
			this.ideals = ideals;
			this.bonds = bonds;
			this.flaws = flaws;
			this.proficiencies = proficiencies;
			this.features = features;
			this.characterDescription = characterDiscription;
		}
	}
	
	public static class InventoryContainer{
		public ArrayList<WeaponContainer> weapons;
		public ArrayList<ArmorContainer> armor;
		public HashMap<String, Integer> items, money;
		
		public InventoryContainer(ArrayList<WeaponContainer> weapons, ArrayList<ArmorContainer> armor, HashMap<String, Integer> items, HashMap<String, Integer> money) {
			this.weapons = weapons;
			this.armor = armor;
			this.items = items;
			this.money = money;
		}
	}
	
	public static class SpellContainer{
		public HashMap<String, String> spellCastingClass; 
		public JsonObject spellSlots;
		
		public SpellContainer(HashMap<String, String> spellCastingClass, JsonObject spellSlots) {
			this.spellCastingClass = spellCastingClass;
			this.spellSlots = spellSlots;
		}
	}
	
	
	public static class CharacterContainer{
		public CharacterInfoContainer characterInfo; 
		public StatInfoContainer statInfo;
		public CharacterDescriptionContainer characterDescriptionInfo;
		public InventoryContainer characterInventory; 
		public ArrayList<AttributeContainer> attributes; 
		public ArrayList<SkillContainer> skills;
		public SpellContainer spells;
		
		public CharacterContainer(CharacterInfoContainer characterInfo, StatInfoContainer statInfo, CharacterDescriptionContainer characterDescriptionInfo, 
				InventoryContainer characterInventory, ArrayList<AttributeContainer> attributes, ArrayList<SkillContainer> skills, SpellContainer spells) {
			this.characterInfo = characterInfo;
			this.statInfo = statInfo;
			this.characterDescriptionInfo = characterDescriptionInfo;
			this.characterInventory = characterInventory;
			this.attributes = attributes;
			this.skills = skills;
			this.spells = spells;
		}
	}
	
}
