package studio.rrprojects.aetreusbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import studio.rrprojects.aetreusbot.gui.ConfigControllerGUI;

public class ConfigController {
	/*
	 * Check for valid config file
	 * If not found -> generate new file, prompt user for information
	 * Load the file
	 * */
	
	private static String BOT_TOKEN = ""; //Token for loading the bot.
	private static String BOT_PREFIX = ""; //Prefix character for chat messages.
	private static String BOT_SECONDARY_PREFIX = ""; //Name for the bot
	private static String BOT_MESSAGE = ""; //Message for the bot, displays next to "Currently Playing:"
	
	static File file;
	static Properties prop;
	static FileReader reader;
	
	static String configFile;
	
	public static void Init() {
		System.out.println("Starting Config Initilization");
		
		//Initializes the file location of the config file set to C://User/USERNAME/Documents/Aetreus Bot/BotInfo.cfg"
			String fileName = "BotInfo.cfg";
			String configDir = Controller.getMainDir();
			configFile = configDir + File.separator + fileName;
			
			//Initializes Properties and FileReader
			prop = new Properties();
			reader = null;
			
			//Checks if directories of config file exist and create them if necessary.
			File dir = new File(configDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			//Checks if the actual config files exists, creates if necessary.
			file = new File(configFile);
			
			if (!file.exists()) {
				System.out.println("Config File Not Found!");
				RunFirstTimeSetup();
			} else {
				LoadConfigFile();
			}
		
	}

	private static void LoadConfigFile() {
		//Loads the valid file
		try {
			reader = new FileReader(configFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			prop.load(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BOT_TOKEN = prop.getProperty("BOT_TOKEN");
		BOT_PREFIX = prop.getProperty("BOT_PREFIX");
		BOT_SECONDARY_PREFIX = prop.getProperty("BOT_SECOND_PREFIX");
		BOT_MESSAGE = prop.getProperty("BOT_MESSAGE");
	
		System.out.println("Config Loaded");
		
		//Once everything is loaded start the bot
		Controller.startDiscordBot();
	}

	private static void RunFirstTimeSetup() {
		System.out.println("Creating config file");
		ConfigControllerGUI.OpenConfigWindow();
	}
	
	public static void CreateConfigFile(String tempBotToken, String tempBotPrefix, String tempBotSecondPrefix, String tempBotMessage) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//If file was created, sets the properties
		prop.setProperty("BOT_TOKEN", tempBotToken.toString());
		prop.setProperty("BOT_PREFIX", tempBotPrefix);
		prop.setProperty("BOT_SECOND_PREFIX", tempBotSecondPrefix);
		prop.setProperty("BOT_MESSAGE", tempBotMessage);
		
		//Writes properties to the newly created file
		try {
			prop.store(new FileOutputStream(configFile), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LoadConfigFile();
	}
	
	public static String getBOT_TOKEN() {
		return BOT_TOKEN; 
	}
	
	public static String getBOT_PREFIX() {
		return BOT_PREFIX; 
	}
	
	public static String getBOT_SECONDARY_PREFIX() {
		return BOT_SECONDARY_PREFIX; 
	}
	
	public static String getBOT_MESSAGE() {
		return BOT_MESSAGE; 
	}

}
