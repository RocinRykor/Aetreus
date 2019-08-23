package studio.rrprojects.aetreusbot.dungeonsanddragons.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TemplateGenerator {

	public static void generate(File file) {
		
		/*
		 * This is the basic template json file that will be generated by default
		 * */
		
		String fileContents = "{\r\n" + 
				"    \"character_name\" : \"character_name\",\r\n" + 
				"    \"char_class\" : {},\r\n" + 
				"    \"background\" : \"background\",\r\n" + 
				"    \"player_name\" : \"player_name\",\r\n" + 
				"    \"race\" : \"race\",\r\n" + 
				"    \"alignment\" : \"alignment\",\r\n" + 
				"    \"experience\" : 0,\r\n" + 
				"    \"proficiency\" : 0,\r\n" + 
				"    \"armor_class\" : 0,\r\n" + 
				"    \"initiative\" : 0,\r\n" + 
				"    \"speed\" : 0,\r\n" + 
				"    \"max_hp\" : 0,\r\n" + 
				"    \"cur_hp\" : 0,\r\n" + 
				"    \"tmp_hp\" : 0,\r\n" + 
				"    \"hit_dice\" : {},\r\n" + 
				"    \"death_saves\" : {\r\n" + 
				"        \"successes\" : 0,\r\n" + 
				"        \"failures\" : 0\r\n" + 
				"    },\r\n" + 
				"    \"personality_traits\" : [],\r\n" + 
				"    \"ideals\" : [],\r\n" + 
				"    \"bonds\" : [],\r\n" + 
				"    \"flaws\" : [],\r\n" + 
				"    \"weapons\" : {},\r\n" + 
				"    \"armor\" : {},\r\n" + 
				"    \"items\" : {},\r\n" + 
				"    \"money\" : {\r\n" + 
				"        \"copper\"   : 0,\r\n" + 
				"        \"silver\"   : 0,\r\n" + 
				"        \"gold\"     : 0,\r\n" + 
				"        \"electrum\" : 0,\r\n" + 
				"        \"platinum\" : 0\r\n" + 
				"    },\r\n" + 
				"    \"proficiencies\" : [],\r\n" + 
				"    \"features\" : {},\r\n" + 
				"    \"character_description\" : {\r\n" + 
				"        \"age\" : 0,\r\n" + 
				"        \"eye_color\" : \"eye_color\",\r\n" + 
				"        \"hair_color\" : \"hair_color\",\r\n" + 
				"        \"height\" : \"height\",\r\n" + 
				"        \"skin\" : \"skin\",\r\n" + 
				"        \"weight\" : \"weight\",\r\n" + 
				"        \"backstory\" : \"backstory\",\r\n" + 
				"        \"appearance\" : \"appearance\"\r\n" + 
				"    },\r\n" + 
				"    \"attributes\" : {\r\n" + 
				"        \"strength\" : {\r\n" + 
				"            \"value\" : 0,\r\n" + 
				"            \"proficient\" : false\r\n" + 
				"        },\r\n" + 
				"        \"dexterity\" : {\r\n" + 
				"            \"value\" : 0,\r\n" + 
				"            \"proficient\" : false\r\n" + 
				"        },\r\n" + 
				"        \"constitution\" : {\r\n" + 
				"            \"value\" : 0,\r\n" + 
				"            \"proficient\" : false\r\n" + 
				"        },\r\n" + 
				"        \"intelligence\" : {\r\n" + 
				"            \"value\" : 0,\r\n" + 
				"            \"proficient\" : false\r\n" + 
				"        },\r\n" + 
				"        \"wisdom\" : {\r\n" + 
				"            \"value\" : 0,\r\n" + 
				"            \"proficient\" : false\r\n" + 
				"        },\r\n" + 
				"        \"charisma\" : {\r\n" + 
				"            \"value\" : 0,\r\n" + 
				"            \"proficient\" : false\r\n" + 
				"        }\r\n" + 
				"    },\r\n" + 
				"        \"skills\" : {\r\n" + 
				"            \"acrobatics\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"dexterity\"\r\n" + 
				"            },\r\n" + 
				"            \"animal_handling\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"wisdom\"\r\n" + 
				"            },\r\n" + 
				"            \"arcana\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"intelligence\"\r\n" + 
				"            },\r\n" + 
				"            \"athletics\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"strength\"\r\n" + 
				"            },\r\n" + 
				"            \"deception\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"charisma\"\r\n" + 
				"            },\r\n" + 
				"            \"history\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"intelligence\"\r\n" + 
				"            },\r\n" + 
				"            \"insight\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"wisdom\"\r\n" + 
				"            },\r\n" + 
				"            \"intimidation\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"charisma\"\r\n" + 
				"            },\r\n" + 
				"            \"investigation\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"intelligence\"\r\n" + 
				"            },\r\n" + 
				"            \"medicine\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"wisdom\"\r\n" + 
				"            },\r\n" + 
				"            \"nature\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"intelligence\"\r\n" + 
				"            },\r\n" + 
				"            \"perception\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"wisdom\"\r\n" + 
				"            },\r\n" + 
				"            \"performance\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"charisma\"\r\n" + 
				"            },\r\n" + 
				"            \"persuasion\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"charisma\"\r\n" + 
				"            },\r\n" + 
				"            \"religion\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"intelligence\"\r\n" + 
				"            },\r\n" + 
				"            \"sleight_of_hand\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"dexterity\"\r\n" + 
				"            },\r\n" + 
				"            \"stealth\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"dexterity\"\r\n" + 
				"            },\r\n" + 
				"            \"survival\" : {\r\n" + 
				"                \"proficient\" : false,\r\n" + 
				"                \"attribute\" : \"wisdom\"\r\n" + 
				"            }\r\n" + 
				"        },\r\n" + 
				"    \"spellcasting\" : {},\r\n" + 
				"    \"spell_slots\" : {\r\n" + 
				"        \"0\" : {\r\n" + 
				"            \"max_slots\" : 0,\r\n" + 
				"            \"used_slots\" : 0,\r\n" + 
				"            \"known_spells\" : {}\r\n" + 
				"        },\r\n" + 
				"        \"1\" : {\r\n" + 
				"            \"max_slots\" : 0,\r\n" + 
				"            \"used_slots\" : 0,\r\n" + 
				"            \"known_spells\" : {}\r\n" + 
				"        },\r\n" + 
				"        \"2\" : {\r\n" + 
				"            \"max_slots\" : 0,\r\n" + 
				"            \"used_slots\" : 0,\r\n" + 
				"            \"known_spells\" : {}\r\n" + 
				"        },\r\n" + 
				"        \"3\" : {\r\n" + 
				"            \"max_slots\" : 0,\r\n" + 
				"            \"used_slots\" : 0,\r\n" + 
				"            \"known_spells\" : {}\r\n" + 
				"        },\r\n" + 
				"        \"4\" : {\r\n" + 
				"            \"max_slots\" : 0,\r\n" + 
				"            \"used_slots\" : 0,\r\n" + 
				"            \"known_spells\" : {}\r\n" + 
				"        },\r\n" + 
				"        \"5\" : {\r\n" + 
				"            \"max_slots\" : 0,\r\n" + 
				"            \"used_slots\" : 0,\r\n" + 
				"            \"known_spells\" : {}\r\n" + 
				"        },\r\n" + 
				"        \"6\" : {\r\n" + 
				"            \"max_slots\" : 0,\r\n" + 
				"            \"used_slots\" : 0,\r\n" + 
				"            \"known_spells\" : {}\r\n" + 
				"        },\r\n" + 
				"        \"7\" : {\r\n" + 
				"            \"max_slots\" : 0,\r\n" + 
				"            \"used_slots\" : 0,\r\n" + 
				"            \"known_spells\" : {}\r\n" + 
				"        },\r\n" + 
				"        \"8\" : {\r\n" + 
				"            \"max_slots\" : 0,\r\n" + 
				"            \"used_slots\" : 0,\r\n" + 
				"            \"known_spells\" : {}\r\n" + 
				"        },\r\n" + 
				"        \"9\" : {\r\n" + 
				"            \"max_slots\" : 0,\r\n" + 
				"            \"used_slots\" : 0,\r\n" + 
				"            \"known_spells\" : {}\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"}";
		
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(fileContents);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
