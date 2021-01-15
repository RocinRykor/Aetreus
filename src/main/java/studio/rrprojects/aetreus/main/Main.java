package studio.rrprojects.aetreus.main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import studio.rrprojects.aetreus.commands.CommandController;
import studio.rrprojects.aetreus.config.ConfigController;
import studio.rrprojects.aetreus.discord.BotListener;
import studio.rrprojects.aetreus.gui.GUIController;

import javax.security.auth.login.LoginException;
import java.io.File;

public class Main {
    static String dirMainDir;
    static JDA jda;
    static ConfigController config;
    static GUIController gui;
    static CommandController command;
    static BotListener listener;

    public Main(){
        Initialize();
        LoadConfigs();
        LoadCommand();
        LoadDiscord();
        LoadGUI();
    }

    // INIT
    private void Initialize() {
        System.out.println("Main Controller Initialization");
        dirMainDir = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Aetreus Bot";
        System.out.println("Home Directory set at: " + dirMainDir);
    }

    // LOADERS
    private void LoadConfigs() {
        config = new ConfigController();
        config.Initialize();
    }

    private void LoadGUI() {
        gui= new GUIController(jda);
        gui.Initialize();
    }

    private void LoadCommand() {
        command = new CommandController();
        command.Initialize();
    }

    private void LoadDiscord() {
        {
            try {
                jda = new JDABuilder(config.getBotToken()).build();
                listener = new BotListener();
            } catch (LoginException e) {
                e.printStackTrace();
            }
            jda.addEventListener(listener);
            listener.Initialize();
        }
    }

    // MISC
    public static void Ready() {
        command.Ready();
        System.out.println("Bot Ready!");
    }

    // GETTERS
    public static ConfigController getConfig() { return config; }
    public static String getDirMainDir() { return dirMainDir; }
    public static JDA getJda() { return jda; }
    public static BotListener getListener() { return listener;}
    public static CommandController getCommand() { return command; }

    // MAIN
    public static void main(String[] args) {
        new Main();
    }
}
