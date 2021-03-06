package studio.rrprojects.aetreus.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import studio.rrprojects.aetreus.commands.admin.AdminCommand;
import studio.rrprojects.aetreus.commands.basic.BasicCommand;
import studio.rrprojects.aetreus.commands.game.shadowrun.GameCommand;
import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.discord.CommandParser;

public class CommandController {
    CommandParser parser;

    public CommandController() {
        parser = new CommandParser();
    }

    public void Initialize() {
        BasicCommand.init();
        AdminCommand.init();
        GameCommand.init();
    }

    public void Ready() {
        BasicCommand.Ready();
        AdminCommand.Ready();
        GameCommand.Ready();
    }

    public void ProcessInput(String inputBeheaded, MessageReceivedEvent event) {
        ProcessInput(inputBeheaded, event, event.getTextChannel().getName());
    }

    public void ProcessInput(String inputBeheaded, MessageReceivedEvent event, String textChannelName) {
        CommandContainer container = parser.Parse(inputBeheaded, event, textChannelName);

        String mainCommand = container.MAIN_COMMAND;
        boolean commandFound = false;
        Object passedCommand = null;

        //This part is gonna suck but I don't see any other way :'(
        //Each type of command needs to be added in manually
        //If I add a new one later I have to remember to add it here as well

        //Basic Command
        for (BasicCommand command:
             BasicCommand.commands) {
            if (mainCommand.equalsIgnoreCase(command.getName()) || mainCommand.equalsIgnoreCase(command.getAlias())) {
                commandFound = true;
                //If Found, store that command
                passedCommand = command;
            }
        }
        if (commandFound) {
            BasicCommand basic = (BasicCommand) passedCommand;
            basic.executeMain(container);
            return;
        }

        //Admin Command
        for (AdminCommand command:
                AdminCommand.commands) {
            if (mainCommand.equalsIgnoreCase(command.getName()) || mainCommand.equalsIgnoreCase(command.getAlias())) {
                commandFound = true;
                //If Found, store that command
                passedCommand = command;
            }
        }
        if (commandFound) {
            AdminCommand admin = (AdminCommand) passedCommand;
            admin.checkRole(container);
            return;
        }

        //Game Command
        for (GameCommand command:
                GameCommand.commands) {
            if (mainCommand.equalsIgnoreCase(command.getName()) || mainCommand.equalsIgnoreCase(command.getAlias())) {
                commandFound = true;
                //If Found, store that command
                passedCommand = command;
            }
        }
        if (commandFound) {
            GameCommand game = (GameCommand) passedCommand;
            game.executeInitial(container);
        }

    }

    public String GetHelpDescriptions() {
        String string = "Basic Commands --- \n";
        for (BasicCommand command : BasicCommand.commands) {
            string += String.format("%s| Alias: %s| %s\n", command.getName(), command.getAlias(), command.getHelpDescription());
        }

        string += "\n\nAdmin Commands --- \n";
        for (AdminCommand command : AdminCommand.commands) {
            string += String.format("%s| Alias: %s| %s\n", command.getName(), command.getAlias(), command.getHelpDescription());
        }

        string += "\n\nGame Commands --- \n";
        for (GameCommand command : GameCommand.commands) {
            string += String.format("%s| Alias: %s| %s\n", command.getName(), command.getAlias(), command.getHelpDescription());
        }

        return string;
    }
}
