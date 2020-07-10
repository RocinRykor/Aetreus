package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

public class CredstickMember {
    String name;
    CredstickContainer container;

    public CredstickMember(String key, CredstickContainer value) {
        name = key;
        container = value;
    }

    public String getName() {
        return name;
    }

    public CredstickContainer getContainer() {
        return container;
    }
}
