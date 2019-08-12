package studio.rrprojects.aetreusbot.command;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.utils.GetInfo;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Message extends Command {

	@Override
	public String getName() {
		return "Message";
	}

	@Override
	public String getAlias() {
		return "M";
	}

	@Override
	public String getHelpDescription() {
		return "Use Aetreus to deliever a message to any channel/user";
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
		if (cmd.MAIN_ARG == null) {
			SendMessage("I'm Sorry, I need a target so I know where to send your message.", cmd.AUTHOR);
			return;
		}
		
		if (cmd.TRIMMED_NOTE == null) {
			SendMessage("I'm Sorry, but you did not include a message for me to send.", cmd.AUTHOR);
			return;
		}
		
		String target = cmd.TRIMMED_RAW.replaceFirst("message ", "");
		
		String message = "New message from " + cmd.AUTHOR.getName() + ": " + cmd.TRIMMED_NOTE;
		
		System.out.println(target);
		
		if (GetInfo.getChannel(target) != null) {
			SendMessage(message, GetInfo.getChannel(target));
		} else if (GetInfo.GetUser(target) != null){
			SendMessage(message, GetInfo.GetUser(target));
		} else {
			SendMessage("I'm Sorry, I can't find that target.", cmd.AUTHOR);
		}
		
	}
	
	private void SendMessage(String message, User user) {
		NewMessage.send(message, user);
	}

	private void SendMessage(String message, Channel destination) {
		NewMessage.send(message, destination);
	}

}
