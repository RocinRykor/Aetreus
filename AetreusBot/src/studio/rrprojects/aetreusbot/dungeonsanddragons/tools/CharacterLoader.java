package studio.rrprojects.aetreusbot.dungeonsanddragons.tools;

import java.util.HashMap;

import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.CharacterContainer;

public class CharacterLoader {
	/*
	 * This function will begin by creating a dictionary of each character tied to it's user.
	 * Each character will be a container based on the json character sheet
	 * */
	static HashMap<String, CharacterContainer> characterTable;
	
	public static void ProcessPlayerTable(HashMap<String, String> playerTable) {
		characterTable = new HashMap<>();
		for (HashMap.Entry<String, String> player : playerTable.entrySet()) {
			characterTable.put(player.getKey(), CharacterParser.ParseCharacter(CharacterPrefFileLoader.LoadFile(player)));
		}		
	}
	
	public static CharacterContainer GetPlayerData(String user) {
		return characterTable.get(user);
	}
}
