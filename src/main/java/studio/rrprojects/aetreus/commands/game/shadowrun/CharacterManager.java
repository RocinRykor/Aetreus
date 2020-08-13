package studio.rrprojects.aetreus.commands.game.shadowrun;

import net.dv8tion.jda.api.entities.User;
import studio.rrprojects.aetreus.commands.game.shadowrun.containers.CharacterContainer;
import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.main.Main;
import studio.rrprojects.aetreus.utils.MyMessageBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CharacterManager extends GameCommand {
    @Override
    public String getName() {
        return "character";
    }

    @Override
    public String getAlias() {
        return "char";
    }

    @Override
    public String getHelpDescription() {
        return "Shadowrun 3rd Edition Character Manager";
    }

    @Override
    public String getGame() {
        return "Shadowrun 3rd Edition";
    }

    ShadowrunCharacterCommand characterCommands;
    HashMap<User, CharacterContainer> characterTable = new HashMap<>();

    @Override
    public void executeMain(CommandContainer cmd) {
        String input = cmd.MAIN_ARG;

        //These sub commands specifically handle loading characters
        HashMap<String, Runnable> subcommands = new HashMap<>();
        subcommands.put("load", () -> LoadCharacter(cmd));
        subcommands.put("get", ()-> getCharacter(cmd));
        subcommands.put("save", ()-> saveCharacter(cmd));
        //subcommands.put("list", ()-> listGames(cmd));

        if (subcommands.containsKey(input)) {
            subcommands.get(input).run();
            return;
        }

        //If a command for loading characters is not passed, ensure that the user has a valid loaded character
        if (!HasValidCharacter(cmd.AUTHOR)) {
            SendMessage("I'm sorry, it looks like you do not have a character loaded. Please use the \"&character load [character name]\" and try again", cmd.DESTINATION);
            return;
        }

        CharacterContainer character = characterTable.get(cmd.AUTHOR);
        boolean commandFound = false;
        ShadowrunCharacterCommand passedCommand = null;
        //Check command against the list of Shadowrun Character Commands, if present run that command
        for (ShadowrunCharacterCommand command:
                ShadowrunCharacterCommand.commands) {
            if (input.equalsIgnoreCase(command.getName()) || input.equalsIgnoreCase(command.getAlias())) {
                commandFound = true;
                //If Found, store that command
                passedCommand = command;
            }
        }

        if (commandFound) {
            passedCommand.executeInitial(cmd, character);
        } else {
            SendMessage("I'm sorry, but that doesn't seem to be a valid command.", cmd.DESTINATION);
            return;
        }
    }

    private void saveCharacter(CommandContainer cmd) {
        if (!HasValidCharacter(cmd.AUTHOR)) {
            return;
        }

        String filePath = Main.getDirMainDir() + File.separator + "Shadowrun" + File.separator + "Zerk.json";

        CharacterContainer character = characterTable.get(cmd.AUTHOR);

        character.SaveInfo(filePath);
    }

    private void getCharacter(CommandContainer cmd) {
        if (!HasValidCharacter(cmd.AUTHOR)) {
            return;
        }

        CharacterContainer character = characterTable.get(cmd.AUTHOR);
        MyMessageBuilder message = new MyMessageBuilder();
        message.add(String.format("%s, by %s", character.getCharacter().getName(), cmd.AUTHOR.getName()));
        message.add(String.format("%s %s, Age %s", character.getCharacter().getSex(), character.getCharacter().getRace(), character.getCharacter().getAge()));
        message.add(character.getSpells().getAllAdeptPowers());
        /*
        message.add(character.getAttributes().getAllAttributes());
        message.add(character.getSkills().getActiveSkills().getAllSkills());
        message.add(character.getSkills().getKnowledgeSkills().getAllSkills());
        message.add(character.getContacts().getAllContacts());
        message.add(character.getInventory().getWeapons().getAllWeapons());
        message.add(character.getInventory().getCredsticks().getAllCredsticks());
        message.add(character.getCondition().Display());
        message.add(character.getInventory().getArmor().getAllArmor());
         */

        SendMessage(message.build(false), cmd.DESTINATION);
    }

    private boolean HasValidCharacter(User user) {
        return characterTable.containsKey(user);
    }

    private void LoadCharacter(CommandContainer cmd) {
        String characterName = "";

        if (cmd.SECONDARY_ARG[0] != null) {
            characterName = cmd.SECONDARY_ARG[0];
        }

        String filePath = Main.getDirMainDir() + File.separator + "Shadowrun" + File.separator + characterName + ".json";

        CharacterContainer character = null;

        try {
            character = new CharacterContainer().BuildCharacter(filePath);
            characterTable.put(cmd.AUTHOR, character);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert character != null;
        character.WriteTo(filePath);
    }

    @Override
    public void Initialize() {
        super.Initialize();
        ShadowrunCharacterCommand.init();
    }
}