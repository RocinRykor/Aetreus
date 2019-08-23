package studio.rrprojects.aetreusbot.dungeonsanddragons;

import java.awt.Color;
import java.util.HashMap;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import studio.rrprojects.aetreusbot.command.Command;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterLoader;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.CharacterContainer;
import studio.rrprojects.aetreusbot.utils.MessageBuilder;
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
		
		String title = "Character Name: " + character.characterInfo.characterName;
		
		String header = "Character's Item's:";
		
		String message = "";
		
		HashMap<String, Integer> items = character.characterInventory.items;
		
		for (HashMap.Entry<String, Integer> entry : items.entrySet()) {
			message += entry.getKey() + ": " + entry.getValue() + "\n"; 
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

}
