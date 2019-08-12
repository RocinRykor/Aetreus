package studio.rrprojects.aetreusbot.utils;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.User;
import studio.rrprojects.aetreusbot.Controller;

public class GetInfo {

	public static Channel getChannel(String channelName) {
		
		try {
			return Controller.getJda().getTextChannelsByName(channelName, true).get(0);
		} catch (Exception e) {
			return null;
		}
		
	}

	public static User GetUser(String user) {
		System.out.println(Controller.getJda().getUsersByName(user, true).toString());
		
		try {
			return Controller.getJda().getUsersByName(user, true).get(0);
		} catch (Exception e) {
			return null;
		}
	}
}
