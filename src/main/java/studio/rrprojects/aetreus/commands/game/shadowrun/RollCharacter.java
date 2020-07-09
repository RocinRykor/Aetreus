package studio.rrprojects.aetreus.commands.game.shadowrun;

import studio.rrprojects.aetreus.commands.game.shadowrun.containers.CharacterContainer;
import studio.rrprojects.aetreus.discord.CommandContainer;

public class RollCharacter extends ShadowrunCharacterCommand{
    public RollCharacter() {
        super();
    }

    @Override
    public String getName() {
        return "Roll";
    }

    @Override
    public String getAlias() {
        return "R";
    }

    @Override
    public String getHelpDescription() {
        return null;
    }

    @Override
    public void executeMain(ShadowrunCommandContainer cmd, CharacterContainer character) {
        super.executeMain(cmd, character);

        if (cmd.hasNull) {
            SendMessage("I'm sorry, but your command is missing important information, please be sure to include the skill you wish to roll and the base target value \n" +
                    "For example \"&character roll Blades 4\"", cmd.DESTINATION);
            return;
        }

        String searchTerm = cmd.primaryFunction;
        int baseTargetValue = Integer.parseInt(cmd.secondaryFunction);

        //Search for matching Skill or Attribute
        String result = null;
        boolean foundMatch = false;
        for (String attribute: SkillTable.tableAttributes) {
            if (attribute.toLowerCase().contains(searchTerm.toLowerCase())) {
                result = attribute;
                foundMatch = true;
            }
        }

        String message;
        if (foundMatch) {
            int parsedValue = character.getAttributes().getAttribute(searchTerm);
            message = String.format("Success! I was able to match the search term `%s` to your characters %s attribute of: %d", searchTerm, result, parsedValue);
        } else {
            message = "I'm sorry, I couldn't find that particular search term in your character sheet";
        }

        SendMessage(message, cmd.DESTINATION);
    }
}
