package studio.rrprojects.aetreus.commands.game;

import net.dv8tion.jda.api.entities.MessageChannel;
import studio.rrprojects.aetreus.commands.game.shadowrun.CharacterManager;
import studio.rrprojects.aetreus.commands.game.shadowrun.Priority;
import studio.rrprojects.aetreus.commands.game.shadowrun.ShadowrunThirdRoller;
import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.utils.MessageUtils;

import java.util.ArrayList;

public abstract class GameCommand {
    public static ArrayList<GameCommand> commands;

    public static void init() {
        System.out.println("Loading Game Commands");
        commands = new ArrayList<>();

        commands.add(new Roll());
        commands.add(new DNDRoller());
        commands.add(new ShadowrunThirdRoller());
        commands.add(new Priority());
        commands.add(new CharacterManager());

        System.out.println("Game Commands Loaded: " + commands.size());
    }

    public static void Ready() {
        for (GameCommand command: commands) {
            command.Initialize();
        }
    }

    public void Initialize() {
    }

    public static ArrayList<GameCommand> getCommands() {
        return commands;
    }

    public abstract String getName();
    public abstract String getAlias();
    public abstract String getHelpDescription();
    public abstract String getGame();
    public void executeMain(CommandContainer cmd) {}
    public void executeInitial(CommandContainer cmd) {
        executeMain(cmd);
    }
    public void SendMessage(String message, MessageChannel destination) {
        MessageUtils.SendMessage(message, destination);
    }
}
