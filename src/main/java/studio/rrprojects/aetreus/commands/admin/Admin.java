package studio.rrprojects.aetreus.commands.admin;

import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.discord.CommandParser;
import studio.rrprojects.aetreus.utils.MessageUtils;

public class Admin extends AdminCommand {
    @Override
    public String getName() {
        return "Admin";
    }

    @Override
    public String getAlias() {
        return "A";
    }

    @Override
    public String getHelpDescription() {
        return "Like the Ping Command, except checks to see if person is an admin first";
    }

    @Override
    public void executeMain(CommandContainer cmd) {
        MessageUtils.SendMessage("Congrats, you are an admin", cmd.DESTINATION);
    }
}
