package studio.rrprojects.aetreus.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.utils.MessageUtils;

import java.util.ArrayList;

public  abstract class BasicCommand {
    public static ArrayList<BasicCommand> commands;

    public static void init() {
        System.out.println("Loading Basic Commands");
        commands = new ArrayList<>();

        commands.add(new Ping());
        commands.add(new Audio());
        commands.add(new Tester());
        commands.add(new Game());

        System.out.println("Basic Commands Loaded: " + commands.size());
    }

    public static void Ready() {
        for (BasicCommand command: commands) {
            command.Initialize();
        }
    }

    public void Initialize() {
    }

    public abstract String getName();
    public abstract String getAlias();
    public abstract String getHelpDescription();
    public void executeMain(CommandContainer cmd) {};
    public void executeHelp() {};
    public void SendMessage(String message, MessageChannel destination) {
        MessageUtils.SendMessage(message, destination);
    }
}
