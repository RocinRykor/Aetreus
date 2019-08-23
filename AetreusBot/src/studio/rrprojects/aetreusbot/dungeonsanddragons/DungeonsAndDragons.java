package studio.rrprojects.aetreusbot.dungeonsanddragons;

import java.util.HashMap;

import net.dv8tion.jda.core.entities.Channel;
import studio.rrprojects.aetreusbot.command.Command;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterLoader;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.ContainerToJsonWriter;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.CharacterContainer;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class DungeonsAndDragons extends Command{

	@Override
	public String getName() {
		return "DungeonsAndDragons";
	}

	@Override
	public String getAlias() {
		return "DnD";
	}

	@Override
	public String getHelpDescription() {
		return "This tool will help manage your D&D characters.";
	}

	@Override
	public String getHomeChannel() {
		return "Rolling";
	}

	@Override
	public boolean isChannelRestricted() {
		return true;
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
		CharacterContainer character = CharacterLoader.GetPlayerData(cmd.AUTHOR.getName());
		
		HashMap<String, Runnable> subCommands = new HashMap<>();
		subCommands.put("list", () -> ListCharacters(cmd, character));
		subCommands.put("save", () -> SaveCharacter(cmd, character));
		subCommands.put("help", () -> HelpCommand(cmd, subCommands));

		if (subCommands.containsKey(cmd.MAIN_ARG.toLowerCase())) {
			subCommands.get(cmd.MAIN_ARG.toLowerCase()).run();
			return;
		}
	}
	
	private void SaveCharacter(CommandContainer cmd, CharacterContainer character) {
		ContainerToJsonWriter.ParseContainer(cmd, character);
	}

	private void ListCharacters(CommandContainer cmd, CharacterContainer character) {
	}

	private void HelpCommand(CommandContainer cmd, HashMap<String, Runnable> subCommands) {
	}

	private void SendMessage(String message, Channel DESTINATION) {
		NewMessage.send(message, DESTINATION);
	}

}
