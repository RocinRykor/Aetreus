package com.rocinrykor.aetreusbot.command;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ping extends Command {

	@Override
	public String getName() {
		return "Ping";
	}
	
	@Override
	public String getAlias() {
		return "p";
	}

	@Override
	public boolean isAdminOnly() {
		return false;
	}

	@Override
	public String getDescription() {
		return "Debug Command: Ping the bot to get a response back.";
	}
	
	@Override
	public String helpMessage() {
		// TODO Auto-generated method stub
		return "On a successful ping, I should respond with \"PONG!\", if I do not something has gone wrong and you should talk to my master.";
	}

	@Override
	public boolean isChannelRestricted() {
		return true;
	}
	
	@Override
	public boolean deleteCallMessage() {
		return false;
	}
	
	@Override
	public void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event, CommandContainer cmd) {
		String message = "PONG!";
		sendMessage(message, event);
	}

	@Override
	public void sendMessage(String string, MessageReceivedEvent event) {
		BotController.sendMessage("PONG!", event);
	}

}
