package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

public class AttributeMember {
    String name;
    AttributeContainer container;

    public AttributeMember(String key, AttributeContainer value) {
        name = key;
        container = value;
    }

    public String getName() {
        return name;
    }

    public AttributeContainer getContainer() {
        return container;
    }
}
