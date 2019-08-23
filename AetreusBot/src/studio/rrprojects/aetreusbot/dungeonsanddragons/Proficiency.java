package studio.rrprojects.aetreusbot.dungeonsanddragons;

import java.util.HashMap;
import java.util.Map.Entry;

import studio.rrprojects.aetreusbot.command.Command;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.CharacterContainer;

public class Proficiency extends Command{
	
	static HashMap<Integer, Integer> profTable = new HashMap<>();
	
	public static void initTable() {
		profTable.put(1, 2);
		profTable.put(2, 2);
		profTable.put(3, 2);
		profTable.put(4, 2);
		profTable.put(5, 3);
		profTable.put(6, 3);
		profTable.put(7, 3);
		profTable.put(8, 3);
		profTable.put(9, 4);
		profTable.put(10, 4);
		profTable.put(11, 4);
		profTable.put(12, 4);
		profTable.put(13, 5);
		profTable.put(14, 5);
		profTable.put(15, 5);
		profTable.put(16, 5);
		profTable.put(17, 6);
		profTable.put(18, 6);
		profTable.put(19, 6);
		profTable.put(20, 6);
	}

	@Override
	public String getName() {
		return "Proficiency";
	}

	@Override
	public String getAlias() {
		return "Prof";
	}

	@Override
	public String getHelpDescription() {
		return "D&D - Get information about character Proficiency";
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

	public static int GetProfValue(CharacterContainer character) {
		int totalLevel = 0;
		
		for(Entry<String, Integer> entry : character.characterInfo.characterClass.entrySet()) {
			totalLevel += entry.getValue();
		}
		
		if (totalLevel < 1 || totalLevel > 20) {
			return 0;
		}
		
		return profTable.get(totalLevel);
	}
	
}
