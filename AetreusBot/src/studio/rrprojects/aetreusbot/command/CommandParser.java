package studio.rrprojects.aetreusbot.command;

import java.time.OffsetDateTime;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.RestAction;
import studio.rrprojects.aetreusbot.Controller;
import studio.rrprojects.aetreusbot.InputCollection;
import studio.rrprojects.aetreusbot.utils.GetInfo;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class CommandParser {

	public static void initialParse(String newInput, MessageReceivedEvent event) {
		String raw = newInput;
		
		//remove any extra space after the prefix before the first command.
		raw = RemoveExtraSpaces(raw);
		raw = RemoveFrontSpaces(raw);
		
		//remove anything after the first word
		String[] breakdown = raw.split(" ");
		String mainCommand = breakdown[0];
		
		//Search for a matching command
		boolean commandFound = false;
		Command passedCommand = null;
		for(Command command : Command.commands) {
			if (mainCommand.equalsIgnoreCase(command.getName()) || mainCommand.equalsIgnoreCase(command.getAlias())) {
				commandFound = true;
				//If Found, store that command
				passedCommand = command;
			}
		}
		
		// if no match is found, stop here.
		if (!commandFound) {
			return;
		}
		
		String commandMods = RemoveFrontSpaces(raw.replaceFirst(mainCommand, ""));
		CommandContainer newCommandContainer = ModifierParser(passedCommand, commandMods, event);
		
		/*
		 * This is where any checks for restriction need to be done
		 * */
		if (passedCommand.isAdminOnly()) {
			if (!UserHasRole("Admins", newCommandContainer)) {
				NewMessage.send("I'm sorry, that command is restricted to Admins and you cannot use it at this time.", newCommandContainer.AUTHOR);
				return;
			}
		}
		
		if (passedCommand.isAdultRestricted()) {
			if (!UserHasRole("Adult", newCommandContainer)) {
				NewMessage.send("I'm sorry, that command is of an Adult nature. \n"
						+ "If you would like to be able to use that command please use the adult command for more info. \n\n"
						+ "Example: &adult", newCommandContainer.AUTHOR);
				return;
			}
		}
		
		if (passedCommand.deleteCallMessage()) {
			try {
				event.getMessage().delete().complete();
			} catch (Exception e) {
				InputCollection.UpdateArray("ERROR: UNABLE TO DELETE MESSAGE");
			}
		}
		//Checks passed - Execute the command
		
		passedCommand.executeMain(newCommandContainer);
		
	}

	private static boolean UserHasRole(String roleName, CommandContainer cmd) {
		if (cmd.AUTHOR.isFake()) {
			return false;
		}
		
		Guild guild = cmd.JDA.getGuilds().get(0);
		Member member = guild.getMember(cmd.AUTHOR);
		Role role = guild.getRolesByName(roleName, true).get(0);
		
		return member.getRoles().contains(role);
		
	}

	private static CommandContainer ModifierParser(Command command, String raw, MessageReceivedEvent event) {
		//All Info that will be needed in the final container
		/*
		 * Author
		 * Channel
		 * Time
		 * 
		 * Destination
		 * 
		 * Main Arg
		 * Secondary Arg
		 * Note
		 * */
		
		User AUTHOR;
		Channel CHANNEL;
		OffsetDateTime TIME;
		
		Channel DESTINATION;
		
		JDA JDA = null;
		
		String MAIN_ARG;
		String[] SECONDARY_ARG = null;
		String NOTE_ARG;
		String TRIMMED_RAW;
		String TRIMMED_NOTE = null;
		
		if (event == null) {
			//Creates fake message details if the command comes from outside discord.
			AUTHOR = new User() {
				
				@Override
				public boolean isFake() {
					return true;
				}
				
				@Override
				public String getAsMention() {
					return "@Console";
				}
				
				@Override
				public long getIdLong() {
					return 0;
				}
				
				@Override
				public RestAction<PrivateChannel> openPrivateChannel() {
					return null;
				}
				
				@Override
				public boolean isBot() {
					return false;
				}
				
				@Override
				public boolean hasPrivateChannel() {
					return false;
				}
				
				@Override
				public String getName() {
					return "Console";
				}
				
				@Override
				public List<Guild> getMutualGuilds() {
					return null;
				}
				
				@Override
				public JDA getJDA() {
					return Controller.getJda();
				}
				
				@Override
				public String getEffectiveAvatarUrl() {
					return null;
				}
				
				@Override
				public String getDiscriminator() {
					return null;
				}
				
				@Override
				public String getDefaultAvatarUrl() {
					return null;
				}
				
				@Override
				public String getDefaultAvatarId() {
					return null;
				}
				
				@Override
				public String getAvatarUrl() {
					return null;
				}
				
				@Override
				public String getAvatarId() {
					return null;
				}
			};
			
			CHANNEL = GetInfo.getChannel(command.getHomeChannel());
			TIME = OffsetDateTime.now();
			
			DESTINATION = GetInfo.getChannel(command.getHomeChannel());
		} else {
			AUTHOR = event.getAuthor();
			CHANNEL = event.getTextChannel();
			TIME = event.getMessage().getCreationTime();
			JDA = event.getJDA();
			
			DESTINATION = CheckChannelRestriction(command, event);
		}
		
		if (raw.contains("\"")) { //Check if beheaded string has a note and if so extract it.
			Integer quotationIndex = raw.indexOf("\"");

			TRIMMED_RAW = raw.substring(0, quotationIndex);
			NOTE_ARG = raw.substring(quotationIndex, raw.length());
			
			TRIMMED_NOTE = NOTE_ARG.replace("\"", "");
			
		} else {
			TRIMMED_RAW = raw;
		}
		
		String[] argBreakdown = TRIMMED_RAW.split(" ");
		
		if (argBreakdown.length > 0) {
			MAIN_ARG = argBreakdown[0];
			
			if (argBreakdown.length > 1) {
				SECONDARY_ARG = new String[argBreakdown.length - 1];
				for (int i = 0; i < SECONDARY_ARG.length; i++) {
					SECONDARY_ARG[i] = argBreakdown[i + 1];
				}
			}
			
		} else {
			MAIN_ARG = null;
			SECONDARY_ARG = null;
		}
		
		
		return new CommandContainer(AUTHOR, CHANNEL, TIME, DESTINATION, JDA, MAIN_ARG, SECONDARY_ARG, TRIMMED_RAW, TRIMMED_NOTE);
	}

	private static Channel CheckChannelRestriction(Command command, MessageReceivedEvent event) {
		if (command.isChannelRestricted()) {
			if (CompareToValidChannels(command, event)) {
				return event.getTextChannel();
			} else {
				NewMessage.send("I'm sorry, but my master wishes to keep the channels clear, so certain commands are restricted so that they may not show up in the General or Tabletop channels. \n\n"
						+ "The results of your command have been forwarded to the proper channel.", event.getAuthor());
				event.getMessage().delete().complete();
				return event.getJDA().getTextChannelsByName(command.getHomeChannel(), true).get(0);
			}
		}
		
		return event.getTextChannel();
	}

	private static boolean CompareToValidChannels(Command command, MessageReceivedEvent event) {
		if (event.getPrivateChannel() != null) {
			return true;
		}
		
		String incomingChannelName = event.getTextChannel().getName();
		
		if (incomingChannelName.equalsIgnoreCase("bottesting") || incomingChannelName.equalsIgnoreCase(command.getHomeChannel()) || incomingChannelName.equalsIgnoreCase("gm-screen")) {
			return true;
		} else {
			return false;
		}
	}

	private static String RemoveExtraSpaces(String input) {
		input = input.trim().replaceAll("[ ]{2,}", " "); //Remove extra spaces
		
		return input;
	}

	private static String RemoveFrontSpaces(String input) {
		while (input.startsWith(" ")) {
			input = input.replaceFirst(" ", "");
		}
		return input;
	}
	
	public static class CommandContainer { //Stores all of the parsed variables for later use
		public final User AUTHOR;
		public final Channel CHANNEL;
		public final OffsetDateTime TIME;
		public final Channel DESTINATION;
		public final JDA JDA;
		public final String MAIN_ARG;
		public final String[] SECONDARY_ARG;
		public final String TRIMMED_RAW;
		public final String TRIMMED_NOTE;
		
		
		public CommandContainer(User AUTHOR, Channel CHANNEL, OffsetDateTime TIME, Channel DESTINATION, JDA JDA, String MAIN_ARG, String[] SECONDARY_ARG, String TRIMMED_RAW, String TRIMMED_NOTE) {
			this.AUTHOR = AUTHOR;
			this.CHANNEL = CHANNEL;
			this.TIME = TIME;
			this.DESTINATION = DESTINATION;
			this.JDA = JDA;
			this.MAIN_ARG = MAIN_ARG;
			this.SECONDARY_ARG = SECONDARY_ARG;
			this.TRIMMED_RAW = TRIMMED_RAW;
			this.TRIMMED_NOTE = TRIMMED_NOTE;
		}
	}

}
