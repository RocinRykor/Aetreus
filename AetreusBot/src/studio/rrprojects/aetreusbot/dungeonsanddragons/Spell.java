package studio.rrprojects.aetreusbot.dungeonsanddragons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import studio.rrprojects.aetreusbot.Controller;
import studio.rrprojects.aetreusbot.command.Command;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterLoader;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.CharacterContainer;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Spell extends Command {

	@Override
	public String getName() {
		return "Spell";
	}

	@Override
	public String getAlias() {
		return "Spells";
	}

	@Override
	public String getHelpDescription() {
		return "D&D - Manages a player's spells and pulls from the Spell Repository";
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
		
		String repoName = "SpellRepo.json";
		String repoFilePath = Controller.getMainDir() + File.separator + "Repo" + File.separator + repoName;
		
		CharacterContainer character = CharacterLoader.GetPlayerData(cmd.AUTHOR.getName());
		
		HashMap<String, Runnable> subCommands = new HashMap<>();
		subCommands.put("help", () -> HelpCommand(cmd, subCommands));
		subCommands.put("search", () -> SpellSearch(cmd, repoFilePath));
		subCommands.put("describe", () -> SpellDescribe(cmd, repoFilePath));
		//subCommands.put("write", () -> SpellWrite(cmd, repoFilePath));
		//subCommands.put("add", () -> SpellAdd(cmd, character));
		//put("remove", () -> SpellRemove(cmd, character));
		//subCommands.put("set", () -> SpellSet(cmd, character));
		
		if (subCommands.containsKey(cmd.MAIN_ARG.toLowerCase())) {
			subCommands.get(cmd.MAIN_ARG.toLowerCase()).run();
			return;
		}
	}
	
	private void SpellWrite(CommandContainer cmd, String repoFilePath) {
		JsonArray repoObj = null;
		
		try {
			repoObj = Json.parse(new FileReader(repoFilePath)).asArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (JsonValue jsonValue : repoObj) {
			JsonObject jsonObj = jsonValue.asObject();
			
			jsonObj.add("homebrew", false);
		}
		
		try {
			FileWriter writer = new FileWriter(new File(repoFilePath));
			repoObj.writeTo(writer, WriterConfig.PRETTY_PRINT);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unused")
	private void SpellDescribe(CommandContainer cmd, String repoFilePath) {
		JsonArray repoObj = null;
		
		try {
			repoObj = Json.parse(new FileReader(repoFilePath)).asArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String baseSearchTerm = "";
		
		//Isolate search term from rest of command
		if (cmd.SECONDARY_ARG.length > 0) {
			for (int i = 0; i < cmd.SECONDARY_ARG.length; i++) {
				baseSearchTerm += cmd.SECONDARY_ARG[i];
				if (i != cmd.SECONDARY_ARG.length - 1) {
					baseSearchTerm += " ";
				}
			}
		}
		
		JsonObject searchResult = new JsonObject();
		for (JsonValue jsonValue : repoObj) {
			JsonObject jsonObj = jsonValue.asObject();
			
			if (jsonObj.get("name").asString().equalsIgnoreCase(baseSearchTerm)) {
				searchResult = jsonObj;
			}
		}
		
		String outputMessage = "";
		
		if (searchResult == null) {
			outputMessage += "I'm sorry, but I can't find that spell. Please consider using the search function and coping the name for better results";
			SendMessage(outputMessage, cmd.DESTINATION);
			return;
		}
		
		outputMessage += searchResult.get("name").asString() + "\n"
				+ searchResult.get("type").asString() + "\n\n"
						+ "Casting Time: " + searchResult.get("casting_time").asString() + "\n"
								+ "Range: " + searchResult.get("range").asString() + "\n"
										+ "Components : " + searchResult.get("components").asObject().getString("raw", "none") + "\n"
												+ "Duration: " + searchResult.get("duration").asString() + "\n\n"
														+ "Description: " + searchResult.get("description").asString() + "\n";
		String higherLevels = searchResult.getString("higher_levels", null);
		if (higherLevels != null) {
			outputMessage += "At Higher Levels: " + higherLevels;
		}

		
		SendMessage(outputMessage, cmd.DESTINATION);
	}

	private void SpellSearch(CommandContainer cmd, String repoFilePath) {
		JsonArray repoObj = null;
		
		try {
			repoObj = Json.parse(new FileReader(repoFilePath)).asArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String baseSearchTerm = "";
		
		//Isolate search term from rest of command
		if (cmd.SECONDARY_ARG.length > 0) {
			for (int i = 0; i < cmd.SECONDARY_ARG.length; i++) {
				baseSearchTerm += cmd.SECONDARY_ARG[i];
				if (i != cmd.SECONDARY_ARG.length - 1) {
					baseSearchTerm += " ";
				}
			}
		}
		
		//Break apart search term at --subcommand
		ArrayList<String> searchTerm = new ArrayList<>();
		
		int searchTermCount = 0;
		
		while (baseSearchTerm.length() > 0) {
			if (searchTermCount >= 10) {
				break;
			}
			
			int startIndex = baseSearchTerm.indexOf("--");
			int nextIndex = baseSearchTerm.indexOf("--", 2);
			
			if (startIndex == -1) {
				break;
			}
			
			if (nextIndex == -1) {
				nextIndex = baseSearchTerm.length();
			}
			
			String tmpString = baseSearchTerm.substring(startIndex, nextIndex);

			baseSearchTerm = baseSearchTerm.replace(tmpString, "");
			
			searchTerm.add(tmpString.trim());
			
			searchTermCount += 1;
		}
		
		//Create an Arraylist to store future matching search results
		ArrayList<JsonObject> searchResult = new ArrayList<>();
		
		String outputMessage = "";
		
		//Make initial search pass
		searchResult = FirstSearch(searchTerm.get(0), repoObj);
		
		//Refine the search Results
		if (searchTerm.size() > 1) {
			for (int i = 1; i < searchTerm.size(); i++) {
				searchResult = RefineSearch(searchTerm.get(i), searchResult);
			}
		}

		
		int searchSizeLimit = 20;
		outputMessage += searchResult.size() + " matching results found! \n\n";
		
		
		if (searchResult.size() == 0) {
			outputMessage += "Expand the search paramaters for more matches \n";
		} else if (searchResult.size() > searchSizeLimit){
			outputMessage += "Only showing first " + searchSizeLimit + ": Please consider narrowing search paramaters \n\n";
			for (int i = 0; i < searchSizeLimit; i++) {
				outputMessage += searchResult.get(i).get("name").asString() + "\n";
			}
		} else {
			for (JsonObject jsonObject : searchResult) {
				outputMessage += jsonObject.get("name").asString() + "\n";
			}
		}
		
		SendMessage(outputMessage, cmd.DESTINATION);
	}

	private ArrayList<JsonObject> RefineSearch(String input, ArrayList<JsonObject> searchResult) {
		//Same as First Search but uses the arraylist as the source and removes non-matching results
		
		input = input.replaceFirst("--", "");
		
		System.out.println(input);
		
		int tmpInt = input.indexOf(" ");
		if (tmpInt == -1) {
			return searchResult;
		}
		
		String searchCategory = input.substring(0, input.indexOf(" "));
		String searchTerm = input.substring(searchCategory.length() + 1);
		
		List<JsonObject> toRemove = new ArrayList<JsonObject>();
		for (JsonObject jsonObject : searchResult) {
			Boolean matchFound = false;
			
			if (searchCategory.toLowerCase().contains("class")) {
				JsonArray jsonArray = jsonObject.get("classes").asArray();
				for (JsonValue tmpValue : jsonArray) {
					if (tmpValue.asString().toLowerCase().contains(searchTerm.toLowerCase())) {
						matchFound = true;
					}
				}
			} else {
				if (jsonObject.get(searchCategory).toString().toLowerCase().contains(searchTerm.toLowerCase())) {
					matchFound = true;
				}
			}
			
			if (!matchFound) {
				toRemove.add(jsonObject);
			}
		}
		
		searchResult.removeAll(toRemove);
		
		return searchResult;
	}

	private ArrayList<JsonObject> FirstSearch(String input, JsonArray repoObj) {
		//uses the Repository as the source adds any matching values to an arraylist
		
		ArrayList<JsonObject> tmpArray = new ArrayList<>();
		
		input = input.replaceFirst("--", "");
		
		String searchCategory = input.substring(0, input.indexOf(" "));
		String searchTerm = input.substring(searchCategory.length() + 1);
		
		for (JsonValue jsonValue : repoObj) {
			JsonObject jsonObj = jsonValue.asObject();
			
			if (searchCategory.toLowerCase().contains("class")) {
				JsonArray jsonArray = jsonObj.get("classes").asArray();
				for (JsonValue tmpValue : jsonArray) {
					if (tmpValue.asString().toLowerCase().contains(searchTerm.toLowerCase())) {
						tmpArray.add(jsonObj);
					}
				}
			} else {
				if (jsonObj.get(searchCategory).asString().toLowerCase().contains(searchTerm.toLowerCase())) {
					tmpArray.add(jsonObj);
				}
			}
		}
		
		return tmpArray;
	}

	private boolean Matches(String searchTerm, JsonObject jsonObj, String key) {
		return jsonObj.get(key).asString().toLowerCase().contains(searchTerm.toLowerCase());
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
