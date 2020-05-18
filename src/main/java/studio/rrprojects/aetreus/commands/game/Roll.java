package studio.rrprojects.aetreus.commands.game;

import studio.rrprojects.aetreus.commands.Game;
import studio.rrprojects.aetreus.discord.CommandContainer;

import java.util.ArrayList;

public class Roll extends GameCommand {
    @Override
    public String getName() {
        return "roll";
    }

    @Override
    public String getAlias() {
        return "r";
    }

    @Override
    public String getHelpDescription() {
        return "Rolls a specified amount of dice - setting changes depending on current Game";
    }

    @Override
    public String getGame() {
        return "NONE";
    }

    @Override
    public void executeMain(CommandContainer cmd) {
        String currentGame = Game.getCurrentGame();
        String searchTerm = currentGame+"roll";
        System.out.println(searchTerm);

        ArrayList<GameCommand> games = GameCommand.getCommands();
        for (GameCommand command : games) {
            if (command.getName().equalsIgnoreCase(searchTerm)) {
                System.out.println("Command Found! - " + command.getName());
                System.out.println();
                command.executeMain(cmd);
            }
        }

    }
}
