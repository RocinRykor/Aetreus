package studio.rrprojects.aetreus.commands.game.shadowrun;

import net.dv8tion.jda.api.entities.User;
import studio.rrprojects.aetreus.commands.game.GameCommand;
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

    HashMap<User, CharacterContainer> characterTable = new HashMap<>();

    @Override
    public void executeMain(CommandContainer cmd) {
        HashMap<String, Runnable> subcommands = new HashMap<>();
        subcommands.put("load", () -> LoadCharacter(cmd));
        //subcommands.put("get", ()-> setGame(cmd));
        //subcommands.put("list", ()-> listGames(cmd));

        String input = cmd.MAIN_ARG;
        if (subcommands.containsKey(input)) {
            subcommands.get(input).run();
        }
    }

    private void LoadCharacter(CommandContainer cmd) {
        String filePath = Main.getDirMainDir() + File.separator + "Shadowrun" + File.separator + "Zerk.json";

        CharacterContainer character = null;

        try {
            character = new CharacterContainer().BuildCharacter(filePath);
            characterTable.put(cmd.AUTHOR, character);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert character != null;
        MyMessageBuilder message = new MyMessageBuilder();
        message.add(String.format("%s, by %s", character.getCharacter().getName(), cmd.AUTHOR.getName()));
        message.add(String.format("%s %s, Age %s", character.getCharacter().getSex(), character.getCharacter().getRace(), character.getCharacter().getAge()));


        /*
        message.add(character.getAttributes().getAllAttributes());
        message.add(character.getSkills().getActiveSkills().getAllSkills());
        message.add(character.getSkills().getKnowledgeSkills().getAllSkills());
        message.add(character.getContacts().getAllContacts());
         */

        character.WriteTo(filePath);

        SendMessage(message.build(false), cmd.DESTINATION);
    }
}


/*
private String getAllSkills(CharacterContainer character) {
        ArrayList<ActiveSkillContainer> activeSkills;
        activeSkills = character.getActiveSkills();

        StringBuilder tmpString = new StringBuilder();
        for (ActiveSkillContainer skill: activeSkills) {
            StringBuilder line = new StringBuilder(String.format("%s: %s", skill.name, skill.level));
            if (skill.specializations.size() > 0) {
                for (Map.Entry<String, Integer> entry : skill.specializations.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    line.append(String.format(", %s: %s", key, value));
                }
            }
            tmpString.append(line).append("\n");
        }
        return String.valueOf(tmpString);
    }

    private String getAllAttributes(CharacterContainer character) {
        StringBuilder string = new StringBuilder();
        HashMap<String, Integer> attributes = new HashMap<>();
        attributes.put("body", 0);
        attributes.put("quickness", 0);
        attributes.put("strength", 0);
        attributes.put("charisma", 0);
        attributes.put("intelligence", 0);
        attributes.put("willpower", 0);
        attributes.put("essence", 0);
        attributes.put("reaction", 0);


        for (Map.Entry<String, Integer> attribute: attributes.entrySet()) {
            attribute.setValue(character.getAttribute(attribute.getKey()));
            StringBuilder append = string.append(String.format("%s: %d\n",
                    attribute.getKey(),
                    attribute.getValue()));
        }

        return string.toString();
    }
 */