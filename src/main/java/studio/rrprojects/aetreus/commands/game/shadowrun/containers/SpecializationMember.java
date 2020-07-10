package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

public class SpecializationMember {
    String name;
    SkillSpecializationContainer container;

    public SpecializationMember(String key, SkillSpecializationContainer value) {
        name = key;
        container = value;
    }

    public String getName() {
        return name;
    }

    public SkillSpecializationContainer getContainer() {
        return container;
    }
}
