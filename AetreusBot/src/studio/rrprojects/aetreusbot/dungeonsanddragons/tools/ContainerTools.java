package studio.rrprojects.aetreusbot.dungeonsanddragons.tools;

import java.util.ArrayList;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

public class ContainerTools {

	public static ArrayList<HitDiceContainer> GetHitDice(JsonObject object) {
		if (object == null) {
			return null;
		}
		
		ArrayList<HitDiceContainer> tmpArray = new ArrayList<>();
		
		String className;
		int diceMax, diceRemaining, dieType;
		
		for (Member member : object) {
			className = member.getName();
			JsonObject tmpObject = member.getValue().asObject();
			diceMax = 0;
			diceRemaining = tmpObject.get("remaining").asInt();
			dieType = tmpObject.get("die_type").asInt();
			
			tmpArray.add(new HitDiceContainer(className, diceMax, diceRemaining, dieType));
		}
		
		
		return tmpArray;
	}
	
	public static JsonObject ReverseHitDice(ArrayList<HitDiceContainer> hitDice) {
		JsonObject memberObj = new JsonObject();
		
		
		for (HitDiceContainer hitDiceContainer : hitDice) {
			JsonObject tmpObj = new JsonObject();
			tmpObj.add("remaining", hitDiceContainer.diceRemaining);
			tmpObj.add("die_type", hitDiceContainer.dieType);
			
			memberObj.add(hitDiceContainer.className, tmpObj);
		}
		
		return memberObj;
	}
	
	public static ArrayList<WeaponContainer> GetWeapons(JsonObject object) {
		if (object == null) {
			return null;
		}
		
		ArrayList<WeaponContainer> tmpArray = new ArrayList<>();
		
		String weaponName, type, damage, damageType;
		Boolean finesse;
		
		for (Member member : object) {
			weaponName = member.getName();
			JsonObject tmpObject = member.getValue().asObject();
			type = tmpObject.get("type").asString();
			damage = tmpObject.get("damage").asString();
			damageType = tmpObject.get("damage_type").asString();
			finesse = tmpObject.get("finesse").asBoolean();
			
			tmpArray.add(new WeaponContainer(weaponName, type, damage, damageType, finesse));
		}
		
		
		return tmpArray;
	}
	
	public static JsonObject ReverseWeapons(ArrayList<WeaponContainer> weapons) {
		JsonObject memberObj = new JsonObject();
		
		
		for (WeaponContainer weapon : weapons) {
			JsonObject tmpObj = new JsonObject();
			tmpObj.add("type", weapon.type);
			tmpObj.add("damage", weapon.damage);
			tmpObj.add("damage_type", weapon.damageType);
			tmpObj.add("finesse", weapon.finesse);
			
			memberObj.add(weapon.weaponName, tmpObj);
		}
		
		return memberObj;
	}
	
	public static ArrayList<ArmorContainer> GetArmor(JsonObject object) {
		if (object == null) {
			return null;
		}
		
		ArrayList<ArmorContainer> tmpArray = new ArrayList<>();
		
		String armorName;
		Boolean equipped, dexBonus, stealthDisadvantage;
		int ac, dexMax;
		
		for (Member member : object) {
			armorName = member.getName();
			JsonObject tmpObject = member.getValue().asObject();
			ac = tmpObject.get("ac").asInt();
			equipped = tmpObject.get("equipped").asBoolean();
			dexBonus = tmpObject.get("dex_bonus").asBoolean();
			dexMax = tmpObject.get("dex_max").asInt();
			stealthDisadvantage = tmpObject.get("stealth_disadvantage").asBoolean();
			
			tmpArray.add(new ArmorContainer(armorName, ac, equipped, dexBonus, dexMax, stealthDisadvantage));
		}
		
		
		return tmpArray;
	}
	
	public static JsonObject ReverseArmor(ArrayList<ArmorContainer> armors) {
		JsonObject memberObj = new JsonObject();
		
		
		for (ArmorContainer armor : armors) {
			JsonObject tmpObj = new JsonObject();
			tmpObj.add("ac", armor.ac);
			tmpObj.add("equipped", armor.equipped);
			tmpObj.add("dex_bonus", armor.dexBonus);
			tmpObj.add("dex_max", armor.dexMax);
			tmpObj.add("stealth_disadvantage", armor.stealthDisadvantage);
			
			memberObj.add(armor.armorName, tmpObj);
		}
		
		return memberObj;
	}
	
