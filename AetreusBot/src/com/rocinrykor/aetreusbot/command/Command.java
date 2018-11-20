package com.rocinrykor.aetreusbot.command;

import java.util.ArrayList;

import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;
import com.rocinrykor.aetreusbot.utils.UserInfo;

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
		commands.add(new Baxter());
		commands.add(new Flood());
		commands.add(new Talk());
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
			return UserInfo.isAdmin(user);
		} else {
			return true;
		}
	}
	
	public boolean checkChannelRestriction() {
		return isChannelRestricted();
	}
	
	public boolean checkDeleteCallMessage() {
		return deleteCallMessage();
	}

	public abstract String getName();
	
	public abstract String getDescription();
	
	public abstract String getAlias();
	
	public abstract String helpMessage();

	public abstract boolean isAdminOnly();
	
	public abstract boolean isChannelRestricted();
	
	public abstract boolean deleteCallMessage();

	public abstract void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event, CommandContainer cmd);
	
	public abstract void sendMessage(String message, MessageReceivedEvent event);
}
