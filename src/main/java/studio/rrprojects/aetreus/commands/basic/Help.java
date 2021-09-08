package studio.rrprojects.aetreus.commands.basic;

import studio.rrprojects.aetreus.commands.CommandController;
import studio.rrprojects.aetreus.commands.game.shadowrun.GameCommand;
import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.main.Main;
import studio.rrprojects.aetreus.utils.MessageUtils;

public class Help extends BasicCommand {
    @Override
    public String getName() {
        return "Help";
    }

    @Override
    public String getAlias() {
        return "H";
    }

    @Override
    public String getHelpDescription() {
        return "Displays a list of each command that is available to the bot";
    }

    @Override
    public void executeMain(CommandContainer cmd) {
        super.executeMain(cmd);

        String string = Main.getCommand().GetHelpDescriptions();

        MessageUtils.SendMessage(string, cmd.DESTINATION);
    }
}
