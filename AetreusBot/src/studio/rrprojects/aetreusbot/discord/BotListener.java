package studio.rrprojects.aetreusbot.discord;

import java.util.ArrayList;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import studio.rrprojects.aetreusbot.ConfigController;
import studio.rrprojects.aetreusbot.Controller;
import studio.rrprojects.aetreusbot.InputCollection;
import studio.rrprojects.aetreusbot.utils.GetInfo;

public class BotListener extends ListenerAdapter {
public static ArrayList<Channel> channelsRestricted, channelsNSFW;
	
	private String errorRestricted, errorPermissions, errorNSFW, errorNSFWChannel;
	
	public static MessageReceivedEvent _event;
	
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
		channelsRestricted.add(GetInfo.getChannel("general"));
		channelsRestricted.add(GetInfo.getChannel("tabletop"));
		
		channelsNSFW = new ArrayList<>();
		channelsNSFW.add(GetInfo.getChannel("nsfw"));
	}
	
	public void onMessageReceived(MessageReceivedEvent event) {
		
		String rawInput = event.getMessage().getContentRaw();
		boolean isBot = event.getAuthor().isBot();
		String hasPrefix = CheckForPrefix(rawInput);
		String beheadedInput;
		
		_event = event;
		
		if (event.getChannelType().compareTo(ChannelType.PRIVATE) == 0 && !isBot) {
			InputCollection.UpdateArray(event.getAuthor().getName() + ": " + rawInput);
		}
		
		if (hasPrefix != null && !isBot) {
			beheadedInput = rawInput.replaceFirst(hasPrefix, "");
		} else {
			return;
		}
		
		InputCollection.ParseNewInput(beheadedInput, event);
	}
	
	@Override
		public void onReady(ReadyEvent event) {
			super.onReady(event);
			Controller.PostLoadInits();
			Controller.StartUpdateTimer();
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
	
}
