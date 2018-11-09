package com.rocinrykor.aetreusbot.discord;

import java.util.ArrayList;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.Command;
import com.rocinrykor.aetreusbot.command.CommandParser;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.user.update.GenericUserPresenceEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter{
	public static ArrayList<String> validChannels;
	
	private String errorRestricted;
	
	public BotListener() {
		InitChannels();
		InitErrorMSG();
	}
	
	private void InitErrorMSG() {
		errorRestricted = "I'm sorry, but my master wishes to keep the channels clear, so certian commands are restricted to the Rolling and BotTesting text channels. \n"
				+ "Please try your command again in one of those channels.";
	}

	private void InitChannels() {
		validChannels = new ArrayList<>();
		
		validChannels.add("rolling");
		validChannels.add("bottesting");
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		super.onReady(event);
		
		System.out.println("Bot Ready!");
		BotController.InitVars();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String rawInput = event.getMessage().getContentRaw();
		boolean isBot = event.getAuthor().isBot();

		if (rawInput.startsWith(BotInfo.getBOT_PREFIX()) && !isBot) {
			
			CommandContainer cmd = (CommandParser.parse(rawInput, event)); //Sends the message to be parsed
			
			boolean commandFound = false;
			
			for(Command command : Command.commands) {
				if (command.compare(cmd.mainCommand)) {
					commandFound = true;
					
					if (command.checkDeleteCallMessage()) {
						DeleteCallMessage(event);
					}
					
					if(command.checkChannelRestriction()) {
						if (!isValidChannel(event)) {
							DeleteCallMessage(event);
							event.getAuthor().openPrivateChannel().complete().sendMessage(errorRestricted).queue();
							break;
						} 
					}

					if(command.hasPermission(cmd.user)) {
						command.execute(cmd.primaryArg, cmd.secondaryArg, cmd.trimmedNote, cmd.event, cmd);
					} else {
						event.getChannel().sendMessage(cmd.user.getAsMention() + ", you are not allowed to use this command.").queue();
					}

					break;
				}
			}
			
			if (commandFound) {
				System.out.println("Command Found");
				return;
			}
			
			event.getChannel().sendMessage("Unknown Command").queue();
		}
	}

	
	private boolean isValidChannel(MessageReceivedEvent event) {
		String channel = event.getChannel().getName();
		
		for (String validChannel : validChannels) {
			if(validChannel.equalsIgnoreCase(channel)) {
				return true;
			}
		}
		
		return false;
	}

	private void DeleteCallMessage(MessageReceivedEvent event) {
		try {
			event.getMessage().delete().queue();
		} catch (Exception e) {
			System.out.println("ERROR: CANNOT DELETE THAT MESSAGE!");
		}
	}
	
}
