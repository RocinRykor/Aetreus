package studio.rrprojects.aetreusbot.dungeonsanddragons;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import studio.rrprojects.aetreusbot.command.Command;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterLoader;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.CharacterContainer;
import studio.rrprojects.aetreusbot.utils.MessageBuilder;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Template extends Command{

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getAlias() {
		return null;
	}

	@Override
	public String getHelpDescription() {
		return null;
	}

	@Override
	public String getHomeChannel() {
		return null;
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
		//subCommands.put("list", () -> TemplateList(cmd, character));
		//subCommands.put("add", () -> TemplateAdd(cmd, character));
		//put("remove", () -> TemplateRemove(cmd, character));
		//subCommands.put("set", () -> TemplateSet(cmd, character));
		
		if (subCommands.containsKey(cmd.MAIN_ARG.toLowerCase())) {
			subCommands.get(cmd.MAIN_ARG.toLowerCase()).run();
			return;
		}
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
