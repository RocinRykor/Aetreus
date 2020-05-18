package studio.rrprojects.aetreus.config;

import studio.rrprojects.aetreus.main.Main;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigController {
    File fileConfig;
    Properties prop;
    FileReader reader;

    private String botToken;
    private String botPrefix;
    private String botSecondPrefix;
    private String botMessage;

    public void Initialize() {
        System.out.println("Config Controller Initialization");

        /* TODO
        * Create Config file used by bot
        * Create functions for adding/reading specific lines from the config file
        *   -> Allows multiple classes to use same config file
        * */
        String configName = "BotInfo.cfg";
        String configDir = Main.getDirMainDir();

        //Quick test to ensure the dir is valid
        File mainDir = new File(configDir);
        if (!mainDir.exists()) mainDir.mkdirs();

        String configPath = configDir + File.separator + configName;
        fileConfig = new File(configPath);
        prop = new Properties();
        reader = null;

        //Check if exists
        if (!fileConfig.exists()) {
            System.out.println("Creating a config file at " + configPath + "\n" +
                    "Please edit that file with a valid token and restart the bot.");
            CreateConfigFile();
            System.exit(0);
        }

        LoadConfigFile();
    }

    private void LoadConfigFile() {
        System.out.println("Loading Config File!");
        try {
            reader = new FileReader(fileConfig);
            prop.load(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setBotToken(prop.getProperty("botToken"));
        setBotPrefix(prop.getProperty("botPrefix"));
        setBotSecondPrefix(prop.getProperty("botSecondPrefix"));
        setBotMessage(prop.getProperty("botMessage"));

        System.out.println("Config Loaded!");
    }

    private void CreateConfigFile() {
        HashMap<String, String> variableTable = new HashMap<String, String>();
        variableTable.put("botPrefix", "&");
        variableTable.put("botSecondPrefix", String.valueOf('!'));
        variableTable.put("botToken", "CHANGE ME BEFORE RELOADING THE BOT");
        variableTable.put("botMessage", "Say \"& Help\" to begin.");

        try {
            fileConfig.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, String> item : variableTable.entrySet()) {
            prop.setProperty(item.getKey(), item.getValue());
        }

        try {
            prop.store(new FileOutputStream(fileConfig), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GETTERS
    public String getBotToken() {
        return botToken;
    }
    public String getBotPrefix() {
        return botPrefix;
    }
    public String getBotSecondPrefix() {
        return botSecondPrefix;
    }
    public String getBotMessage() {
        return botMessage;
    }

    //SETTERS
    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setBotPrefix(String botPrefix) {
        this.botPrefix = botPrefix;
    }

    public void setBotSecondPrefix(String botSecondPrefix) {
        this.botSecondPrefix = botSecondPrefix;
    }

    public void setBotMessage(String botMessage) {
        this.botMessage = botMessage;
    }


}
