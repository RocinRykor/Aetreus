package studio.rrprojects.aetreus.commands.game.shadowrun;

import net.dv8tion.jda.api.entities.MessageChannel;
import studio.rrprojects.aetreus.commands.game.shadowrun.containers.CharacterContainer;
import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.utils.MessageUtils;

import java.util.ArrayList;

public abstract class ShadowrunCharacterCommand {

    public static ArrayList<ShadowrunCharacterCommand> commands;

    public static void init() {
        System.out.println("Loading Shadowrun Character Commands");
        commands = new ArrayList<>();

        commands.add(new RollCharacter());

        System.out.println("Shadowrun Character Commands Loaded: " + commands.size());
        SkillTable.InitTables();
    }

    public abstract String getName();
    public abstract String getAlias();
    public abstract String getHelpDescription();
    public void executeMain(ShadowrunCommandContainer cmd, CharacterContainer character, CommandContainer commandContainer) {}
    public void executeInitial(CommandContainer cmd, CharacterContainer character) {
        ShadowrunCommandContainer src = new ShadowrunCommandContainer(cmd.RAW, cmd.DESTINATION);
        executeMain(src, character, cmd);
    }
    public void SendMessage(String message, MessageChannel destination) {
        MessageUtils.SendMessage(message, destination);
    }
}
