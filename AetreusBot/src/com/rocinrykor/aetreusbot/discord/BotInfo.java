package com.rocinrykor.aetreusbot.discord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class BotInfo {
	private static String BOT_TOKEN = ""; //Token for loading the bot.
	private static String BOT_PREFIX = ""; //Prefix character for chat messages.
	private static String BOT_GUILD = ""; //Guild that the bot searchs for
	private static String BOT_ROLE = ""; //Role that the bot searchs for
	private static String BAXTER_TOKEN = ""; //Token for the Baxter (pet) bot.
	
	public BotInfo() {
		
		//Initializes the file location of the config file set to C://User/USERNAME/Documents/Baxter Bot/BaxterInfo.cfg"
		String fileName = "AetreusInfo.cfg";
		String configDir = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Aetreus Bot";
		String configFile = configDir + File.separator + fileName;
		
		//Initializes Properties and FileReader
		Properties prop = new Properties();
		FileReader reader = null;
		
		//Checks if directories of config file exist and create them if necessary.
		File dir = new File(configDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		//Checks if the actual config files exists, creates if necessary.
		File file = new File(configFile);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//If file was created, sets the two properties
			prop.setProperty("BOT_TOKEN", "DEFUALT_TOKEN_PLEASE_CHANGE");
			prop.setProperty("BOT_PREFIX", "&");
			prop.setProperty("BOT_GUILD", "DEFUALT_GUILD_PLEASE_CHANGE");
			prop.setProperty("BOT_ROLE", "DEFUALT_ROLE_PLEASE_CHANGE");
			prop.setProperty("BAXTER_TOKEN","DEFUALT_TOKEN_PLEASE_CHANGE");
			
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
		}
		
		//Opens and reads the config file
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
		
		//Gathers the property values from config file
		BOT_TOKEN = prop.getProperty("BOT_TOKEN", "0");
		BOT_PREFIX = prop.getProperty("BOT_PREFIX", "&");
		BOT_GUILD  = prop.getProperty("BOT_GUILD", "Steve's Cafe");
		BOT_ROLE  = prop.getProperty("BOT_ROLE", "admin");
		BAXTER_TOKEN = prop.getProperty("BAXTER_TOKEN", "0");
	}
	
	public static String getBOT_TOKEN() {
		return BOT_TOKEN; 
	}
	
	public static String getBOT_PREFIX() {
		return BOT_PREFIX; 
	}
	
	public static String getBAXTER_TOKEN() {
		return BAXTER_TOKEN;
	}

}
