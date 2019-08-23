package studio.rrprojects.aetreusbot.dungeonsanddragons;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.MessageEmbed.Footer;
import studio.rrprojects.aetreusbot.command.Command;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterLoader;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.CharacterContainer;
import studio.rrprojects.aetreusbot.utils.MessageBuilder;
import studio.rrprojects.aetreusbot.utils.MessageTools;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Items extends Command{

	@Override
	public String getName() {
		return "Items";
	}

	@Override
	public String getAlias() {
		return "Item";
	}

	@Override
	public String getHelpDescription() {
		return "D&D - Manages character's items";
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
		
		CharacterContainer character = CharacterLoader.GetPlayerData(cmd.AUTHOR.getName());
		
		HashMap<String, Runnable> subCommands = new HashMap<>();
		subCommands.put("help", () -> HelpCommand(cmd, subCommands));
		subCommands.put("list", () -> ListItems(cmd, character));
		subCommands.put("add", () -> AddItem(cmd, character));
		subCommands.put("remove", () -> RemoveItem(cmd, character));
		//subCommands.put("set", () -> TemplateSet(cmd, character));
		
		if (subCommands.containsKey(cmd.MAIN_ARG.toLowerCase())) {
			subCommands.get(cmd.MAIN_ARG.toLowerCase()).run();
			return;
		}
		
		ListItems(cmd, character);
		
	}
	
	private void AddItem(CommandContainer cmd, CharacterContainer character) {
		int itemQuant = 1;
		int startingPos = 0;
		
		if (cmd.SECONDARY_ARG.length > 0) {
			if (StartsWithDigit(cmd.SECONDARY_ARG[0])) {
				itemQuant = Integer.parseInt(cmd.SECONDARY_ARG[0]);
				startingPos = 1;
			}
			
			String itemName = "";
			
			for (int i = startingPos; i < cmd.SECONDARY_ARG.length; i++) {
				itemName += cmd.SECONDARY_ARG[i];
				if (i != cmd.SECONDARY_ARG.length - 1) {
					itemName += " ";
				}
			}
			
			HashMap<String, Integer> items = character.characterInventory.items;
			items.put(itemName, itemQuant);
		}
		
		ListItems(cmd, character);
	}

	private void RemoveItem(CommandContainer cmd, CharacterContainer character) {
		int itemQuant = 1;
		int startingPos = 0;
		
		if (StartsWithDigit(cmd.SECONDARY_ARG[0])) {
			itemQuant = Integer.parseInt(cmd.SECONDARY_ARG[0]);
			startingPos = 1;
		}
		
		String itemName = "";
		
		for (int i = startingPos; i < cmd.SECONDARY_ARG.length; i++) {
			itemName += cmd.SECONDARY_ARG[i];
			if (i != cmd.SECONDARY_ARG.length - 1) {
				itemName += " ";
			}
		}
		
		HashMap<String, Integer> items = character.characterInventory.items;
		if (items.containsKey(itemName)) {
			int oldValue = items.get(itemName);
			int newValue = oldValue - itemQuant;
			
			if (newValue <= 0) {
				items.remove(itemName);
			} else {
				items.replace(itemName, newValue);
			}
		}
		
		ListItems(cmd, character);
	}

	private void ListItems(CommandContainer cmd, CharacterContainer character) {
		String title = "Character Name: " + character.characterInfo.characterName;
		
		String header = "Character's Items:";
		
		String message = "";
		
		HashMap<String, Integer> items = character.characterInventory.items;
		
		for (HashMap.Entry<String, Integer> entry : items.entrySet()) {
			message += MessageTools.CapatalizeFirst(entry.getKey()) + ": " + entry.getValue() + "\n"; 
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
	
	private void HelpCommand(CommandContainer cmd, HashMap<String, Runnable> subCommands) {
		String message = "Template Command: Lorem ipsum dolor sit amet";
		
		for (Map.Entry<String, Runnable> command: subCommands.entrySet()) {
			message += command.getKey() + "\n";
		}
		
		SendMessage(message, cmd.DESTINATION);
	}
	
	private boolean StartsWithDigit(String input) {
		return Character.isDigit(input.charAt(0));
	}

}
