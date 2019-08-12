package studio.rrprojects.aetreusbot.command;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.User;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Help extends Command {

	@Override
	public String getName() {
		return "Help";
	}

	@Override
	public String getAlias() {
		return "H";
	}

	@Override
	public String getHelpDescription() {
		return "Lists all other commands and their description.";
	}

	@Override
	public String getHomeChannel() {
		return "General";
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
		return true;
	}
	
	public void executeMain(CommandContainer cmd) {
		String message = "```\n";
		
		for(Command command : Command.commands) { 
			message += command.getName() + ": " + command.getHelpDescription() + "\n\n";
		}
		
		message += "```";
		
		SendMessage(message, cmd.DESTINATION, cmd.AUTHOR);
	}
	
	private void SendMessage(String message, Channel DESTINATION, User user) {
		NewMessage.send(message, DESTINATION, user);
	}
}
