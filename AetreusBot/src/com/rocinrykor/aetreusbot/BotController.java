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

import com.rocinrykor.aetreusbot.command.Command;
import com.rocinrykor.aetreusbot.discord.BotListener;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;


public class BotController {
	
/* 
 * Main Controller for Aetreus
 * On program start, starts Aetreus
 * Used to call new messages and change bot "playing" status
 * */
	
	static AudioPlayerManager playerManager;
	
	private static JDA jda;
	private static Guild guild;
	private static Role adminRole;
	private static Role nsfwRole;
	
	static boolean aetreusOnline;
	
	
	public static void main(String[] args) {
		
		System.out.println("Start");
		
		//Loads config file, then runs StartBot()
		ConfigController.Init();
		
		StartSystemTray();
		
	}
	
	public static JDA getJDA() {
		return jda;
	}
	
	static void StartBot() {
		System.out.println("Starting Bot");
		
		try {
			jda = new JDABuilder(AccountType.BOT)
					.setToken(ConfigController
							.getBOT_TOKEN())
					.build();
			
			jda.addEventListener(new BotListener());
			
			Command.init();
			
			ResetPresence();
			
			aetreusOnline = true;
			
		} catch (LoginException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void StartUpdateTimer() {
		//Started by the message listener so that daily message function can be started properly
		
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
		SetPresence(ConfigController.getBOT_MESSAGE());
	}

	public static Guild getGuild() {
		return guild;
	}
	
	public static Role getAdminRole() {
		return adminRole;
	}
	
	public static Role getNSFWRole() {
		return nsfwRole;
	}
	
	private static void ShutdownBot() {
		jda.shutdown();
		System.exit(0);
	}
	
	public static void sendMessage(String message, MessageChannel channel) {
		channel.sendMessage(message).queue();
	}
	
	public static void sendMessage(EmbedBuilder builder, MessageChannel channel) {
		channel.sendMessage(builder.build()).queue();
	}
	
	

	private static void StartSystemTray() {
		if (!SystemTray.isSupported()) {
		      System.out.println("SystemTray is not supported");
		      return;
		    }

		    SystemTray tray = SystemTray.getSystemTray();
		    Toolkit toolkit = Toolkit.getDefaultToolkit();
		    Image image = toolkit.getImage("C:\\Users\\Rocin Rykor\\git\\Aetreus\\AetreusBot\\Ampersand.png"); //Default Icon
		    //Image image = toolkit.getImage("WIP-Icon.png"); //Icon For testing

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
		        ShutdownBot();
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
		nsfwRole = jda.getRolesByName("Adult", true).get(0);
	}
}
