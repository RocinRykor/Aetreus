package studio.rrprojects.aetreus.commands.basic;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import studio.rrprojects.aetreus.commands.basic.BasicCommand;
import studio.rrprojects.aetreus.commands.game.shadowrun.GameCommand;
import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.main.Main;
import studio.rrprojects.aetreus.utils.MyMessageBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangeGame extends BasicCommand {
    ArrayList<GameCommand> commands;
    HashMap<String, String> gameTable;
    JDA jda;
    String defaultGame = "sr3";
    static String currentGame;

    @Override
    public String getName() {
        return "Game";
    }

    @Override
    public String getAlias() {
        return "g";
    }

    @Override
    public String getHelpDescription() {
        return "Changes what \"Game\" the bot is currently playing - &roll commands point to that game";
    }

    @Override
    public void executeMain(CommandContainer cmd) {
        String input = cmd.MAIN_ARG;

        HashMap<String, Runnable> subcommands = new HashMap<>();
        subcommands.put("reset", this::ResetGame);
        subcommands.put("set", ()-> setGame(cmd));
        subcommands.put("list", ()-> listGames(cmd));

        if (subcommands.containsKey(input)) {
            subcommands.get(input).run();
        }
    }

    private void listGames(CommandContainer cmd) {
        System.out.println("Listing games");
        MyMessageBuilder message = new MyMessageBuilder();

        message.add("List of available games: ");
        message.addBlankLine();

        for (Map.Entry<String, String> game: gameTable.entrySet()) {
            message.add("Game: " + game.getValue() + " | Key: " + game.getKey());
        }

        message.addBlankLine();
        message.addWithUnderLine("To change the current game use \"&game set <KEY>\"");

        SendMessage(message.build(true), cmd.DESTINATION);
    }

    private void setGame(CommandContainer cmd) {
        if (cmd.SECONDARY_ARG != null) {
            String key = cmd.SECONDARY_ARG[0];
            if (gameTable.containsKey(key)) {
                SetPresence(key);
                SendMessage("Game set to: " + gameTable.get(key), cmd.DESTINATION);
            } else {
                SendMessage("Sorry I can't find that game", cmd.DESTINATION);
                listGames(cmd);
            }
        } else {
            SendMessage("ERROR - You must specify a game", cmd.DESTINATION);
            listGames(cmd);
        }
    }

    private void ResetGame() {
        SetPresence(defaultGame);
    }

    private void SetPresence(String key) {
        String game = gameTable.get(key);
        jda.getPresence().setActivity(Activity.playing(game));
        currentGame = key;
    }

    public static String getCurrentGame() {
        return currentGame;
    }

    @Override
    public void Initialize() {
        commands = GameCommand.getCommands();
        jda = Main.getJda();
        gameTable= new HashMap<>();

        gameTable.put("sr3", "ShadowRun 3rd Edition");
        gameTable.put("dnd", "Dungeons and Dragons");

        ResetGame();
        System.out.println("GAME Initialized");
    }
}
