package studio.rrprojects.aetreusbot.command;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.User;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Ping extends Command {

	@Override
	public String getName() {
		return "Ping";
	}

	@Override
	public String getAlias() {
		return "P";
	}

	@Override
	public String getHelpDescription() {
		return "Simple command responds with \"PING RECIEVED\" upon successful execution.";
	}

	@Override
	public String getHomeChannel() {
		return "bottesting";
	}

	@Override
	public boolean isChannelRestricted() {
		return false;
	}
	
	@Override
	public boolean isAdultRestricted() {
		return false;
	}

	@Override
	public boolean isAdminOnly() {
		return false;
	}

	@Override
	public boolean deleteCallMessage() {
		return true;
	}
	
	public void executeMain(CommandContainer cmd) {
		String response = cmd.AUTHOR.getAsMention() + " Ping Recieved!";
		
		SendMessage(response, cmd.DESTINATION, cmd.AUTHOR);
	}
	
	private void SendMessage(String message, Channel DESTINATION, User user) {
		NewMessage.send(message, DESTINATION, user);
	}
}
