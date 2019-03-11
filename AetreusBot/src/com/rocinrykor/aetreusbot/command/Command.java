package com.rocinrykor.aetreusbot.command;

import java.util.ArrayList;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;
import com.rocinrykor.aetreusbot.utils.UserInfo;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command {

	public static ArrayList<Command> commands;

	public static void init() {
		commands = new ArrayList<>();

		commands.add(new Ping());
		commands.add(new Help());
		commands.add(new Roll());
		commands.add(new Shadowrun());
		commands.add(new Delete());
		commands.add(new Audio());
		commands.add(new Flood());
		commands.add(new Talk());
		commands.add(new Nyan());
		commands.add(new Adult());
	}

	public boolean compare(String main) {
		if (main.equalsIgnoreCase(getAlias())) {
			return true;
		} else if (main.equalsIgnoreCase(getName())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasPermission(User user) {
		if(isAdminOnly()) {
			return UserInfo.hasRole(user, BotController.getAdminRole());
		} else {
			return true;
		}
	}
	
	public boolean isAdult(User user) {
		if(isAdultResricted()) {
			return UserInfo.hasRole(user, BotController.getNSFWRole());
		} else {
			return true;
		}
	}
	
	public boolean checkChannelRestriction() {
		
		if (isChannelRestricted() || isAdultResricted()) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public boolean checkDeleteCallMessage() {
		return deleteCallMessage();
	}

	public abstract String getName();
	
	public abstract String getDescription();
	
	public abstract String getAlias();
	
	//This is the text channel that the command should be forwarded to if the command is posted in a restricted channel.
	public abstract String getHomeChannel();
	
	public abstract String helpMessage();

	public abstract boolean isAdminOnly();
	
	public abstract boolean isChannelRestricted();
	
	public abstract boolean isAdultResricted();
	
	public abstract boolean deleteCallMessage();
	
	public void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event, CommandContainer cmd, MessageChannel outputChannel) {}	
	
	public abstract void sendMessage(EmbedBuilder builder, MessageChannel channel);

	public abstract void sendMessage(String message, MessageChannel channel);
}
