package studio.rrprojects.aetreusbot.utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.User;
import studio.rrprojects.aetreusbot.InputCollection;

public class NewMessage {

	public static void send(String message, String destination) {
		message = checkEmpty(message);
		send(message, GetInfo.getChannel(destination), null);
	}
	
	public static void send(String message, Channel DESTINATION) {
		send(message, DESTINATION, null);
	}


	public static void send(String message, Channel DESTINATION, User user) {
		message = checkEmpty(message);
		
		if (DESTINATION == null) {
			send(message, user);
			return;
		}
		
		DESTINATION.getJDA().getTextChannelById(DESTINATION.getId()).sendMessage(message).complete();
	}

	public static void send(String message, User user) {
		message = checkEmpty(message);
		
		if (user.isFake()) {
			InputCollection.UpdateArray(message);
			return;
		}
		
		try {
			user.openPrivateChannel().complete().sendMessage(message).complete();
		} catch (Exception e) {
			InputCollection.UpdateArray("ERROR: CAN NOT SEND PRIVATE MESSAGE TO " + user.getName());
		}
	}
	
	private static String checkEmpty(String message) {
		if (message.equalsIgnoreCase("")) {
			return "No message defined!";
		} else {
			return message;
		}
	}

	public static void send(EmbedBuilder message, Channel DESTINATION) {
		if (DESTINATION == null) {
			return;
		}
		
		DESTINATION.getJDA().getTextChannelById(DESTINATION.getId()).sendMessage(message.build()).complete();
		
	}

}