	public static ArrayList<AttributeContainer> GetAttributes(JsonObject object) {
		if (object == null) {
			return null;
		}
		
		ArrayList<AttributeContainer> tmpArray = new ArrayList<>();
		
		String attributeName;
		int value;
		Boolean proficient;
		
		for (Member member : object) {
			attributeName = member.getName();
			JsonObject tmpObject = member.getValue().asObject();
			value = tmpObject.get("value").asInt();
			proficient = tmpObject.get("proficient").asBoolean();
			
			tmpArray.add(new AttributeContainer(attributeName, value, proficient));
		}
		
		return tmpArray;
	}
	
	public static JsonObject ReverseAttributes(ArrayList<AttributeContainer> attributes) {
		JsonObject memberObj = new JsonObject();
		
		
		for (AttributeContainer attribute : attributes) {
			JsonObject tmpObj = new JsonObject();
			tmpObj.add("value", attribute.value);
			tmpObj.add("proficient", attribute.proficient);

			
			memberObj.add(attribute.attributeName, tmpObj);
		}
		
		return memberObj;
	}
	
	public static ArrayList<SkillContainer> GetSkills(JsonObject object) {
		if (object == null) {
			return null;
		}
		
		ArrayList<SkillContainer> tmpArray = new ArrayList<>();
		
		String skillName, attribute;
		Boolean proficient;
		
		for (Member member : object) {
			skillName = member.getName();
			JsonObject tmpObject = member.getValue().asObject();
			attribute = tmpObject.get("attribute").asString();
			proficient = tmpObject.get("proficient").asBoolean();
			
			tmpArray.add(new SkillContainer(skillName, attribute, proficient));
		}
		
		return tmpArray;
	}
	
	public static JsonObject ReverseSkills(ArrayList<SkillContainer> skills) {
		JsonObject memberObj = new JsonObject();
		
		
		for (SkillContainer skill : skills) {
			JsonObject tmpObj = new JsonObject();
			tmpObj.add("proficient", skill.proficient);
			tmpObj.add("attribute", skill.attribute);
			
			memberObj.add(skill.skillName, tmpObj);
		}
		
		return memberObj;
	}
	
	public static class HitDiceContainer { //Stores all of the parsed variables for later use
		public String className;
		public int diceMax;
		public int diceRemaining;
		public int dieType;
		
		public HitDiceContainer(String className, int diceMax, int diceRemaining, int dieType) {
			this.className = className;
			this.diceMax = diceMax;
			this.diceRemaining = diceRemaining;
			this.dieType = dieType;
		}
		
	}
		
	public static class WeaponContainer { //Stores all of the parsed variables for later use
		public String weaponName;
		public String type;
		public String damage;
		public String damageType;
		public Boolean finesse;
		
		public WeaponContainer(String weaponName, String type, String damage, String damageType, Boolean finesse) {
			this.weaponName = weaponName;
			this.type = type;
			this.damage = damage;
			this.damageType = damageType;
			this.finesse = finesse;
		}
		
	}
	
	public static class ArmorContainer { //Stores all of the parsed variables for later use
		public String armorName;
		public int ac;
		public Boolean equipped;
		public Boolean dexBonus;
		public int dexMax;
		public Boolean stealthDisadvantage;
		
		public ArmorContainer(String armorName, int ac, Boolean equipped, Boolean dexBonus, int dexMax, Boolean stealthDisadvantage) {
			this.armorName = armorName;
			this.ac = ac;
			this.equipped = equipped;
			this.dexBonus = dexBonus;
			this.dexMax = dexMax;
			this.stealthDisadvantage = stealthDisadvantage;
		}
		
	}
	
	public static class AttributeContainer { //Stores all of the parsed variables for later use
		public String attributeName;
		public int value;
		public Boolean proficient;
		
		public AttributeContainer(String attributeName, int value, Boolean proficient) {
			this.attributeName = attributeName;
			this.value = value;
			this.proficient = proficient;

		}
		
	}
	
	public static class SkillContainer { //Stores all of the parsed variables for later use
		public String skillName;
		public String attribute;
		public Boolean proficient;
		
		public SkillContainer(String skillName, String attribute, Boolean proficient) {
			this.skillName = skillName;
			this.attribute = attribute;
			this.proficient = proficient;

		}
		
	}

	
}
