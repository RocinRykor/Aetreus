package studio.rrprojects.aetreus.commands.admin;

import net.dv8tion.jda.api.entities.Role;
import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.main.Main;

import java.util.ArrayList;

public abstract class AdminCommand {
    public static ArrayList<AdminCommand> commands;

    public static void init() {
        System.out.println("Loading Admin Commands");
        commands = new ArrayList<>();

        commands.add(new Admin());

        System.out.println("Admin Commands Loaded: " + commands.size());
    }

    public static void Ready() {
    }

    public abstract String getName();
    public abstract String getAlias();
    public abstract String getHelpDescription();
    public void executeMain(CommandContainer cmd) {}
    public void checkRole(CommandContainer cmd) {
        Role adminRole = Main.getJda().getGuilds().get(0).getRolesByName("Admins", true).get(0);
        boolean hasRole = cmd.JDA.getGuilds().get(0).getMember(cmd.AUTHOR).getRoles().contains(adminRole);

        if (!hasRole) {
            cmd.EVENT.getMessage().delete().queue();
            cmd.AUTHOR.openPrivateChannel().complete().sendMessage("Sorry, you do not have the required role for the command.").queue();
            return;
        }

        executeMain(cmd);
    }
    public void executeHelp() {};
}
