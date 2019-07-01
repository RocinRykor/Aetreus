package studio.rrprojects.aetreusbot.dungeonsanddragons;

import net.dv8tion.jda.core.entities.Channel;
import studio.rrprojects.aetreusbot.command.Command;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
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
	public boolean deleteCallMessage() {
		return false;
	}
	
	public void executeMain(CommandContainer cmd) {
		String responce = cmd.AUTHOR.getAsMention() + " Ping Recieved!";
		
		SendMessage(responce, cmd.DESTINATION);
	}
	
	private void SendMessage(String message, Channel DESTINATION) {
		NewMessage.send(message, DESTINATION);
	}

}
