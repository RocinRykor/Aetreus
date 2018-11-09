package com.rocinrykor.aetreusbot;

import javax.security.auth.login.LoginException;

import com.rocinrykor.aetreusbot.baxter.Meter;
import com.rocinrykor.aetreusbot.command.Command;
import com.rocinrykor.aetreusbot.discord.BotInfo;
import com.rocinrykor.aetreusbot.discord.BotListener;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BotController {
	
/* 
 * Main Controller for Aetreus
 * On program start, starts Aetreus
 * Used to call new messages and change bot "playing" status
 * */
	static AudioPlayerManager playerManager;
	
	BotInfo info;
	
	private static JDA jda;
	private static Guild guild;
	private static Role adminRole;
	
	static boolean aetreusOnline;
	
	static String presenceDefault = "Type &Help to begin";
	
	public BotController() {
		info = new BotInfo();
		
		StartBot();
		
		Meter.Init();
	}
	
	public JDA getJDA() {
		return jda;
	}
	
	private static void StartBot() {
		
		if (BotInfo.getBOT_TOKEN().equalsIgnoreCase("DEFUALT_TOKEN_PLEASE_CHANGE")) {
			System.out.println("ERROR: A VALID TOKEN HAS NOT BEEN DEFINED \n"
					+ "PLEASE EDIT THE CONFIG FILE AND RESTART THE BOT");
			System.exit(0);
		}
		
		try {
			jda = new JDABuilder(AccountType.BOT).setToken(BotInfo.getBOT_TOKEN()).build();
			jda.addEventListener(new BotListener());
			
			Command.init();
			SetPresence(presenceDefault);
			aetreusOnline = true;
			
		} catch (LoginException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void SetPresence(String presence) {
		jda.getPresence().setGame(Game.playing(presence));
	}
	
	public static void ResetPresence() {
		SetPresence(presenceDefault);
	}

	public static Guild getGuild() {
		return guild;
	}
	
	public static Role getAdminRole() {
		return adminRole;
	}
	
	private static void ShutdownBot() {
		jda.shutdown();
		System.exit(0);
	}
	
	public static void sendMessage(String message, MessageReceivedEvent event) {
		event.getChannel().sendMessage(message).queue();
	}
	
	public static void main(String[] args) {
		BotController botController = new BotController();
	}

	public static void InitVars() {
		guild = (jda.getGuilds().get(0));
		adminRole = jda.getRolesByName("Admins", true).get(0);
	}
}
