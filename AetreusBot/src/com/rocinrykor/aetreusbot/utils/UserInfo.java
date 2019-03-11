package com.rocinrykor.aetreusbot.utils;

import com.rocinrykor.aetreusbot.BotController;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

public class UserInfo {

	static Guild guild = BotController.getGuild();
	
	public static boolean hasRole(User user, Role role) {
		return user.getId().equals(guild.getOwner().getUser().getId()) || (role != null && guild.getMember(user).getRoles().contains(role));
	}

}
