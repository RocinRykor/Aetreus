package com.rocinrykor.aetreusbot.command;

import java.awt.Color;
import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help extends Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Help";
	}
	
	@Override
	public String getAlias() {
		return "h";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "As Primary: List all commands | As Argument: Get detailed information on that commands.";
	}
	
	@Override
	public String helpMessage() {
		// TODO Auto-generated method stub
		return "Yes, that is a prime example of using this command as an argument!";
	}
	
	@Override
	public String getHomeChannel() {
		return "general";
	}

	@Override
	public boolean isAdminOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChannelRestricted() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isAdultResricted() {
		return false;
	}

	@Override
	public boolean deleteCallMessage() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event,
			CommandContainer cmd, MessageChannel channel) {
		EmbedBuilder builder = new EmbedBuilder();
		
		builder.setTitle("List of Commands");
		builder.setColor(Color.CYAN);
		
		for(Command command : Command.commands) {
			builder.addField(command.getName(), command.getDescription(), false);
		}
		
		event.getChannel().sendMessage(builder.build()).queue();
		
	}

	public void sendMessage(EmbedBuilder builder, MessageChannel channel) {
		BotController.sendMessage(builder, channel);
	}

	public void sendMessage(String message, MessageChannel channel) {
		BotController.sendMessage(message, channel);
	}
	
}
