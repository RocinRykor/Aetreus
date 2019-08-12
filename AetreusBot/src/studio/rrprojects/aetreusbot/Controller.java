package studio.rrprojects.aetreusbot;

import java.io.File;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import studio.rrprojects.aetreusbot.command.Command;
import studio.rrprojects.aetreusbot.discord.BotListener;
import studio.rrprojects.aetreusbot.dungeonsanddragons.CharacterFileLoader;
import studio.rrprojects.aetreusbot.gui.MainWindowController;
import studio.rrprojects.aetreusbot.gui.SystemTrayController;

public class Controller {
	static Controller controller;
	static SystemTrayController tray;
	static MainWindowController window;
	private static JDA jda;
	
	private static String mainDir = "";
	
	public static void main(String[] args) {
		controller = new Controller();
	}
		
	public Object getController() {
		return controller;
	}
	
	Controller() {
		/*
		 * Starts system tray icon, main GUI window, then starts the bot
		 * */
		
		setMainDir(System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Aetreus Bot");
		
		System.out.println("Starting Program!");
		
		tray = new SystemTrayController();
			tray.startSystemTray();
			
		window = new MainWindowController();	
			window.StartAppWindow();
			
		ConfigController.Init();
	}
	
	static void startDiscordBot() {
		//Started by ConfigController
		
		try {
			jda = new JDABuilder(AccountType.BOT).setToken(ConfigController.getBOT_TOKEN()).build();
		} catch (LoginException e) {
			e.printStackTrace();
		}
		
		Command.init(); //loads all commands
		
		jda.addEventListener(new BotListener());		
	}
	
	public static void StartUpdateTimer() {
		//Started by the message listener so that daily message function can be started properly
		
		int sleepTime = 900;
		
		DailyMessage.Init();
		
		DailyMessage.Update();
		
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
	
	public static void PostLoadInits() {
		//These are all the things that I need to initialize after the bot is loaded
		CharacterFileLoader.LoadCharacters();
	}

	public static void ShutdownBot() {
		jda.shutdown();
		System.exit(0);
	}

	public static JDA getJda() {
		return jda;
	}

	public static String getMainDir() {
		return mainDir;
	}

	public void setMainDir(String mainDir) {
		Controller.mainDir = mainDir;
	}


}
