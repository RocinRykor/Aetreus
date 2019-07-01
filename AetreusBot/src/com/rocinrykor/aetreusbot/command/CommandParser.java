package com.rocinrykor.aetreusbot.command;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandParser {
	
	public static CommandContainer parse(String rawInput, String prefix, MessageReceivedEvent event) {

		String raw = rawInput; //Take the raw input
		String beheadedString = raw.replaceFirst(prefix , ""); //Remove the Ampersand (Bot signal key)
		String trimmedString = null; //This will be the beheaded string minus any note that is attached
		String noteArg = null; //This will be the removed note from the beheaded string
		
		String[] arg; //All arguments before being categorized
		String mainCommand = null; //Main action the bot will take such as rolling or managing items and equipment -Required-
		String primaryArg = null; //This is the main argument that will tell the bot what to do with the main action -Required-
		String[] secondaryArg = null; //Any additional optional arguments, if using can have 1 or more of these -Not Always Required-
		String trimmedNote = null; //Note string minus quotation marks, will be passed as its own arg for description purposes -Not Required-
		
		if (beheadedString.contains("\"")) { //Check if beheaded string has a note and if so extract it.
			Integer quotationIndex = beheadedString.indexOf("\"");

			trimmedString = beheadedString.substring(0, quotationIndex - 1);
			noteArg = beheadedString.substring(quotationIndex, beheadedString.length());
			
			trimmedNote = noteArg.replace("\"", "");

		} else {
			trimmedString = beheadedString;
		}
		
		arg = trimmedString.split(" "); //Takes the main string and breaks it into arguments
		
		
		if (arg.length >= 1) {
			mainCommand = arg[0].toLowerCase(); //First argument becomes the Main Command
			
			if (arg.length >= 2) {
				primaryArg = arg[1].toLowerCase(); //Second argument becomes the Primary argument
				if (arg.length > 2) { //If there are arguments after the second, they become secondary arguments
					secondaryArg = new String[arg.length - 2];
					for (int i = 0; i < secondaryArg.length; i++) {
						secondaryArg[i] = arg[i+2];
					}
				} 
			} else {
				primaryArg = "none";
			} 
		} else {
			System.out.println("Needs Main Command");
			System.out.println("Sorry that message does not have a main command");
		}
		
		Guild guild = event.getGuild();
		User user = event.getAuthor();

		return new CommandContainer(raw, beheadedString, mainCommand, primaryArg, secondaryArg, trimmedNote, guild, user, event);

	}
	
	public static class CommandContainer { //Stores all of the parsed variables for later use
		public final String raw;
		public final String beheadedString;
		public final String mainCommand;
		public final String primaryArg;
		public final String[] secondaryArg;
		public final String trimmedNote;
		public Guild guild;
		public User user;
		public final MessageReceivedEvent event;

		//public final MessageReceivedEvent event;
		
		public CommandContainer(String raw, String beheadedString, String mainCommand, String primaryArg, String[] secondaryArg, String trimmedNote, Guild guild, User user, MessageReceivedEvent event) {
			this.raw = raw;
			this.beheadedString = beheadedString;
			this.mainCommand = mainCommand;
			this.primaryArg = primaryArg;
			this.secondaryArg = secondaryArg;
			this.trimmedNote = trimmedNote;
			this.guild = guild;
			this.user = user;
			this.event = event;
		}
	}
	
}
