package studio.rrprojects.aetreus.commands.game.shadowrun;

import studio.rrprojects.aetreus.commands.game.shadowrun.containers.CharacterContainer;
import studio.rrprojects.aetreus.discord.CommandContainer;

public class InitiativeCharacter extends ShadowrunCharacterCommand {
    @Override
    public String getName() {
        return "Initiative";
    }

    @Override
    public String getAlias() {
        return "I";
    }

    @Override
    public String getHelpDescription() {
        return null;
    }

    @Override
    public void executeMain(ShadowrunCommandContainer cmd, CharacterContainer character, CommandContainer commandContainer) {
        super.executeMain(cmd, character, commandContainer);

        System.out.println("Rolling Initiative");

        int valueInit = character.getAttributes().getAttribute("initiative").getContainer().getTotal();
        int valueReactionBase = character.getAttributes().getAttribute("reaction").getContainer().getTotal();

        System.out.println(valueReactionBase);
        int valueReactionTotal = valueReactionBase - character.getCondition().getWoundModifier();
        System.out.println(valueReactionTotal);

        ShadowrunThirdRoller roller = new ShadowrunThirdRoller();
        roller.RollInit(valueInit, valueReactionTotal, commandContainer);
    }
}
