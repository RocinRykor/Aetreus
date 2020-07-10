package studio.rrprojects.aetreus.commands.game.shadowrun;

import studio.rrprojects.aetreus.commands.game.shadowrun.containers.CharacterContainer;
import studio.rrprojects.aetreus.commands.game.shadowrun.containers.CredstickMember;
import studio.rrprojects.aetreus.discord.CommandContainer;

import java.util.ArrayList;
import java.util.HashMap;

public class MoneyCharacter extends ShadowrunCharacterCommand {
    @Override
    public String getName() {
        return "Money";
    }

    @Override
    public String getAlias() {
        return "M";
    }

    @Override
    public String getHelpDescription() {
        return null;
    }

    @Override
    public void executeMain(ShadowrunCommandContainer cmd, CharacterContainer character, CommandContainer commandContainer) {
        super.executeMain(cmd, character, commandContainer);

        if (cmd.primaryFunction == null) {
            return;
        }

        String primaryFunction = cmd.primaryFunction;
        String secondaryValue = cmd.secondaryFunction;

        HashMap<String, Runnable> functionTable = new HashMap<>();
        functionTable.put("add", () -> AddMoney(character, secondaryValue, commandContainer));
        functionTable.put("gain", () -> AddMoney(character, secondaryValue, commandContainer));
        functionTable.put("spend", () -> RemoveMoney(character, secondaryValue, commandContainer));
        functionTable.put("remove", () -> RemoveMoney(character, secondaryValue, commandContainer));
        functionTable.put("list", () -> ListMoney(character, commandContainer));

        if (functionTable.containsKey(primaryFunction.toLowerCase())) {
            functionTable.get(primaryFunction.toLowerCase()).run();
        }
    }

    private void AddMoney(CharacterContainer character, String secondaryValue, CommandContainer commandContainer) {
        if (secondaryValue == null) {
            return;
        }

        int changeValue = Integer.parseInt(secondaryValue);

        CredstickMember credstick = character.getInventory().getCredsticks().getPrimaryCredstick();
        int newTotal = credstick.getContainer().getNuyen() + changeValue;

        character.getCharacter().getCareer().setNuyen(character.getCharacter().getCareer().getNuyen() + changeValue);
        credstick.getContainer().setNuyen(newTotal);
        ListMoney(character, commandContainer);
    }

    private void RemoveMoney(CharacterContainer character, String secondaryValue, CommandContainer commandContainer) {
        if (secondaryValue == null) {
            return;
        }

        int changeValue = Integer.parseInt(secondaryValue);

        CredstickMember credstick = character.getInventory().getCredsticks().getPrimaryCredstick();
        int newTotal = credstick.getContainer().getNuyen() - changeValue;

        if (newTotal < 0) {
            SendMessage("Sorry, you don't have enough money to remove that amount", commandContainer.DESTINATION);
            return;
        }

        credstick.getContainer().setNuyen(newTotal);
        ListMoney(character, commandContainer);
    }

    private void ListMoney(CharacterContainer character, CommandContainer commandContainer) {
        ArrayList<CredstickMember> credStickList = character.getInventory().getCredsticks().getAllCredsticks();

        String message = "";
        for (CredstickMember credstick : credStickList) {
            message += String.format("Name: %s (Rating: %d) - Amount: %d¥\n", credstick.getName(), credstick.getContainer().getRating(), credstick.getContainer().getNuyen());
        }

        SendMessage(message, commandContainer.DESTINATION);
    }
}
