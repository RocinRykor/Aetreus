package studio.rrprojects.aetreus.commands.game.shadowrun;

import studio.rrprojects.aetreus.commands.game.shadowrun.containers.CharacterContainer;
import studio.rrprojects.aetreus.discord.CommandContainer;

import java.util.HashMap;

public class DamageCharacter extends ShadowrunCharacterCommand {
    @Override
    public String getName() {
        return "Damage";
    }

    @Override
    public String getAlias() {
        return "D";
    }

    @Override
    public String getHelpDescription() {
        return null;
    }

    HashMap<String, Integer> damageCodeTable;
    HashMap<Character, String> damageTypeTable;

    @Override
    public void executeMain(ShadowrunCommandContainer cmd, CharacterContainer character, CommandContainer commandContainer) {
        super.executeMain(cmd, character, commandContainer);

        if (cmd.primaryFunction == null) {
            return;
        }

        String primaryFunction = cmd.primaryFunction;
        String secondaryValue = cmd.secondaryFunction;

        int damageValue = 0;;
        if (isDigit(primaryFunction)) {
            damageValue = Integer.parseInt(primaryFunction);
        } else {
            damageValue = damageCodeTable.get(primaryFunction);
        }

        if (damageValue == 0) {
            SendMessage("I'm sorry, I cant resolve that damage amount", commandContainer.DESTINATION);
            return;
        }

        String damageType;
        if (secondaryValue == null) {
            damageType = "Stun";
        } else {
            Character searchTerm = secondaryValue.toLowerCase().charAt(0);
            if (damageTypeTable.containsKey(searchTerm)) {
                damageType = damageTypeTable.get(searchTerm);
            } else {
                damageType = "Stun";
            }
        }

        if (damageType.equalsIgnoreCase("Stun")) {
            ResolveStunDamage(damageValue, character);
        } else {
            ResolvePhysDamage(damageValue, character);
        }

        SendMessage(character.getCondition().Display(), commandContainer.DESTINATION);
    }

    private void ResolveStunDamage(int damageValue, CharacterContainer character) {
        int currentValue = character.getCondition().getStun();
        int newValue = currentValue + damageValue;

        int overflowValue = 0;
        if (newValue < 0) {
            newValue = 0;
        } else if (newValue > 10) {
            overflowValue = newValue - 10;
            newValue = 10;
        }

        character.getCondition().setStun(newValue);
        ResolvePhysDamage(overflowValue, character);
    }

    private void ResolvePhysDamage(int damageValue, CharacterContainer character) {
        int currentValue = character.getCondition().getPhysical();
        int newValue = currentValue + damageValue;

        int overflowValue = 0;
        if (newValue < 0) {
            newValue = 0;
        } else if (newValue > 10) {
            overflowValue = newValue - 10;
            newValue = 10;
        }

        character.getCondition().setPhysical(newValue);
        ResolveOverFlowDamage(overflowValue, character);
    }

    private void ResolveOverFlowDamage(int damageValue, CharacterContainer character) {
        int currentValue = character.getCondition().getOverflow();
        int newValue = currentValue + damageValue;

        int overflowValue = 0;
        if (newValue < 0) {
            newValue = 0;
        } else if (newValue > 10) {
            overflowValue = newValue - 10;
            newValue = 10;
        }

        character.getCondition().setOverflow(newValue);
    }

    private boolean isDigit(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void Initialize() {
        super.Initialize();

        //Damage Code
        damageCodeTable = new HashMap<>();
        damageCodeTable.put("l", 1);
        damageCodeTable.put("m", 3);
        damageCodeTable.put("s", 6);
        damageCodeTable.put("d", 10);

        //Damage Type
        damageTypeTable = new HashMap<>();
        damageTypeTable.put('p', "Physical");
        damageTypeTable.put('s', "Stun");
    }
}
