package studio.rrprojects.aetreusbot.dungeonsanddragons.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import studio.rrprojects.aetreusbot.Controller;

public class CharacterPrefFileLoader {
	static HashMap<String, String> playerTable = new HashMap<>();

	public static void LoadCharacters() {
		/*
		 * See if CharacterPref File is loaded
		 * Load or Create as Needed
		 * Load/Create Files from CharacterPref file
		 * */
		
		List<Member> members = Controller.getJda().getGuilds().get(0).getMembersWithRoles(Controller.getJda().getRolesByName("Tabletop RPG", true).get(0));
		File finalCharacterPrefFile = LoadCharacterPrefFile(members);
		
		PopulatePLayerTable(finalCharacterPrefFile);
		
		CharacterLoader.ProcessPlayerTable(playerTable);
		
		CreateDirsAndFiles();
	}


	private static void CreateDirsAndFiles() {
		String startingDir = Controller.getMainDir() + File.separator + "Character Files";
		
		for (HashMap.Entry<String, String> player : playerTable.entrySet()) {
			String playerDir = startingDir + File.separator + player.getKey();
			
			File dir = new File(playerDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			
			String characterFile = playerDir + File.separator + player.getValue() + ".json";
			
			File file = new File(characterFile);
			if (!file.exists()) {
				try {
					file.createNewFile();
					TemplateGenerator.generate(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}


	private static void PopulatePLayerTable(File finalCharacterPrefFile) {
		/*
		 * Take the Character Pref file and use it to create a Hashmap table for easier use
		 * */
		
		JsonObject obj = null;
		try {
			obj = Json.parse(new FileReader(finalCharacterPrefFile)).asObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JsonArray array = obj.get("Players").asArray();
		
		for (JsonValue arrayValue : array) {
			String playerName = arrayValue.asObject().get("playerName").asString();
			String currentCharacter = arrayValue.asObject().get("currentCharacter").asString();
			
			playerTable.put(playerName, currentCharacter);
		}
	}


	private static File LoadCharacterPrefFile(List<Member> members) {
		/*
		 * Grab the ChracterPref file for later use.
		 * If it does not exist it will be created and auto-populated
		 * */
		
		String startingDir = Controller.getMainDir() + File.separator + "Character Files";
		String characterPrefFile = "CharacterPref.json";
		
		
		String finalPath = startingDir + File.separator + characterPrefFile;
		
		File fileDir = new File(startingDir);
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}
		
		File fileCharacterPref = new File(finalPath);	
		if (!fileCharacterPref.exists()) {
			System.out.println("Error: Specified File Does Not Exist!");
			try {
				fileCharacterPref.createNewFile();
				PopulateCharacterPrefFile(fileCharacterPref,members);
				System.out.println("File Created!");
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(startingDir);
		}
		
		return fileCharacterPref;
	}

	private static void PopulateCharacterPrefFile(File fileCharacterPref, List<Member> members) {
		/*
		 * If the Chartacter Preference File is created for the first time, populate it with the list of players and set a default character
		 * */
		
		JsonArray array = new JsonArray();
		
		for (Member member : members) {
			JsonObject obj = new JsonObject();
			obj.add("playerName", member.getUser().getName());
			obj.add("currentCharacter", "Default");
			array.add(obj);
		}
		
		JsonObject mainObj = new JsonObject();
		mainObj.add("Players", array);
		
		try {
			FileWriter writer = new FileWriter(fileCharacterPref);
			try {
				mainObj.writeTo(writer, WriterConfig.PRETTY_PRINT);
				writer.flush();
				writer.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	public static String GetCurrentCharacter(User user) {		
		return playerTable.get(user.getName());
	}


	public static File LoadFile(Entry<String, String> player) {
		String filePath = Controller.getMainDir() + File.separator + "Character Files" + File.separator + player.getKey() + File.separator + player.getValue() + ".json";
		return new File(filePath);
	}
	
	public static File LoadFile(User user) {
		String filePath = Controller.getMainDir() + File.separator + "Character Files" + File.separator + user.getName() + File.separator + GetCurrentCharacter(user) + ".json";
		return new File(filePath);
	}
}
