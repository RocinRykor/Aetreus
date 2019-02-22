package com.rocinrykor.aetreusbot;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;

import com.rocinrykor.aetreusbot.baxter.Meter;
import com.rocinrykor.aetreusbot.command.Command;
import com.rocinrykor.aetreusbot.discord.BotInfo;
import com.rocinrykor.aetreusbot.discord.BotListener;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
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
	
	public static JDA getJDA() {
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
	
	public static void StartUpdateTimer() {
		int sleepTime = 900;
		
		DailyMessage.Init();
		
		new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(sleepTime * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					DailyMessage.Update();
				}
			}
		}.start();
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
	
	public static void sendMessage(EmbedBuilder builder, MessageReceivedEvent event) {
		event.getChannel().sendMessage(builder.build()).queue();
	}
	
	public static void main(String[] args) {
		StartSystemTray();
		BotController botController = new BotController();
	}

	private static void StartSystemTray() {
		if (!SystemTray.isSupported()) {
		      System.out.println("SystemTray is not supported");
		      return;
		    }

		    SystemTray tray = SystemTray.getSystemTray();
		    Toolkit toolkit = Toolkit.getDefaultToolkit();
		    Image image = toolkit.getImage("C:\\Users\\Rocin Rykor\\git\\Aetreus\\AetreusBot\\Ampersand.png");

		    PopupMenu menu = new PopupMenu();

		    MenuItem messageItem = new MenuItem("Show Message");
		    messageItem.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        JOptionPane.showMessageDialog(null, "Hello!");
		      }
		    });
		    menu.add(messageItem);

		    MenuItem closeItem = new MenuItem("Exit");
		    closeItem.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        System.exit(0);
		      }
		    });
		    menu.add(closeItem);
		    TrayIcon icon = new TrayIcon(image, "Discord Bot - Aetreus", menu);
		    icon.setImageAutoSize(true);

		    try {
				tray.add(icon);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
	}

	public static void InitVars() {
		guild = (jda.getGuilds().get(0));
		adminRole = jda.getRolesByName("Admins", true).get(0);
	}
}
