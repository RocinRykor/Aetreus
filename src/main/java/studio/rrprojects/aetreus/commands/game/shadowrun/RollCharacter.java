package studio.rrprojects.aetreus.commands.game.shadowrun;

import studio.rrprojects.aetreus.commands.game.shadowrun.containers.*;
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
    public void executeMain(ShadowrunCommandContainer cmd, CharacterContainer character, CommandContainer commandContainer) {
        super.executeMain(cmd, character, commandContainer);

        if (cmd.primaryFunction == null) {
            SendMessage("I'm sorry, but your command is missing important information, please be sure to include the skill you wish to roll and the base target value \n" +
                    "For example \"&character roll Blades 4\"", cmd.DESTINATION);
            return;
        }

        String searchTerm = cmd.primaryFunction;
        int baseTargetValue;
        if (cmd.secondaryFunction != null) {
            baseTargetValue = Integer.parseInt(cmd.secondaryFunction);
        } else {
            baseTargetValue = 4;
        }

        //Search for matching Skill or Attribute
        String result = null;
        String note;
        int parsedValue = 0;

        Object member = FindMatch(searchTerm, character);
        if (member instanceof AttributeMember) {
            result = ((AttributeMember) member).getName();
            parsedValue = ((AttributeMember) member).getContainer().getTotal();
        } else if (member instanceof ActiveSkillMember) {
            result = ((ActiveSkillMember) member).getName();
            parsedValue = ((ActiveSkillMember) member).getContainer().getLevel();
        } else if (member instanceof SpecializationMember) {
            result = ((SpecializationMember) member).getName();
            parsedValue = ((SpecializationMember) member).getContainer().getLevel();
        } else if (member instanceof KnowledgeSkillMember) {
            result = ((KnowledgeSkillMember) member).getName();
            parsedValue = ((KnowledgeSkillMember) member).getContainer().getLevel();
        }

        note = String.format("Search Term: `%s`\n" +
                "Matching Attribute: '%s'\n" +
                "Value: %d", searchTerm, result, parsedValue);

        if (parsedValue == 0) {
            SendMessage("I'm sorry I didn't find that search term on your sheet", cmd.DESTINATION);
            return;
        }

        //Calculate Threshold
        //Start with any damage to player
        int characterWoundModifier = character.getCondition().getWoundModifier();
        int totalTarget = baseTargetValue + characterWoundModifier;

        //Package information and send it to SR3Roll
        DicePackageSR dicePackage = new DicePackageSR();
        dicePackage.dicePool = parsedValue;
        dicePackage.totalTarget = totalTarget;

        ShadowrunThirdRoller srRoll = new ShadowrunThirdRoller();
        srRoll.RollDicePackage(dicePackage, commandContainer);

        String message = String.format("\nYour character has a wound modifier of `%d` bringing your target value to: %d", characterWoundModifier, totalTarget);

        message += "\n" + note;

        SendMessage(message, cmd.DESTINATION);
    }

    private Object FindMatch(String searchTerm, CharacterContainer character) {
        //Attribute
        if (character.getAttributes().containsAttribute(searchTerm)) {
            return character.getAttributes().getAttribute(searchTerm);
        }

        //Active Skills
        if (character.getSkills().getActiveSkills().containsSkill(searchTerm)) {
            return character.getSkills().getActiveSkills().getSkill(searchTerm);
        }

        //Check Active Skill Specialization;
        if (character.getSkills().getActiveSkills().containsSpecialization(searchTerm)) {
            return character.getSkills().getActiveSkills().getSpecialization(searchTerm);
        }

        //Knowledge Skills
        if (character.getSkills().getKnowledgeSkills().containsSkill(searchTerm)) {
            return character.getSkills().getKnowledgeSkills().getSkill(searchTerm);
        }

        //IF NONE
        return null;
    }

    static class DicePackageSR {
        public int totalTarget;
        public int dicePool;
    }
}

/*
        * //Start by looking at Attributes
        for (String attribute: SkillTable.tableAttributes) {
            if (attribute.toLowerCase().contains(searchTerm.toLowerCase())) {
                resultType = "Attribute";
                result = attribute;
                foundMatch = true;
            }
        }
        //Start looking at Active Skills and Knowledge Skills if no result has been found so far
        if (!foundMatch) {
            for (String activeSkill: SkillTable.tableActiveSkills) {
                if (activeSkill.toLowerCase().contains(searchTerm.toLowerCase())) {
                    resultType = "Active Skill";
                    result = activeSkill;
                    foundMatch = true;
                }
            }

            for (String knowledgeSkill: SkillTable.tableKnowledgeSkills) {
                if (knowledgeSkill.toLowerCase().contains(searchTerm.toLowerCase())) {
                    resultType = "Knowledge Skill";
                    result = knowledgeSkill;
                    foundMatch = true;
                }
            }
        }

        //Look at Weapons

        String message;
        int parsedValue;
        if (foundMatch) {
            if (resultType.equalsIgnoreCase("Attribute")) {
                parsedValue = character.getAttributes().getAttribute(result);
            } else if (resultType.equalsIgnoreCase("Active Skill")) {
                parsedValue = character.getSkills().getActiveSkills().getSkillByName(result);
            } else {
                parsedValue = character.getSkills().getKnowledgeSkills().getSkillByName(result);
            }

            message = String.format("Success! I was able to match the search term `%s` to your characters %s attribute of: %d", searchTerm, result, parsedValue);
        } else {
            message = "I'm sorry, I couldn't find that particular search term in your character sheet";
            return;
        }

        //Calculate Threshold
        //Start with any damage to player
        int characterWoundModifier = character.getCondition().getWoundModifier();
        int totalTarget = baseTargetValue + characterWoundModifier;
        * */
