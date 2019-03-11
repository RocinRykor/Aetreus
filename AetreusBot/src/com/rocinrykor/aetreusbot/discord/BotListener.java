package com.rocinrykor.aetreusbot.discord;

import java.util.ArrayList;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.ConfigController;
import com.rocinrykor.aetreusbot.command.Command;
import com.rocinrykor.aetreusbot.command.CommandParser;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.user.update.GenericUserPresenceEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter{
	
	/* 
	 * 
	 * */
	
	public static ArrayList<String> channelsRestricted, channelsNSFW;
	
	private String errorRestricted, errorPermissions, errorNSFW, errorNSFWChannel;
	
	public BotListener() {
		InitRestrictedChannels();
		InitErrorMSG();
	}
	
	private void InitErrorMSG() {
		errorRestricted = "I'm sorry, but my master wishes to keep the channels clear, so certain commands are restricted so that they may not show up in the General or Tabletop channels. \n"
				+ "I have moved the results of the command to the appropiate channel. \n"
				+ "In the future please use the correct channel for such commands, such as Rolling, or you can always use the command in this private discussion with me.";
		
		errorPermissions = "I'm sorry, but my master has not yet made you an admin so that you can use that command. \n"
				+ " If you wish to use that command in the future, please discuss the matter with him.";
		
		errorNSFW = "I'm sorry, but that command is of an adult nature and thus I cannot allow you to use that command, especially outside of the designated text channel. \n"
				+ "If you would like to use that command, and any others that may be designated as adult-only, please respond with the following command: \n\n"
				+ "&adult \"I Understand\" ";
		
		errorNSFWChannel = "Please keep commands of that nature to the NSFW Channel. \n"
				+ "I have displayed the results of this command over in the appropiate channel.";
	}

	private void InitRestrictedChannels() {
		/*
		 * channelsRestricted = list of servers that cannot be used if a command is listed as being channel restricted
		 * channelsNSFW = list of servers that will be valid homes for any and all commands designated as adult only
		 * 
		 * TODO - Add these channels to config file instead.
		 * */

		channelsRestricted = new ArrayList<>();
		channelsRestricted.add("general");
		channelsRestricted.add("tabletop");
		
		channelsNSFW = new ArrayList<>();
		channelsNSFW.add("nsfw");
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		super.onReady(event);
		
		BotController.InitVars();
		System.out.println("Bot Ready!");
		
		BotController.StartUpdateTimer();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		/*
		 * With each message, check if it is a valid call to the bot -> check if command is valid -> compare against channel restriction and user permission if needed -> execute the command
		 * */
		
		String rawInput = event.getMessage().getContentRaw();
		boolean isBot = event.getAuthor().isBot();
		String hasPrefix = CheckForPrefix(rawInput);
		
		
		
		if (hasPrefix != null && !isBot) {
			CommandContainer cmd = (CommandParser.parse(rawInput, hasPrefix, event)); //Sends the message to be parsed
			
			/*
			 * Search the list of valid commands for a match.
			 * Check user permission for the command -> stop process, delete message, and warn user if permissions check fails
			 * Delete call message if needed
			 * Check that the command was made in a non-restricted channel, move to forward the output if needed
			 * Execute the command, with proper forwarding if needed
			 * */
			
			boolean commandFound = false;
			for(Command command : Command.commands) {
				if (command.compare(cmd.mainCommand)) {
					commandFound = true;
					
					if(!command.isAdult(cmd.user)) {
						event.getAuthor().openPrivateChannel().complete()
							.sendMessage(errorNSFW).queue();;
						DeleteCallMessage(event);
						return;
					}
					
					if(!command.hasPermission(cmd.user)) {
						event.getAuthor().openPrivateChannel().complete()
							.sendMessage(errorPermissions).queue();;
						DeleteCallMessage(event);
						return;
					}
					
					MessageChannel outputChannel = event.getChannel();
					boolean flagWarnUser = false;
					String message = null;
					if(command.checkChannelRestriction()) {
						String currentChannel = event.getChannel().getName();
						
						//Check for restricted channels
						if (command.isChannelRestricted()) {
							if (ChannelMatches(currentChannel, channelsRestricted)) {
								flagWarnUser = true;
								message = errorRestricted;
								DeleteCallMessage(event);
								outputChannel = BotController.getGuild().getTextChannelsByName(command.getHomeChannel(), true).get(0);
							}
						}
						
						//Check for adult only restriction
						if (command.isAdultResricted()) {
	
							if (!ChannelMatches(currentChannel, channelsNSFW) && event.getPrivateChannel() == null) {
								flagWarnUser = true;
								message = errorNSFWChannel;
								DeleteCallMessage(event);
								outputChannel = BotController.getGuild().getTextChannelsByName(command.getHomeChannel(), true).get(0);
							}
							
						}
						
						if (flagWarnUser) {
							event.getAuthor().openPrivateChannel().complete()
							.sendMessage(message).queue();
						}
						
					}
					
					if (command.checkDeleteCallMessage()) {
						DeleteCallMessage(event);
					}
					
					command.execute(cmd.primaryArg, cmd.secondaryArg, cmd.trimmedNote, event, cmd, outputChannel);
				}
			}
		}

		
	}

	
	private String CheckForPrefix(String rawInput) {
		
		if (rawInput.startsWith(ConfigController.getBOT_PREFIX())) {
			return ConfigController.getBOT_PREFIX();
		} else if (rawInput.startsWith(ConfigController.getBOT_SECONDARY_PREFIX())) {
			return ConfigController.getBOT_SECONDARY_PREFIX();
		} else {
			return null;
		}

	}

	private boolean ChannelMatches(String currentChannel, ArrayList<String> ChannelsList) {
		for (String channelName : ChannelsList) {
			if(channelName.equalsIgnoreCase(currentChannel)) {
				return true;
			}
		} 
		return false;
	}


	private boolean isRestrictedChannel(MessageReceivedEvent event) {
		String currentChannel = event.getChannel().getName();
		
		for (String channelName : channelsRestricted) {
			if(channelName.equalsIgnoreCase(currentChannel)) {
				return true;
			}
		}
		
		return false;
	}

	private void DeleteCallMessage(MessageReceivedEvent event) {
		//Will only try to delete the call message if it was not sent in a private channel
		if (event.getPrivateChannel() == null) {
			try {
				event.getMessage().delete().queue();
			} catch (Exception e) {
				System.out.println("ERROR: CANNOT DELETE THAT MESSAGE!");
			}
		}
		
	}
	
}
