package com.rocinrykor.aetreusbot.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Testing extends Command {

	@Override
	public String getName() {
		return "Testing";
	}

	@Override
	public String getDescription() {
		return "A placeholder command to test whatever new function is being added";
	}

	@Override
	public String getAlias() {
		return "test";
	}

	@Override
	public String helpMessage() {
		return "I'm afraid I can't help you too much with this one, as I'm never too sure what it is supposed to be doing.";
	}

	@Override
	public boolean isAdminOnly() {
		return true;
	}

	@Override
	public boolean isChannelRestricted() {
		return true;
	}

	@Override
	public boolean deleteCallMessage() {
		return true;
	}

	@Override
	public void sendMessage(String message, MessageReceivedEvent event) {
	}
}
