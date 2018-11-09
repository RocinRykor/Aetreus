package com.rocinrykor.aetreusbot.utils;

import com.rocinrykor.aetreusbot.BotController;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

public class UserInfo {

	public static boolean isAdmin(User user) {
		Guild guild = BotController.getGuild();
		Role adminRole = BotController.getAdminRole();
		return user.getId().equals(guild.getOwner().getUser().getId()) || (adminRole != null && guild.getMember(user).getRoles().contains(adminRole));
	}
}
