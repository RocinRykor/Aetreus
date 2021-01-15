package studio.rrprojects.aetreus.commands.basic;

import studio.rrprojects.aetreus.commands.basic.BasicCommand;
import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.utils.MessageUtils;

public class Ping extends BasicCommand {
    @Override
    public String getName() {
        return "Ping";
    }

    @Override
    public String getAlias() {
        return "P";
    }

    @Override
    public String getHelpDescription() {
        return "Pings the server, if successful the bot will respond";
    }

    @Override
    public void executeMain(CommandContainer cmd) { MessageUtils.SendMessage("PONG!", cmd.DESTINATION); }
}
