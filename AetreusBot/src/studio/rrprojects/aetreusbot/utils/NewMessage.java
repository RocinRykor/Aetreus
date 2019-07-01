package studio.rrprojects.aetreusbot.utils;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.User;
import studio.rrprojects.aetreusbot.InputCollection;

public class NewMessage {

	public static void send(String message, String destination) {
		message = checkEmpty(message);
		send(message, GetInfo.getChannel(destination));
	}

	public static void send(String message, Channel DESTINATION) {
		message = checkEmpty(message);
		DESTINATION.getJDA().getTextChannelById(DESTINATION.getId()).sendMessage(message).complete();
	}

	public static void send(String message, User user) {
		message = checkEmpty(message);
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

}
